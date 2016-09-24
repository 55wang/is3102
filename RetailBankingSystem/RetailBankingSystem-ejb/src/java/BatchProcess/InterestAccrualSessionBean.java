/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BatchProcess;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import entity.common.TransactionRecord;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.DepositAccountProduct;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Date;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils.InterestConditionType;
import server.utilities.EnumUtils.TransactionType;

/**
 *
 * @author litong
 */
@Stateless
public class InterestAccrualSessionBean implements InterestAccrualSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private InterestSessionBeanLocal interestBean;

    @Override
    public List<DepositAccount> calculateMonthlyInterestsForDepositAccount(List<DepositAccount> accounts) {
        List<DepositAccount> result = new ArrayList<>();

        for (DepositAccount a : accounts) {
            result.add(depositBean.creditInterestAccount(a));
        }

        return result;
    }

    @Override
    public List<DepositAccount> calculateDailyInterestsForDepositAccount(List<DepositAccount> accounts) {
        List<DepositAccount> result = new ArrayList<>();

        for (DepositAccount a : accounts) {
            if (a instanceof CustomerDepositAccount) {
                result.add(calculateDailyInterestForCustomerDepositAccount((CustomerDepositAccount) a));
            } else { // fixed account
                result.add(calculateDailyInterestForCustomerFixedDepositAccount((CustomerFixedDepositAccount) a));
            }
        }

        return result;
    }

    @Override
    public DepositAccount calculateMonthlyInterestForDepositAccount(DepositAccount a) {
        return depositBean.creditInterestAccount(a);
    }

    @Override
    public DepositAccount calculateDailyInterestForDepositAccount(DepositAccount a) {
        if (a instanceof CustomerDepositAccount) {
            return calculateDailyInterestForCustomerDepositAccount((CustomerDepositAccount) a);
        } else { // fixed account
            return calculateDailyInterestForCustomerFixedDepositAccount((CustomerFixedDepositAccount) a);
        }
    }

    private CustomerFixedDepositAccount calculateDailyInterestForCustomerFixedDepositAccount(CustomerFixedDepositAccount account) {
        List<TimeRangeInterest> interests = account.getInterestRules();

        BigDecimal originalAmount = account.getBalance().setScale(30);
        BigDecimal totalInterest = BigDecimal.ZERO.setScale(30);
        // TODO: Change to day in month
        BigDecimal dailyInterval = new BigDecimal(30 * 12 / account.getProduct().getInterestInterval());

        for (TimeRangeInterest i : interests) {
            BigDecimal interval = BigDecimal.ZERO;
            // Min |   | Max
            if (originalAmount.compareTo(i.getMaximum()) >= 0) {
                // apply to full range
                interval = i.getMaximum().subtract(i.getMinimum());
            } else if (originalAmount.compareTo(i.getMinimum()) >= 0) {
                // apply to partial range
                interval = originalAmount.subtract(i.getMinimum());
            } else {
                // not apply, originalAmount too small
                continue;
            }
            BigDecimal percentage = i.getPercentage().divide(dailyInterval, 12, RoundingMode.HALF_UP);
            totalInterest = totalInterest.add(interval.multiply(percentage));
        }

        account.getCumulatedInterest().addCumulatedInterest(totalInterest);
        em.merge(account);
        return account;
    }

    private CustomerDepositAccount calculateDailyInterestForCustomerDepositAccount(CustomerDepositAccount a) {

        DepositAccountProduct p = (DepositAccountProduct) a.getProduct();
        List<Interest> allInterests = p.getInterestRules();
        List<Interest> normalInterests = new ArrayList<>();
        List<RangeInterest> rangeInterests = new ArrayList<>();
        List<ConditionInterest> conditionInterests = new ArrayList<>();

        for (Interest i : allInterests) {
            if (i instanceof TimeRangeInterest) {
            } else if (i instanceof RangeInterest) {
                rangeInterests.add((RangeInterest) i);
            } else if (i instanceof ConditionInterest) {
                conditionInterests.add((ConditionInterest) i);
            } else {
                normalInterests.add(i);
            }
        }

        BigDecimal originalAmount = a.getBalance().setScale(30);
        BigDecimal totalInterest = BigDecimal.ZERO.setScale(30);
        // TODO: Change to day in month
        BigDecimal dailyInterval = new BigDecimal(30 * 12 / a.getProduct().getInterestInterval());

        // Get highest base interest
        BigDecimal baseInterest = BigDecimal.ZERO;
        for (Interest i : normalInterests) {
            if (baseInterest.compareTo(i.getPercentage()) < 0) {
                baseInterest = i.getPercentage();
            }
        }
        // Assume Cap is 60,000, this is independent
        for (ConditionInterest i : conditionInterests) {
            // if fulfill, then stack
            Boolean met = isAccountMeetCondition(a, i);
            if (met) {
                System.out.println("Condition: " + i.getConditionType() + " has met with percentage " + i.getPercentage());
                BigDecimal ceiling = i.getCeiling();
                if (originalAmount.compareTo(ceiling) >= 0) {
                    // only process ceiling
                    BigDecimal percentage = i.getPercentage().divide(dailyInterval, 12, RoundingMode.HALF_UP);
                    System.out.println("Daily Interest Percentage " + percentage);
                    totalInterest = totalInterest.add(ceiling.multiply(percentage));
                } else {
                    // process originalAmount
                    BigDecimal percentage = i.getPercentage().divide(dailyInterval, 12, RoundingMode.HALF_UP);
                    System.out.println("Percentage " + percentage);
                    totalInterest = totalInterest.add(originalAmount.multiply(percentage));
                }
            }
        }
        // Assume not be overlapped
        BigDecimal leftOver = originalAmount;
        BigDecimal tempInterest = baseInterest;
        for (RangeInterest i : rangeInterests) {
            if (tempInterest.compareTo(i.getPercentage()) < 0) {
                tempInterest = i.getPercentage();
            }
            BigDecimal interval = BigDecimal.ZERO;
            // Min |   | Max
            if (originalAmount.compareTo(i.getMaximum()) >= 0) {
                // apply to full range
                interval = i.getMaximum().subtract(i.getMinimum());
            } else if (originalAmount.compareTo(i.getMinimum()) >= 0) {
                // apply to partial range
                interval = originalAmount.subtract(i.getMinimum());
            } else {
                // not apply, originalAmount too small
                continue;
            }
            BigDecimal percentage = baseInterest.divide(dailyInterval, 30, RoundingMode.HALF_UP);
            totalInterest = totalInterest.add(interval.multiply(percentage));
            leftOver = leftOver.subtract(interval);
        }

        BigDecimal percentage = tempInterest.divide(dailyInterval, 30, RoundingMode.HALF_UP);
        totalInterest = totalInterest.add(leftOver.multiply(percentage));

        System.out.println("Total interest for the day: " + totalInterest);
        a.getCumulatedInterest().addCumulatedInterest(totalInterest);
        System.out.println("Total cumulated interest for the day: " + a.getCumulatedInterest().getCummulativeAmount());
        em.merge(a);
        return a;
    }

    @Override
    public Boolean isAccountMeetCondition(DepositAccount a, ConditionInterest i) {
        Date lastMonthFirstDay = DateUtils.getLastBeginOfMonth();
        Date lastMonthLastDay = DateUtils.getLastEndOfMonth();
        Date sinceDate = DateUtils.getBeginOfMonth();
        Date toDate = DateUtils.getEndOfMonth();
        BigDecimal bd1 = new BigDecimal(500);
        BigDecimal bd2 = new BigDecimal(500.0);
        BigDecimal bd3 = new BigDecimal(1000);

        System.out.println(i.getConditionType());
        if (i.getConditionType() == InterestConditionType.BILL) {
            List<TransactionRecord> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.BILL);
            System.out.println(ts);
            BigDecimal minimumNumberOfBills = i.getAmount();
            BigDecimal currentNumberOfBills = new BigDecimal(ts.size());
            System.out.println(minimumNumberOfBills.compareTo(currentNumberOfBills) <= 0);
            return minimumNumberOfBills.compareTo(currentNumberOfBills) <= 0;
        } else if (i.getConditionType() == InterestConditionType.CCSPENDING) {
            List<TransactionRecord> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.CCSPENDING);
            System.out.println(ts);
            BigDecimal minimumAmountSpending = i.getAmount();
            BigDecimal currentAmountSpending = BigDecimal.ZERO;
            for (TransactionRecord t : ts) {
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
            if (a instanceof CustomerDepositAccount) {
                CustomerDepositAccount cda = (CustomerDepositAccount) a;
                System.out.println(cda.getPreviousBalance().compareTo(a.getBalance()) <= 0);
                return cda.getPreviousBalance().compareTo(a.getBalance()) <= 0;
            } else {
                return false;
            }

        } else if (i.getConditionType() == InterestConditionType.SALARY) {
            List<TransactionRecord> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.SALARY);
            System.out.println(ts);
            BigDecimal minimumSalaryCrediting = i.getAmount();
            BigDecimal currentSalaryCrediting = BigDecimal.ZERO;
            for (TransactionRecord t : ts) {
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
            List<TransactionRecord> ts = retrieveTransactions(a, sinceDate, toDate, TransactionType.SALARY);
            System.out.println(ts);
            return !ts.isEmpty();
        } else {
            return false;
        }
    }

    private List<TransactionRecord> retrieveTransactions(DepositAccount a, Date sinceDate, Date toDate, TransactionType type) {
        Query q = em.createQuery(
                "SELECT t FROM " + ConstantUtils.TRANSACTION_ENTITY + " t WHERE "
                + "t.fromAccount.accountNumber = :accountNumber AND "
                + "t.actionType = :type AND "
                + "t.creationDate BETWEEN :sinceDate AND :toDate"
        );
        q.setParameter("accountNumber", a.getAccountNumber());
        q.setParameter("sinceDate", sinceDate);
        q.setParameter("toDate", toDate);
        q.setParameter("type", type);
        return q.getResultList();
    }
}
