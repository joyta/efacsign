/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package efacsign.thread;

import efacsign.util.FileUtil;
import efacsign.util.EntityManagerUtil;
import efacsign.model.Comprobante;
import efacsign.sign.XadesSign;
import efacsign.sri.soap.recepcion.RespuestaSolicitud;
import efacsign.sri.soap.recepcion.SoapRecepcion;
import static efacsign.util.StringUtil.converBase64;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;

/**
 *
 * @author desarrollador
 */
public class ThreadRecepcion extends Thread {

    ResourceBundle resource = null;
    
    public ThreadRecepcion() {
        resource = ResourceBundle.getBundle("efac");
    }
    
    public String firmarDocumentoXmlXades(Comprobante c) throws FileNotFoundException, IOException{
        XadesSign x = new XadesSign();
        byte[] xmlSigned = x.firmarDocumentoXmlXades(c.getXml().getBytes());
        String xml64 = converBase64(xmlSigned);
        
        FileUtil.writeXml(c, c.getXml().getBytes());
        FileUtil.writeSignedXml(c, xmlSigned);
        
        return xml64;
    }

    @Override
    public void run() {
        System.out.println("Iniciando thead recepción....");
        
        while (true) {        
            EntityManager em = EntityManagerUtil.getEntityManager();
            List<efacsign.model.Comprobante> l = em.createQuery("from Comprobante where estado='Registrado' and tipo='01' and origen='Venta'").setMaxResults(1).getResultList();

            for (Comprobante c : l) {
                
                SoapRecepcion n = new SoapRecepcion();
                
                String url = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                String host = "celcer.sri.gob.ec";

                if(c.getAmbiente().equals("PRODUCCION")){                        
                    url = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                    host = "cel.sri.gob.ec";
                }
                
                try {
                    String xml64 = this.firmarDocumentoXmlXades(c);
                
                    RespuestaSolicitud r = n.sendPostSoap(
                            url, "POST", host,
                            n.formatSendPost(xml64),
                            Proxy.NO_PROXY, c, em
                    );  
                } catch (Exception e) {
                }
                                        
            }

            try {
                this.sleep(10000L);
            } catch (Exception e) {
            }

        }
    }

}
