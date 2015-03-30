/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package efacsign;

import efacsign.model.Comprobante;
import efacsign.sri.soap.SoapAutorizacion;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author desarrollador
 */
public class ThreadAutorizacion extends Thread {

    public ThreadAutorizacion() {
        
    }

    @Override
    public void run() {
        System.out.println("Iniciando thead autorización....");
        
        while (true) {            
            EntityManager em = EntityManagerUtil.getEntityManager();
            List<efacsign.model.Comprobante> l = em.createQuery("from Comprobante where estado='Recibido' and tipo='01' and origen='Venta'").setMaxResults(1).getResultList();

            for (Comprobante c : l) {                                
                SoapAutorizacion n = new SoapAutorizacion();
                try {
                    String url = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";
                    String host = "celcer.sri.gob.ec";
                    
                    if(c.getAmbiente().equals("PRODUCCION")){                        
                        url = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";
                        host = "cel.sri.gob.ec";
                    }
                    
                    n.getRequestSoap(url, "POST", host,
                        n.formatSendPost(c.getClave_acceso()),
                        Proxy.NO_PROXY, c);
                } catch (IOException e) {
                }
                                      
            }

            try {
                this.sleep(10000L);
            } catch (Exception e) {
            }

        }
    }

}
