/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package efacsign;

import efacsign.sri.soap.recepcion.SoapRecepcion;
import efacsign.model.Comprobante;
import efacsign.sign.XadesSign;
import efacsign.sri.soap.recepcion.RespuestaSolicitud;
import java.net.Proxy;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author desarrollador
 */
public class NewClass {
    
    public static String converBase64(byte[] xml) {

        String str = new String(xml);

        try {
            String bytesEncoded = DatatypeConverter.printBase64Binary(str.getBytes("UTF-8"));
            return bytesEncoded;
        } catch (Exception e) {
            System.out.println("convert64: " + e);
            return null;
        }

    }

    public static void main(String[] args) {
        //RecepcionComprobantesService service1 = new RecepcionComprobantesService();        
        //RecepcionComprobantes port1 = service1.getRecepcionComprobantesPort();

         //AutorizacionComprobantesService service2 = new AutorizacionComprobantesService();
        //AutorizacionComprobantes port2 = service2.getAutorizacionComprobantesPort();
        //RespuestaComprobante respComprobante = port.autorizacionComprobante(claveAcceso);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("efacSignPU");
        EntityManager em = emf.createEntityManager();

        List<efacsign.model.Comprobante> l = em.createQuery("from Comprobante where estado='Registrado' and tipo='01' and origen='Venta'").getResultList();
        System.out.println(l);

        for (Comprobante c : l) {

            XadesSign x = new XadesSign();
            byte[] xmlSigned = x.firmarDocumentoXmlXades(c.getXml().getBytes());
            String xml64 = converBase64(xmlSigned);
            
            SoapRecepcion n = new SoapRecepcion();
            RespuestaSolicitud r = n.sendPostSoap(
                    "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl",
                    "POST", 
                    "celcer.sri.gob.ec",
                    n.formatSendPost(new String(xml64)),
                    Proxy.NO_PROXY,
                    c, em
            );
            
            System.out.println(r);

            //System.out.println(c.getClave_acceso());
            //RespuestaSolicitud r = port1.validarComprobante(xml64);            
            //System.out.println(r.getEstado());
            //System.out.println("Comprobantes: " + r.getComprobantes().getComprobante());
            //RespuestaComprobante rc = port2.autorizacionComprobante(c.getClave_acceso());
            //System.out.println(rc.getClaveAccesoConsultada());
            //System.out.println(rc.getAutorizaciones().getAutorizacion());
            
        }

        /*try {
         em.getTransaction().begin();
         em.persist(yo);
         em.getTransaction().commit();
         } catch (Exception e) {
 
         e.printStackTrace();
         }finally {
         em.close();*/
    }

    
}
