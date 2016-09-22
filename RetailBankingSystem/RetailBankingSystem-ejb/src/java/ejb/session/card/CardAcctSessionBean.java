/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardOrder;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;

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
    public String updateCardOrderStatus(CreditCardOrder order, EnumUtils.ApplicationStatus status) {
        try {
            order.setApplicationStatus(status);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.updateCardOrderStatus Error");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<CreditCardAccount> showAllCreditCardAccount() {
        Query q = em.createQuery("SELECT cco FROM CreditCardAccount cca");
        return q.getResultList();
    }

    @Override
    public CreditCardAccount getCardAccountFromId(Long cardID) {
        return em.find(CreditCardAccount.class, cardID);
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
    public String updateCardAccountStatus(CreditCardAccount cca, EnumUtils.CardAccountStatus status) {
        try {
            cca.setCardStatus(status);
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
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.updateCardAcctTransactionMonthlyLimit Error");
            System.out.println(e);
            return null;
        }
    }
    
    

}
