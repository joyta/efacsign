/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author desarrollador
 */
public class EntityManagerUtil {
    
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("efacSignPU");
    private static EntityManager em;
    
    
    public static EntityManager getEntityManager(){        
        if(em == null){
            em = emf.createEntityManager();        
        }
        return em;
    }
}
