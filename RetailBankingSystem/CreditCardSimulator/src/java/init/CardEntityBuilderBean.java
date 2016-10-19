/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Singleton
@Startup
public class CardEntityBuilderBean {

    @PersistenceContext(unitName = "CreditCardSimulatorPU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        if (needInit()) {
            buildEntities();
        }
    }

    // Use Super Admin Account as a flag
    private Boolean needInit() {
        

        Long result;
        try {
            Query q = em.createNativeQuery("SELECT count(*) FROM VisaCardTransaction");
            result = (Long) q.getSingleResult();
        } catch (Exception ex) {
            result = 0L;
        }

        return result == 0L;
    }

    private void buildEntities() {
        System.out.println("build card entity - to be continued");
    }

}
