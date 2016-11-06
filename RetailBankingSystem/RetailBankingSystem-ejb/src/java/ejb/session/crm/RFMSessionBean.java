/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wang
 */
@Stateless
public class RFMSessionBean implements RFMSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long getDepositRecencyByCustomerId(Long Id) {
        Long score = 10L;
        //from past 12 months
//        Recency = 10 - the number of months that have passed since the customer last purchased
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
        return 10L;
    }

    @Override
    public Long getDepositFrequencyByCustomerId(Long Id) {
        Long score = 10L;
        //from past 12 months
//        Frequency = number of purchases in the last 12 months (maximum of 10)
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
        return 10L;
    }

    @Override
    public Long getDepositMonetaryByCustomerId(Long Id) {
        Long score = 10L;
//        Monetary = value of the highest order from a given customer (benchmarked against $10k)
        //from past 12 months
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
        return 10L;
    }

    @Override
    public Long getCardRecencyByCustomerId(Long Id) {
        Long score = 10L;
//        Recency = 10 - the number of months that have passed since the customer last purchased
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
        return 10L;
    }

    @Override
    public Long getCardFrequencyByCustomerId(Long Id) {
        Long score = 10L;
//        Frequency = number of purchases in the last 12 months (maximum of 10)
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
        return 10L;
    }

    @Override
    public Long getCardMonetaryByCustomerId(Long Id) {
        Long score = 10L;
//        Monetary = value of the highest order from a given customer (benchmarked against $10k)
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
        return 10L;
    }
}
