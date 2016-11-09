/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.common.TransactionRecord;
import entity.customer.Customer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Stateless
public class RFMSessionBean implements RFMSessionBeanLocal {
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long getDepositRecencyByCustomerId(String Id) {
        System.out.println("--------getDepositRecencyByCustomerId("+Id+")--------");
        Date currentDate = new Date();
        Query q = em.createQuery("SELECT tr FROM TransactionRecord tr, Customer c, MainAccount m, DepositAccount da WHERE tr.fromAccount = da AND da.mainAccount = m AND m.customer = c AND c.id =:id ORDER BY tr.creationDate DESC");
        q.setParameter("id", Id);
        List<TransactionRecord> trs = q.getResultList(); 
//        for(int i = 0; i < trs.size(); i++){
//            System.out.println("Transaction Record #"+i+": "+trs.get(i).getCreationDate());
//        }
        Long score = 5L;
        try{
            int monthDiff = DateUtils.monthDifference(currentDate, trs.get(0).getCreationDate());
            System.out.println("************Month Difference: "+monthDiff+"************");
            Long monthDifference = Long.valueOf(monthDiff);
            if(monthDifference >= 4L)
                return 1L;
            else return score-monthDifference;
        }catch(Exception ex){
                return 1L;
        }
    }

    @Override
    public Long getDepositFrequencyByCustomerId(String Id) {
        System.out.println("--------getDepositFrequencyByCustomerId("+Id+")--------");
        Date currentDate = new Date();
        Date twelveMonthAgo = DateUtils.getLastNthBeginOfMonth(12);
        Query q = em.createQuery("SELECT Count(tr.referenceNumber) FROM TransactionRecord tr, Customer c, MainAccount m, DepositAccount da WHERE tr.fromAccount = da AND da.mainAccount = m AND m.customer = c AND c.id =:id AND tr.creationDate BETWEEN :twelveMonthAgo AND :currentDate");
        q.setParameter("id", Id);
        q.setParameter("twelveMonthAgo", twelveMonthAgo);
        q.setParameter("currentDate", currentDate);
        Long frequency = (Long)q.getSingleResult();
         System.out.println("************Deposit frequency: "+frequency+"************");
        if(frequency<3L)
            return 1L;
        else if(frequency<5L)
            return 2L;
        else if(frequency<8L)
            return 3L;
        else if(frequency<10L)
            return 4L;
        else
            return 5L;
    }

    @Override
    public Long getDepositMonetaryByCustomerId(String Id) {
        System.out.println("--------getDepositMonetaryByCustomerId("+Id+")--------");
        Date currentDate = new Date();
        Date twelveMonthAgo = DateUtils.getLastNthBeginOfMonth(12);
        Query q = em.createQuery("SELECT tr FROM TransactionRecord tr, Customer c, MainAccount m, DepositAccount da WHERE tr.fromAccount = da AND da.mainAccount = m AND m.customer = c AND c.id =:id AND tr.creationDate BETWEEN :twelveMonthAgo AND :currentDate ORDER BY tr.amount DESC");
        q.setParameter("id", Id);
        q.setParameter("twelveMonthAgo", twelveMonthAgo);
        q.setParameter("currentDate", currentDate);
        List<TransactionRecord> trs = q.getResultList(); 
        try{
            BigDecimal largestAmount = trs.get(0).getAmount();
            Double largest = largestAmount.doubleValue();
            System.out.println("************Largest amount: "+largestAmount+"************");
            if(largest<3000.0)
                return 1L;
            else if(largest<5000.0)
                return 2L;
            else if(largest<8000.0)
                return 3L;
            else if(largest<10000.0)
                return 4L;
            else
                return 5L;
        }catch(Exception ex){
                return 1L;
        }
//        Monetary = value of the highest order from a given customer (benchmarked against $10k)
        //from past 12 months
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
    }

    @Override
    public Long getCardRecencyByCustomerId(String Id) {
        System.out.println("--------getCardRecencyByCustomerId("+Id+")--------");
        Date currentDate = new Date();
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct, Customer c, MainAccount m, CreditCardAccount cca WHERE ct.creditCardAccount = cca AND cca.mainAccount = m AND m.customer = c AND c.id =:id ORDER BY ct.createDate DESC");
        q.setParameter("id", Id);
        List<CardTransaction> cts = q.getResultList(); 
//        for(int i = 0; i < trs.size(); i++){
//            System.out.println("Transaction Record #"+i+": "+trs.get(i).getCreationDate());
//        }
        Long score = 5L;
        try{
            int monthDiff = DateUtils.monthDifference(currentDate, cts.get(0).getCreateDate());
            System.out.println("************Month Difference: "+monthDiff+"************");
            Long monthDifference = Long.valueOf(monthDiff);
            if(monthDifference >= 4L)
                return 1L;
            else return score-monthDifference;
        }catch(Exception ex){
                return 1L;
        }
    }

    @Override
    public Long getCardFrequencyByCustomerId(String Id) {
        System.out.println("--------getCardFrequencyByCustomerId("+Id+")--------");
        Date currentDate = new Date();
        Date twelveMonthAgo = DateUtils.getLastNthBeginOfMonth(12);
        Query q = em.createQuery("SELECT Count(ct.id) FROM CardTransaction ct, Customer c, MainAccount m, CreditCardAccount cca WHERE ct.creditCardAccount = cca AND cca.mainAccount = m AND m.customer = c AND c.id =:id AND ct.createDate BETWEEN :twelveMonthAgo AND :currentDate");
        q.setParameter("id", Id);
        q.setParameter("twelveMonthAgo", twelveMonthAgo);
        q.setParameter("currentDate", currentDate);
        Long frequency = (Long)q.getSingleResult();
         System.out.println("************Card frequency: "+frequency+"************");
        if(frequency<3L)
            return 1L;
        else if(frequency<5L)
            return 2L;
        else if(frequency<8L)
            return 3L;
        else if(frequency<10L)
            return 4L;
        else
            return 5L;
//        Frequency = number of purchases in the last 12 months (maximum of 10)
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
    }

    @Override
    public Long getCardMonetaryByCustomerId(String Id) {
        System.out.println("--------getCardMonetaryByCustomerId("+Id+")--------");
        Date currentDate = new Date();
        Date twelveMonthAgo = DateUtils.getLastNthBeginOfMonth(12);
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct, Customer c, MainAccount m, CreditCardAccount cca WHERE ct.creditCardAccount = cca AND cca.mainAccount = m AND m.customer = c AND c.id =:id AND ct.createDate BETWEEN :twelveMonthAgo AND :currentDate ORDER BY ct.amount DESC");
        q.setParameter("id", Id);
        q.setParameter("twelveMonthAgo", twelveMonthAgo);
        q.setParameter("currentDate", currentDate);
        List<CardTransaction> cts = q.getResultList(); 
        try{
            Double largest = cts.get(0).getAmount();
            System.out.println("************Largest amount: "+largest+"************");
            if(largest<3000.0)
                return 1L;
            else if(largest<5000.0)
                return 2L;
            else if(largest<8000.0)
                return 3L;
            else if(largest<10000.0)
                return 4L;
            else
                return 5L;
        }catch(Exception ex){
                return 1L;
        }
//        Monetary = value of the highest order from a given customer (benchmarked against $10k)
        //to be continued
//        Query q = em.createNativeQuery("");
//        Long numOfMonth = score - (Long) q.getSingleResult();
//        return numOfMonth;
    }
}
