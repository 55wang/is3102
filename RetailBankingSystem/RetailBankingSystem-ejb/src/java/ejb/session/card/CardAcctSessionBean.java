/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardOrder;
import entity.card.account.DebitCardAccount;
import entity.card.account.PromoCode;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.ApplicationStatus;
import server.utilities.EnumUtils.CardAccountStatus;
import server.utilities.GenerateAccountAndCCNumber;
import server.utilities.PincodeGenerationUtils;

/**
 *
 * @author wang
 */
@Stateless
public class CardAcctSessionBean implements CardAcctSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public String updateCreditCardOrder(CreditCardOrder cco) {
        try {
            em.merge(cco);
            return "SUCCESS";
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.updateCreditScore Error");
            System.out.println(e);
            return null;
        }
    }
    
    @Override
    public CreditCardAccount updateCreditCardAccount(CreditCardAccount cca) {
        em.merge(cca);
        return cca;
    }
    
    @Override
    public List<CreditCardOrder> showAllCreditCardOrder() {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco");
        return q.getResultList();
    }
    
    @Override
    public List<CreditCardAccount> showAllPendingCreditCardOrder() {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus = :status");
        q.setParameter("status", CardAccountStatus.PENDING);
        return q.getResultList();
    }

    @Override
    public List<CreditCardOrder> showAllCreditCardOrder(EnumUtils.ApplicationStatus applicationStatus) {
        
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco WHERE coo.applicationStatus = :applicationStatus");
        q.setParameter("applicationStatus", applicationStatus);
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
    public CardTransaction getSpecificCaedTransactionFromId(Long ccaId) {
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

    @Override
    public CardTransaction createCardAccountTransaction(String ccNumber, CardTransaction ct) {
        CreditCardAccount ca = getCardByCardNumber(ccNumber);
        if (ca != null) {
            System.out.println("Adding Transaction for card:" + ccNumber);
            ct.setCreditCardAccount(ca);
            ca.addOutstandingAmount(ct.getAmount());
            em.persist(ct);
            em.merge(ca);
            return ct;
        } else {
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
    public List<DebitCardAccount> showAllDebitCardAccount(CardAccountStatus status, Long id) {
        System.out.println("Status:" + status + " and id:" + id);
        Query q = em.createQuery("SELECT cca FROM DebitCardAccount cca WHERE cca.CardStatus <> :inStatus AND cca.customerDepositAccount.mainAccount.id =:id");
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
        System.out.println("EJB getCardByCardNumber " + cardNumber);
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.creditCardNum = :cardNumber");
        q.setParameter("cardNumber", cardNumber);
        return (CreditCardAccount) q.getSingleResult();
    }

    @Override
    public CreditCardAccount validateCreditCardDailyTransactionLimit(CreditCardAccount creditCard, double requestAmount) {
        List<CardTransaction> dailyTransactions = getDailyTransactionFromAccount(creditCard);
        System.out.println(dailyTransactions);
        double dailyAmount = 0.0;
        for (CardTransaction ct : dailyTransactions) {
            dailyAmount += ct.getAmount();
        }

        System.out.println("Daily amount is: " + dailyAmount);
        System.out.println("Request amount is: " + requestAmount);
        System.out.println("Daily Limit is: " + creditCard.getTransactionDailyLimit());
        if ((dailyAmount + requestAmount) > creditCard.getTransactionDailyLimit()) {
            return null;
        }

        return creditCard;
    }

    @Override
    public CreditCardAccount validateCreditCardMonthlyTransactionLimit(CreditCardAccount creditCard, double requestAmount) {
        List<CardTransaction> monthlyTransactions = getMonthlyTransactionFromAccount(creditCard);

        double monthlyAmount = 0.0;
        for (CardTransaction ct : monthlyTransactions) {
            monthlyAmount += ct.getAmount();
        }

        System.out.println("Monthly amount is: " + monthlyAmount);
        System.out.println("Request amount is: " + requestAmount);
        System.out.println("Monthly limit is: " + creditCard.getTransactionMonthlyLimit());
        if ((monthlyAmount + requestAmount) > creditCard.getTransactionMonthlyLimit()) {
            return null;
        }

        return creditCard;
    }

    @Override
    public List<CardTransaction> getDailyTransactionFromAccount(CreditCardAccount creditCard) {
        java.sql.Date startDate = new java.sql.Date(DateUtils.getBeginOfDay().getTime());
        java.sql.Date endDate = new java.sql.Date(DateUtils.getEndOfDay().getTime());
        System.out.println("Getting Daily Transaction");
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "ct.creditCardAccount.id =:ccId AND "
                + "ct.updateDate BETWEEN :startDate AND :endDate"
        );
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("ccId", creditCard.getId());

        try {
            List<CardTransaction> result = q.getResultList();
            if (result == null) {
                return new ArrayList<>();
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<CardTransaction> getMonthlyTransactionFromAccount(CreditCardAccount creditCard) {

        java.sql.Date startDate = new java.sql.Date(DateUtils.getBeginOfDay().getTime());
        java.sql.Date endDate = new java.sql.Date(DateUtils.getEndOfDay().getTime());
        System.out.println("Getting Monthly Transaction");
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "ct.creditCardAccount.id =:ccId AND "
                + "ct.updateDate BETWEEN :startDate AND :endDate"
        // TODO: Add status
        );
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("ccId", creditCard.getId());

        try {
            List<CardTransaction> result = q.getResultList();
            if (result == null) {
                return new ArrayList<>();
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public CreditCardAccount createCardAccount(CreditCardAccount cca) {
        try {
            if (cca.getCreditCardNum() == null || cca.getCreditCardNum().isEmpty()) {
                cca.setCreditCardNum(generateMasterCardNumber());
                cca.setCvv(Integer.parseInt(generateCVVNumber()));
            }
            System.out.println("Saving cca:" + cca.getCreditCardNum());
            System.out.println("cca:" + cca.getPartialHiddenAccountNumber());
            em.persist(cca);
            return cca;
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.createCardAccount Error");
            System.out.println(e);
            return null;
        }
    }
    
    private String generateMasterCardNumber() {
        String ccNumber = "";
        for(;;) {
             ccNumber = GenerateAccountAndCCNumber.generateMasterCardNumber();
             CreditCardAccount a = null;
             try {
                 a = getCardByCardNumber(ccNumber);
             } catch (Exception e) {
                 System.out.println("No cc number found in database");
             }
             if (a == null) {
                 return ccNumber;
             }
        }
    }
    
    private String generateCVVNumber() {
        return PincodeGenerationUtils.generateRandom(true, 3);
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
    public String updateDebitAccountStatus(DebitCardAccount dca, CardAccountStatus status) {
        try {
            dca.setCardStatus(status);
            em.merge(dca);
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
    public String updateCardAcctTransactionLimit(CreditCardAccount cca) {
        try {
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
    

    @Override
    public DebitCardAccount createDebitAccount(CustomerDepositAccount da) {
        try {
//            DepositAccount da = em.find(DepositAccount.class, depositAccountId);
            DebitCardAccount dba = new DebitCardAccount();
            dba.setCreditCardNum(generateAccountNumber());
            dba.setCvv(Integer.parseInt(server.utilities.CommonHelper.generateRandom(true, 3)));
            dba.setCardStatus(EnumUtils.CardAccountStatus.PENDING);
            Calendar cal = Calendar.getInstance();
            dba.setCreationDate(cal.getTime());
            cal.set(Calendar.YEAR, 2);
            dba.setValidDate(cal.getTime());
            dba.setCustomerDepositAccount((CustomerDepositAccount) da);
            dba.setNameOnCard(da.getMainAccount().getCustomer().getFullName());
            em.persist(dba);

            return dba;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private String generateAccountNumber() {
        String accountNumber = "";
        for (;;) {
            accountNumber = GenerateAccountAndCCNumber.generateMasterCardNumber();
            DepositAccount a = em.find(DepositAccount.class, accountNumber);
            if (a == null) {
                return accountNumber;
            }
        }
    }
    
}
