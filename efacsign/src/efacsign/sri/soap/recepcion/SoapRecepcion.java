/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign.sri.soap.recepcion;

import efacsign.EntityManagerUtil;
import efacsign.model.Mensaje;
import efacsign.XML_Utilidades;
import efacsign.model.Comprobante;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import javax.persistence.EntityManager;
import org.w3c.dom.Document;

/**
 *
 * @author desarrollador
 */
public class SoapRecepcion {
    
    public static XML_Utilidades xml_utilidades = new XML_Utilidades();
    
    public RespuestaSolicitud sendPostSoap(String urlWebServices, String method, String host, String getEncodeXML, Proxy proxy, Comprobante comprobante, EntityManager em) {

        RespuestaSolicitud r = null;
                
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

            r = getEstadoPostSoap(xml_utilidades.convertStringToDocument(sb.toString()),
                    "RespuestaRecepcionComprobante",
                    "estado",
                    comprobante);//está extrae la data de los nodos en un archivo XML

            con.disconnect();

        } catch (Exception ex) {
            System.out.println("Error: "+ ex);
        }
        return r;
    }
    
    

    public String formatSendPost(String bytesEncodeBase64) {
        String xml = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ec='http://ec.gob.sri.ws.recepcion'>"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<ec:validarComprobante>"
                + "<xml>" + bytesEncodeBase64 + "</xml>"
                + "</ec:validarComprobante>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

        return xml;
    }
    
    

    public RespuestaSolicitud getEstadoPostSoap(Document doc, String nodoRaiz, String nodoElemento, Comprobante comprobante) {

        String estado = xml_utilidades.getNodes(nodoRaiz, nodoElemento, doc);
        System.out.println("Estado: "+estado);
        
        RespuestaSolicitud r = new RespuestaSolicitud(estado);        
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        if (estado.equals("DEVUELTA")) {
            //Comprobante c = new Comprobante(xml_utilidades.getNodes("comprobante", "claveAcceso", doc));
            Mensaje m = new Mensaje(
                    xml_utilidades.getNodes("mensaje", "identificador", doc),
                    xml_utilidades.getNodes("mensaje", "mensaje", doc),
                    xml_utilidades.getNodes("mensaje", "informacionAdicional", doc),
                    xml_utilidades.getNodes("mensaje", "tipo", doc)
            );
            
            m.setComprobanteId(comprobante.getId());                        
            
            try {
                comprobante.setEstado("Devuelto");                    
                em.getTransaction().begin();
                em.merge(comprobante);
                em.persist(m);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //em.close();
            } 
                        
            System.out.println("Clave de Accceso: " + comprobante.getClave_acceso());
            System.out.println("Identificador Error: " + m.getIdentificador());
            System.out.println("Descripción Error: " + m.getMensaje());
            System.out.println("Descripción Adicional Error: " + m.getInformacionAdicional());
            System.out.println("Tipo mensaje: " + m.getTipo());
            
        } else if (estado.equals("RECIBIDA")) {
            System.out.println("RECIBIDA");  
            
            try {
                comprobante.setEstado("Recibido");                    
                em.getTransaction().begin();
                em.merge(comprobante);                
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //em.close();
            }
        }    
        
        return r;
    }
}
