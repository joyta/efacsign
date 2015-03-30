/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign.util;

import java.util.ResourceBundle;

/**
 *
 * @author desarrollador
 */
public class ResourceUtil {
    private static ResourceBundle resource = ResourceBundle.getBundle("efac");
    
    public static String getString(String key){
        return  resource.getString(key);
    }
}
