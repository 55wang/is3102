/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.card.CardTransactionSessionBeanLocal;
import entity.VisaSettlement;
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
    @EJB
    private CardTransactionSessionBeanLocal cardTransactionSessionBean;

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
        VisaSettlement citi = new VisaSettlement();
        citi.setFromBankCode("001");
        citi.setToBankCode("005");
        citi.setAmount(0.0);
        citi.setFromBankName("Merlion Bank Singapore");
        citi.setToBankName("CITIBANK NA");
        cardTransactionSessionBean.persistSettlement(citi);

        VisaSettlement ocbc = new VisaSettlement();
        ocbc.setFromBankCode("001");
        ocbc.setToBankCode("013");
        ocbc.setAmount(0.0);
        ocbc.setFromBankName("Merlion Bank Singapore");
        ocbc.setToBankName("OVERSEA-CHINESE BANKING CORPN LTD");
        cardTransactionSessionBean.persistSettlement(ocbc);
    }

}
