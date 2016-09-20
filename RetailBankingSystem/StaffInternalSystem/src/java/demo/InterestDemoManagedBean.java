/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import BatchProcess.InterestAccrualSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.DepositAccountProduct;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.EnumUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "interestDemoManagedBean")
@ViewScoped
public class InterestDemoManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal depositAccountSessionBean;
    @EJB
    private InterestSessionBeanLocal accountRuleSessionBean;
    @EJB
    private InterestAccrualSessionBeanLocal interestAccrualSessionBean;

    private DepositAccount account;
    private List<Interest> normalInterests = new ArrayList<>();
    private List<RangeInterest> rangeInterests = new ArrayList<>();
    private List<TimeRangeInterest> timeRangeInterests = new ArrayList<>();
    private List<ConditionInterest> conditionInterests = new ArrayList<>();

    public InterestDemoManagedBean() {
    }

    @PostConstruct
    public void init() {
        setAccount(depositAccountSessionBean.getAccountFromId(1L));
        initInterests();
    }

    public BigDecimal getTotalInterest(DepositAccount account) {
        BigDecimal totalInterest = BigDecimal.ZERO;
        BigDecimal monthlyInterval = null; 
        if (account instanceof CustomerDepositAccount) {
            monthlyInterval = new BigDecimal(12 / account.getProduct().getInterestInterval());
        } else {
            
        }
        
        System.out.println(monthlyInterval);
        BigDecimal originalAmount = account.getBalance();

        if (account.getType().equals(EnumUtils.DepositAccountType.CUSTOM)) {
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
                Boolean met = interestAccrualSessionBean.isAccountMeetCondition(account, i);
                if (met) {
                    System.out.println("Condition: " + i.getConditionType() + " has met with percentage " + i.getPercentage());
                    BigDecimal ceiling = i.getCeiling();
                    if (originalAmount.compareTo(ceiling) >= 0) {
                        // only process ceiling
                        BigDecimal percentage = i.getPercentage().divide(monthlyInterval, 12, RoundingMode.HALF_UP);
                        System.out.println("Percentage " + percentage);
                        totalInterest = totalInterest.add(ceiling.multiply(percentage));
                    } else {
                        // process originalAmount
                        BigDecimal percentage = i.getPercentage().divide(monthlyInterval, 12, RoundingMode.HALF_UP);
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
                BigDecimal percentage = baseInterest.divide(monthlyInterval, 12, RoundingMode.HALF_UP);
                totalInterest = totalInterest.add(interval.multiply(percentage));
                leftOver = leftOver.subtract(interval);
            }
            
            BigDecimal percentage = tempInterest.divide(monthlyInterval, 12, RoundingMode.HALF_UP);
            totalInterest = totalInterest.add(leftOver.multiply(percentage));
                
        } else if (account.getType().equals(EnumUtils.DepositAccountType.CURRENT)) {

        } else if (account.getType().equals(EnumUtils.DepositAccountType.SAVING)) {

        } else if (account.getType().equals(EnumUtils.DepositAccountType.FIXED)) {

        }

        return totalInterest;
    }

    private void initInterests() {
        List<Interest> interests = ((DepositAccountProduct)account.getProduct()).getInterestRules();

        for (Interest i : interests) {
            if (i instanceof TimeRangeInterest) {
                timeRangeInterests.add((TimeRangeInterest) i);
            } else if (i instanceof RangeInterest) {
                rangeInterests.add((RangeInterest) i);
            } else if (i instanceof ConditionInterest) {
                conditionInterests.add((ConditionInterest) i);
            } else {
                normalInterests.add(i);
            }
        }
    }

    /**
     * @return the account
     */
    public DepositAccount getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(DepositAccount account) {
        this.account = account;
    }

    /**
     * @return the normalInterests
     */
    public List<Interest> getNormalInterests() {
        return normalInterests;
    }

    /**
     * @param normalInterests the normalInterests to set
     */
    public void setNormalInterests(List<Interest> normalInterests) {
        this.normalInterests = normalInterests;
    }

    /**
     * @return the rangeInterests
     */
    public List<RangeInterest> getRangeInterests() {
        return rangeInterests;
    }

    /**
     * @param rangeInterests the rangeInterests to set
     */
    public void setRangeInterests(List<RangeInterest> rangeInterests) {
        this.rangeInterests = rangeInterests;
    }

    /**
     * @return the timeRangeInterests
     */
    public List<TimeRangeInterest> getTimeRangeInterests() {
        return timeRangeInterests;
    }

    /**
     * @param timeRangeInterests the timeRangeInterests to set
     */
    public void setTimeRangeInterests(List<TimeRangeInterest> timeRangeInterests) {
        this.timeRangeInterests = timeRangeInterests;
    }

    /**
     * @return the conditionInterests
     */
    public List<ConditionInterest> getConditionInterests() {
        return conditionInterests;
    }

    /**
     * @param conditionInterests the conditionInterests to set
     */
    public void setConditionInterests(List<ConditionInterest> conditionInterests) {
        this.conditionInterests = conditionInterests;
    }
}
