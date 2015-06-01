/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package efacsign.sri.soap.autorizacion;

import efacsign.util.EntityManagerUtil;
import efacsign.util.FileUtil;
import efacsign.util.XML_Utilidades;
import efacsign.model.Comprobante;
import efacsign.model.Mensaje;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.text.SimpleDateFormat;
import javax.persistence.EntityManager;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;

/**
 *
 * @author desarrollador
 */
public class SoapAutorizacion {

    public static XML_Utilidades xml_utilidades = new XML_Utilidades();

    public String formatSendPost(String codAcceso) {

        String xml = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ec='http://ec.gob.sri.ws.autorizacion'>"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<ec:autorizacionComprobante>"
                + "<claveAccesoComprobante>" + codAcceso + "</claveAccesoComprobante>"
                + "</ec:autorizacionComprobante>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

        return xml;
    }

    public void getAutorizacion(Document doc, Comprobante comprobante) throws XPathExpressionException {        
        String pathLevelAutorizacon = "//RespuestaAutorizacionComprobante/autorizaciones/autorizacion[last()]/";
        String pathLevelMensajes = "//RespuestaAutorizacionComprobante/autorizaciones/autorizacion/mensajes[last()]/mensaje/";

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String estado = xml_utilidades.getLastNode(pathLevelAutorizacon, "estado", doc);
        EntityManager em = EntityManagerUtil.getEntityManager();

        if (estado.equals("AUTORIZADO")) {
            
            try {                                
                String fa = xml_utilidades.getLastNode(pathLevelAutorizacon, "fechaAutorizacion", doc);                
                
                comprobante.setEstado("Autorizado"); 
                comprobante.setNumero_autorizacion(xml_utilidades.getLastNode(pathLevelAutorizacon, "numeroAutorizacion", doc));
                comprobante.setFecha_autorizacion(fa);
                comprobante.setAmbiente(xml_utilidades.getLastNode(pathLevelAutorizacon, "ambiente", doc));
                
                String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + 
                        "\n" + 
                        xml_utilidades.getNodeXml("RespuestaAutorizacionComprobante", doc);
                comprobante.setXml(xml);
                FileUtil.writeSignedAuth(comprobante, xml.getBytes());
                
                em.getTransaction().begin();
                em.merge(comprobante);                
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //em.close();
            } 
            
            System.out.println(
                    "Estado: " + comprobante.getEstado() + "\n"
                    + "N° Auto: " + comprobante.getNumero_autorizacion() + "\n"
                    + "Fecha Auto: " + comprobante.getFecha_autorizacion() + "\n"
                    + "Ambiente: " + comprobante.getAmbiente()
            );
        } else if (estado.equals("NO AUTORIZADO")) {
            Mensaje m = new Mensaje();
            
            try {
                comprobante.setEstado("NoAutorizado"); 
                                
                comprobante.setFecha_autorizacion(xml_utilidades.getLastNode(pathLevelAutorizacon, "fechaAutorizacion", doc));
                comprobante.setAmbiente(xml_utilidades.getLastNode(pathLevelAutorizacon, "ambiente", doc));                
                
                m.setIdentificador(xml_utilidades.getLastNode(pathLevelMensajes, "identificador", doc));
                m.setMensaje(xml_utilidades.getLastNode(pathLevelMensajes, "mensaje", doc));
                m.setTipo(xml_utilidades.getLastNode(pathLevelMensajes, "tipo", doc));
                m.setComprobanteId(comprobante.getId());
                
                em.getTransaction().begin();
                em.merge(comprobante);   
                em.persist(m);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //em.close();
            } 
            
            //xml_utilidades.getLastNode(pathLevelAutorizacon, "estado", doc)
            System.out.println(
                    "Estado: " + comprobante.getEstado() + "\n"
                    + "Fecha Auto: " + comprobante.getFecha_autorizacion() + "\n"
                    + "Ambiente: " + comprobante.getAmbiente() + "\n"
                    + "Identificador: " + m.getIdentificador() + "\n"
                    + "Mensaje: " + m.getMensaje() + "\n"
                    + "Tipo: " + m.getTipo());
        }
    }

    public boolean getRequestSoap(String urlWebServices, String method, String host, String getEncodeXML, Proxy proxy, Comprobante comprobante) throws IOException {

        try {

            URL oURL = new URL(urlWebServices);

            HttpURLConnection con = (HttpURLConnection) oURL.openConnection(proxy);
            con.setDoOutput(true);

            con.setRequestMethod(method);

            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", "");
            con.setRequestProperty("Host", host);

            OutputStream reqStreamOut = con.getOutputStream();
            reqStreamOut.write(getEncodeXML.getBytes());            

            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream(), "UTF8"));

            String line = "";
            StringBuilder sb = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            System.out.println(sb.toString());
            Document doc = xml_utilidades.convertStringToDocument(sb.toString());

            this.getAutorizacion(doc, comprobante);

            con.disconnect();

            return true;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
}
