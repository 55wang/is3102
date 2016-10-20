/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;
import server.utilities.EnumUtils.CardTransactionStatus;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CardTransactionSessionBean implements CardTransactionSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public CardTransaction updateCardTransaction(CardTransaction ct) {
        em.merge(ct);
        return ct;
    }
    
    @Override
    public Boolean createCardTransaction(CardTransaction ct) {
        try {
            em.merge(ct);
            em.flush();
            return true;
        } catch (Exception ex) {
            System.out.println("CardTransactionSessionBean.createCardTransaction: " + ex.toString());
            return false;
        }
    }

    @Override
    public List<CardTransaction> getListCardTransactionsByStatus(CardTransactionStatus status) {
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE ct.cardTransactionStatus=:inStatus");
        q.setParameter("inStatus", status);
        List<CardTransaction> cts = q.getResultList();
        System.out.println(cts);
        return cts;
    }

    @Override
    public CardTransaction getCardTransactionByVisaId(String visaId) {
        System.out.println(visaId);
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE ct.visaId =:inVisaId");
        q.setParameter("inVisaId", visaId);
        return (CardTransaction) q.getSingleResult();
    }

    @Override
    public List<CardTransaction> getListCardTransactionsByCcaId(Long ccaId) {
        System.out.println(ccaId);
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE ct.creditCardAccount.id =:inCcaId");
        q.setParameter("inCcaId", ccaId);
        List<CardTransaction> cts = q.getResultList();
        System.out.println(cts);
        return cts;
    }

    @Override
    public CardTransaction getCardTransactionByCcaId(Long ccaId) {
        return (CardTransaction) em.find(CardTransaction.class, ccaId);
    }

    @Override
    public CardTransaction createCardAccountTransaction(CreditCardAccount cca, CardTransaction ct) {
//        CreditCardAccount ca = getCreditCardAccountByCardNumber(ccNumber);
        if (cca != null) {
            ct.setCreditCardAccount(cca);
            em.persist(ct);
            em.merge(cca);
            return ct;
        } else {
            return null;
        }
    }

    @Override
    public List<CardTransaction> getTransactionByStartDateAndEndDate(Date startDate, Date endDate) {

        System.out.println("SELECT ct FROM CardTransaction ct WHERE ct.updateDate BETWEEN " + startDate + " AND " + endDate + "");
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "ct.updateDate BETWEEN :startDate AND :endDate"
        );
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        return q.getResultList();
    }

    @Override
    public List<CardTransaction> getListDailyTransactionsByCreditCardAccount(CreditCardAccount cca) {
        Date startDate = DateUtils.getBeginOfDay();
        Date endDate = DateUtils.getEndOfDay();
        System.out.println("Getting Daily Transaction");
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "ct.creditCardAccount.id =:ccId AND "
                + "ct.updateDate BETWEEN :startDate AND :endDate"
        );
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("ccId", cca.getId());

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
    public List<CardTransaction> getListMonthlyTransactionsByCreditCardAccount(CreditCardAccount cca) {

        Date startDate = DateUtils.getBeginOfDay();
        Date endDate = DateUtils.getEndOfDay();
        System.out.println("Getting Monthly Transaction");
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "ct.creditCardAccount.id =:ccId AND "
                + "ct.updateDate BETWEEN :startDate AND :endDate"
        // TODO: Add status
        );
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("ccId", cca.getId());

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
    public boolean validateCreditCardDailyTransactionLimit(CreditCardAccount creditCard, double requestAmount) {
        List<CardTransaction> dailyTransactions = getListDailyTransactionsByCreditCardAccount(creditCard);
        System.out.println(dailyTransactions);
        double dailyAmount = 0.0;
        for (CardTransaction ct : dailyTransactions) {
            dailyAmount += ct.getAmount();
        }

        System.out.println("Daily amount is: " + dailyAmount);
        System.out.println("Request amount is: " + requestAmount);
        System.out.println("Daily Limit is: " + creditCard.getTransactionDailyLimit());
        if ((dailyAmount + requestAmount) > creditCard.getTransactionDailyLimit()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validateCreditCardMonthlyTransactionLimit(CreditCardAccount creditCard, double requestAmount) {
        List<CardTransaction> monthlyTransactions = getListMonthlyTransactionsByCreditCardAccount(creditCard);

        double monthlyAmount = 0.0;
        for (CardTransaction ct : monthlyTransactions) {
            monthlyAmount += ct.getAmount();
        }

        System.out.println("Monthly amount is: " + monthlyAmount);
        System.out.println("Request amount is: " + requestAmount);
        System.out.println("Monthly limit is: " + creditCard.getTransactionMonthlyLimit());
        if ((monthlyAmount + requestAmount) > creditCard.getTransactionMonthlyLimit()) {
            return false;
        }
        return true;
    }
}
