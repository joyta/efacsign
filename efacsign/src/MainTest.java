/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import efacsign.thread.ThreadRecepcion;
import efacsign.thread.ThreadAutorizacion;
import efacsign.thread.ThreadNotificacion;

/**
 *
 * @author desarrollador
 */
public class MainTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ThreadRecepcion recThread = new ThreadRecepcion();                       
        recThread.start();
        
        ThreadAutorizacion autThread = new ThreadAutorizacion();                       
        autThread.start();
        
        ThreadNotificacion notThread = new ThreadNotificacion();                       
        notThread.start();
        
    }

}
