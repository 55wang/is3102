/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardOrder;
import entity.card.account.PromoCode;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;
import server.utilities.EnumUtils.*;

/**
 *
 * @author wang
 */
@Stateless
public class CardAcctSessionBean implements CardAcctSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public List<CreditCardOrder> showAllCreditCardOrder() {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco");
        return q.getResultList();
    }
    
    @Override
    public CreditCardOrder getCardOrderFromId(Long orderNumber) {
        return em.find(CreditCardOrder.class, orderNumber);
    }
    
    @Override
    public List<CardTransaction> getCardTransactionFromId(Long ccaId) {
        System.out.println(ccaId);
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE ct.creditCardAccount.id =:inCcaId");
        q.setParameter("inCcaId", ccaId);
        List<CardTransaction> cts = q.getResultList();
        System.out.println(cts);
        return cts;
    }
    
    @Override
    public CardTransaction getSpecificCaedTransactionFromId(Long ccaId){
        return (CardTransaction) em.find(CardTransaction.class, ccaId);
    }
    
    //try not to use void, always return something or null. and catch it at the caller side.
    @Override
    public String createCardOrder(CreditCardOrder order) {
        try {
            em.persist(order);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.createCardOrder Error");
            System.out.println(e);
            return null;
        }
    }

    //update cardorder application status which can be cancel or approve and etc.
    @Override
    public String updateCardOrderStatus(CreditCardOrder order, ApplicationStatus status) {
        try {
            order.setApplicationStatus(status);
            em.merge(order);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.updateCardOrderStatus Error");
            System.out.println(e);
            return null;
        }
    }
    
    @Override
    public List<CreditCardAccount> showAllCreditCardAccount(CardAccountStatus status, Long id) {
        System.out.println("Status:" + status + " and id:" + id);
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus <> :inStatus AND cca.mainAccount.id =:id");
        q.setParameter("inStatus", status);
        q.setParameter("id", id);
        return q.getResultList();
    }
    
    @Override
    public CreditCardAccount getCardAccountFromId(Long cardID) {
        return em.find(CreditCardAccount.class, cardID);
    }
    
    @Override
    public CreditCardAccount getCardByCardNumber(String cardNumber) {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.creditCardNum =:cardNumber");
        q.setParameter("cardNumber", cardNumber);
        List<CreditCardAccount> ccas = q.getResultList();
        if (ccas != null && ccas.isEmpty() && ccas.size() == 1) {
            return ccas.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public CreditCardAccount validateCreditCardDailyTransactionLimit(CreditCardAccount creditCard, Double requestAmount) {
        List<CardTransaction> dailyTransactions = getDailyTransactionFromAccount(creditCard);
        
        Double dailyAmount = 0.0;
        for (CardTransaction ct : dailyTransactions) {
            dailyAmount += ct.getAmount();
        }
        
        if (dailyAmount + requestAmount > creditCard.getTransactionDailyLimit()) {
            return null;
        }
        
        return creditCard;
    }
    
    @Override
    public CreditCardAccount validateCreditCardMonthlyTransactionLimit(CreditCardAccount creditCard, Double requestAmount) {
        List<CardTransaction> monthlyTransactions = getMonthlyTransactionFromAccount(creditCard);
        
        Double monthlyAmount = 0.0;
        for (CardTransaction ct : monthlyTransactions) {
            monthlyAmount += ct.getAmount();
        }
        
        if (monthlyAmount + requestAmount > creditCard.getTransactionMonthlyLimit()) {
            return null;
        }
        
        return creditCard;
    }
    
    @Override
    public List<CardTransaction> getDailyTransactionFromAccount(CreditCardAccount creditCard) {
        
        Date now = new Date();
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "datediff(day, ct.updateDate, :now) = 0 AND"
                + "ct.creditCardAccount.id =: ccId"
        );
        q.setParameter("now", now);
        q.setParameter("ccId", creditCard.getId());
        
        return q.getResultList();
    }
    
    @Override
    public List<CardTransaction> getMonthlyTransactionFromAccount(CreditCardAccount creditCard) {
        Date startDate = DateUtils.getBeginOfMonth();
        Date endDate = DateUtils.getBeginOfMonth();
        
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "ct.updateTime BETWEEN :startDate AND :endDate AND"
                + "ct.creditCardAccount.id =: ccId"// TODO: Add status
        );
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("ccId", creditCard.getId());
        return q.getResultList();
    }
    
    @Override
    public String createCardAccount(CreditCardAccount cca) {
        try {
            em.persist(cca);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.createCardAccount Error");
            System.out.println(e);
            return null;
        }
    }

    //update cardaccount status 
    @Override
    public String updateCardAccountStatus(CreditCardAccount cca, CardAccountStatus status) {
        try {
            cca.setCardStatus(status);
            em.merge(cca);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.updateCardAccountStatus Error");
            System.out.println(e);
            return null;
        }
    }
    
    @Override
    public String updateCardAcctTransactionDailyLimit(CreditCardAccount cca, double newDailyLimit) {
        try {
            cca.setTransactionDailyLimit(newDailyLimit);
            em.merge(cca);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.updateCardAcctTransactionDailyLimit Error");
            System.out.println(e);
            return null;
        }
    }
    
    @Override
    public String updateCardAcctTransactionMonthlyLimit(CreditCardAccount cca, double newMonthlyLimit) {
        try {
            cca.setTransactionMonthlyLimit(newMonthlyLimit);
            em.merge(cca);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.updateCardAcctTransactionMonthlyLimit Error");
            System.out.println(e);
            return null;
        }
    }

    // redeem credit card reward generate code
    public String redeemPromoCode(CreditCardAccount cca, String PromoName) {
        String code = null;
        do {
            try {
                code = server.utilities.CommonHelper.generateRandom(true, 12);
                Query q = em.createQuery("SELECT pc FROM PromoCode pc WHERE pc.promotionCode =:inCode");
                q.setParameter("inCode", code);
                Object result = q.getSingleResult();
                if (result != null) {
                    code = null;
                }
            } catch (NullPointerException npe) {
            }
        } while (code != null); //generate a code that is not found in database

        try {
            PromoCode pc = new PromoCode();
            pc.setPromotionName(PromoName);
            pc.setPromotionCode(code);
            pc.setCreditCardAccount(cca);
            cca.getPromoCode().add(pc);
            em.merge(cca);
            return code;
        } catch (Exception e) {
            return null;
        }
    }
    
}
