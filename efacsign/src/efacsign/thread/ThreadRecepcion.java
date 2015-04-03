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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        Calendar calendar = GregorianCalendar.getInstance();
        
        while (true) {        
            EntityManager em = EntityManagerUtil.getEntityManager();
            List<efacsign.model.Comprobante> l = em.createNativeQuery("select * from tributario.comprobante where estado='Registrado' and tipo in ('01','04') and origen='Venta' and (last_send is null or last_send <= now())", Comprobante.class).setMaxResults(1).getResultList();

            for (Comprobante c : l) {
                
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, 5);                
                c.setLast_send(calendar.getTime());                                
                
                System.out.println("Recepcion comprobante, Tipo:" + c.getTipo() + ", N: " + c.getNumero());
                
                SoapRecepcion n = new SoapRecepcion();
                
                String url = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                String host = "celcer.sri.gob.ec";

                if(c.getAmbiente().equals("PRODUCCION")){                        
                    url = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                    host = "cel.sri.gob.ec";
                }
                
                try {
                    String xml64 = this.firmarDocumentoXmlXades(c);                    
                    n.sendPostSoap(
                            url, "POST", host,
                            n.formatSendPost(xml64),
                            Proxy.NO_PROXY, c, em
                    );  
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                
                
                                        
            }

            try {
                this.sleep(10000L);
            } catch (Exception e) {
            }

        }
    }

}
