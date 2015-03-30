/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign.util;

import efacsign.model.Comprobante;
import java.io.FileOutputStream;

/**
 *
 * @author desarrollador
 */
public class FileUtil {        
    
    public static void writeXml(Comprobante c, byte[] xmlSigned){
        try {
            String file = ResourceUtil.getString("efac.path.xml") + c.getId().toString() + ".xml";              
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(xmlSigned);
            fo.close(); 
        } catch (Exception e) {
        }
    }
    
    public static void writeSignedXml(Comprobante c, byte[] xmlSigned){
        try {
            String file = ResourceUtil.getString("efac.path.firmado") + c.getId().toString() + ".xml";              
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(xmlSigned);
            fo.close(); 
        } catch (Exception e) {
        }
    }
    
    public static void writeSignedAuth(Comprobante c, byte[] xmlAut){
        try {
            String file = ResourceUtil.getString("efac.path.autorizado") + c.getId().toString() + ".xml";              
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(xmlAut);
            fo.close(); 
        } catch (Exception e) {
        }
    }
    
}
