/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package efacsign.thread;

import efacsign.util.EntityManagerUtil;
import efacsign.model.Comprobante;
import efacsign.util.ResourceUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

/**
 *
 * @author desarrollador
 */
public class ThreadNotificacion extends Thread {    
    
    private Long tiempoEspera = 0L;
    
    public ThreadNotificacion() {  
        tiempoEspera = Long.parseLong(ResourceUtil.getString("efac.thread.tiempo.notificacion"));
    }
        

    @Override
    public void run() {
        System.out.println("Iniciando thead notificación....");        
        
        while (true) {        
            EntityManager em = EntityManagerUtil.getEntityManager();            
            
            List<efacsign.model.Comprobante> l = em.createNativeQuery("select * from tributario.comprobante where estado='Autorizado' and tipo in ('01','04') and origen='Venta' and (last_email is null or last_email <= now()) and send_email='No' ", Comprobante.class).setMaxResults(1).getResultList();

            for (Comprobante c : l) {
                em.refresh(c, LockModeType.READ);                                                
                
                System.out.println("-------------------------------------------------------------");
                System.out.println("Notificación comprobante, Tipo:" + c.getTipo() + ", N: " + c.getNumero() + ", Id: " + c.getId());
                
                try {
                    getRequestSoap(ResourceUtil.getString("efac.email.url")+c.getId().toString(), "GET", "localhost","",Proxy.NO_PROXY, c);
                } catch (Exception e) {
                }
            }

            try {
                this.sleep(tiempoEspera);
            } catch (Exception e) {
            }

        }
    }
    
    public boolean getRequestSoap(String urlWebServices, String method, String host, String getEncodeXML, Proxy proxy, Comprobante comprobante) throws IOException {

        try {

            URL oURL = new URL(urlWebServices);

            HttpURLConnection con = (HttpURLConnection) oURL.openConnection(proxy);
            con.setDoOutput(true);

            con.setRequestMethod(method);
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
            
            con.disconnect();

            return true;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

}
