/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign.util;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author desarrollador
 */
public class StringUtil {
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
}
