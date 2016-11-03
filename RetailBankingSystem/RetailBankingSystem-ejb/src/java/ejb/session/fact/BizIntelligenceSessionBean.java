/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class BizIntelligenceSessionBean implements BizIntelligenceSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<BigDecimal> getBankTotalDepositAmount() {
        //to be continued
        Query q = em.createNativeQuery("SELECT sum(balance) from depositaccount");
        return q.getResultList();
    }
}
