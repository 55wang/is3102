/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BatchProcess;

import entity.common.Transaction;
import entity.dams.account.DepositAccount;
import entity.dams.rules.ConditionInterest;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Date;
import javax.persistence.PersistenceContext;
import utils.DateUtils;
import utils.EnumUtils.InterestConditionType;
import utils.EnumUtils.TransactionType;

/**
 *
 * @author litong
 */
@Stateless
public class InterestAccrualSessionBean implements InterestAccrualSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Boolean isAccountMeetCondition(DepositAccount a, ConditionInterest i) {
        Date lastMonthFirstDay = DateUtils.getLastBeginOfMonth();
        Date lastMonthLastDay = DateUtils.getLastEndOfMonth();
        Date sinceDate = DateUtils.getBeginOfMonth();
        Date toDate = DateUtils.getEndOfMonth();
        BigDecimal bd1 = new BigDecimal(500);
        BigDecimal bd2 = new BigDecimal(500.0);
        BigDecimal bd3 = new BigDecimal(1000);
        
        System.out.println("bd1 compare to bd2: " + bd1.compareTo(bd2));
        System.out.println("bd1 compare to bd3: " + bd1.compareTo(bd3));
        System.out.println("bd3 compare to bd2: " + bd3.compareTo(bd2));
        System.out.println(lastMonthFirstDay);
        System.out.println(lastMonthLastDay);
        System.out.println(sinceDate);
        System.out.println(toDate);
        System.out.println(i.getConditionType());
        if (i.getConditionType() == InterestConditionType.BILL) {
            List<Transaction> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.BILL);
            System.out.println(ts);
            BigDecimal minimumNumberOfBills = i.getAmount();
            BigDecimal currentNumberOfBills = new BigDecimal(ts.size());
            System.out.println(minimumNumberOfBills.compareTo(currentNumberOfBills) <= 0);
            return minimumNumberOfBills.compareTo(currentNumberOfBills) <= 0;
        } else if (i.getConditionType() == InterestConditionType.CCSPENDING) {
            List<Transaction> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.CCSPENDING);
            System.out.println(ts);
            BigDecimal minimumAmountSpending = i.getAmount();
            BigDecimal currentAmountSpending = BigDecimal.ZERO;
            for (Transaction t : ts) {
                // payment
                if (!t.getCredit()) {
                    currentAmountSpending = currentAmountSpending.add(t.getAmount());
                }
            }
            System.out.println("minimumAmountSpending: " + minimumAmountSpending);
            System.out.println("currentAmountSpending " + currentAmountSpending);
            System.out.println(minimumAmountSpending.compareTo(currentAmountSpending) <= 0);
            return minimumAmountSpending.compareTo(currentAmountSpending) <= 0;
        } else if (i.getConditionType() == InterestConditionType.INCREASE) {
            System.out.println(a.getPreviousBalance().compareTo(a.getBalance()) <= 0);
            return a.getPreviousBalance().compareTo(a.getBalance()) <= 0;
        } else if (i.getConditionType() == InterestConditionType.SALARY) {
            List<Transaction> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.SALARY);
            System.out.println(ts);
            BigDecimal minimumSalaryCrediting = i.getAmount();
            BigDecimal currentSalaryCrediting = BigDecimal.ZERO;
            for (Transaction t : ts) {
                // payment
                if (t.getCredit()) {
                    currentSalaryCrediting = currentSalaryCrediting.add(t.getAmount());
                }
            }
            System.out.println(minimumSalaryCrediting.compareTo(currentSalaryCrediting) <= 0);
            return minimumSalaryCrediting.compareTo(currentSalaryCrediting) <= 0;
        } else if (i.getConditionType() == InterestConditionType.INVEST) {
            sinceDate = DateUtils.getLastNthBeginOfMonth(i.getBenefitMonths());
            System.out.println(sinceDate);
            List<Transaction> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.SALARY);
            System.out.println(ts);
            return !ts.isEmpty();
        } else {
            return false;
        }
    }
    
    private List<Transaction> retrieveTransactions(DepositAccount a, Date sinceDate, Date toDate, TransactionType type) {
        Query q = em.createQuery(
                "SELECT t FROM Transaction t WHERE "
                        + "t.fromAccount.id = :accountId AND "
                        + "t.actionType = :type AND "
                        + "t.creationDate BETWEEN :sinceDate AND :toDate"
        );
        q.setParameter("accountId", a.getId());
        q.setParameter("sinceDate", sinceDate);
        q.setParameter("toDate", toDate);
        q.setParameter("type", type);
        return q.getResultList();
    }
}
