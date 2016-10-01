/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *  http://www.moneysense.gov.sg/Understanding-Financial-Products/Banking-and-Cash/Banking.aspx
 * @author leiyang
 */
@Entity
//@Inheritance(strategy=InheritanceType.JOINED)
public class DepositAccountProduct extends DepositProduct {
    
    // info
    @Column(precision=12, scale=2)
    private BigDecimal initialDeposit;
    @Column(precision=12, scale=2)
    private BigDecimal minBalance;
    @Column(precision=12, scale=2)
    private BigDecimal charges;
    @Column(precision=12, scale=2)
    private BigDecimal annualFees;
    private Integer waivedMonths;
    
    // mapping
    @OneToMany(cascade = {CascadeType.MERGE})
    private List<Interest> interestRules = new ArrayList<>();

    @Override
    public String toString() {
        return "DepositAccountProduct{" + "initialDeposit=" + initialDeposit + ", minBalance=" + minBalance + ", charges=" + charges + ", annualFees=" + annualFees + '}';
    }
    
    public void addInterest(Interest i) {
        this.interestRules.add(i);
    }
    
    public void addInterestsRules(List<Interest> interests) {
        interests.addAll(interests);
    }
    
    public void removeInterestsRules(List<Interest> interests) {
        interests.removeAll(interests);
    }
    
    public List<Interest> getBaseInterest() {
        List<Interest> result = new ArrayList();
        for (Interest i : interestRules) {
            if (i instanceof TimeRangeInterest) {
            } else if (i instanceof RangeInterest) {
            } else if (i instanceof ConditionInterest) {
            } else {
                result.add(i);
            }
        }
        return result;
    }
    
    public List<ConditionInterest> getConditionalInterest() {
        List<ConditionInterest> result = new ArrayList();
        for (Interest i : interestRules) {
            if (i instanceof TimeRangeInterest) {
            } else if (i instanceof RangeInterest) {
            } else if (i instanceof ConditionInterest) {
                result.add((ConditionInterest)i);
            }
        }
        return result;
    }
    
    public List<RangeInterest> getRangeInterest() {
        List<RangeInterest> result = new ArrayList();
        for (Interest i : interestRules) {
            if (i instanceof TimeRangeInterest) {
            } else if (i instanceof RangeInterest) {
                result.add((RangeInterest)i);
            } else if (i instanceof ConditionInterest) {
            }
        }
        return result;
    }
    
    /**
     * @return the initialDeposit
     */
    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    /**
     * @param initialDeposit the initialDeposit to set
     */
    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    /**
     * @return the minBalance
     */
    public BigDecimal getMinBalance() {
        return minBalance;
    }

    /**
     * @param minBalance the minBalance to set
     */
    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    /**
     * @return the charges
     */
    public BigDecimal getCharges() {
        return charges;
    }

    /**
     * @param charges the charges to set
     */
    public void setCharges(BigDecimal charges) {
        this.charges = charges;
    }

    /**
     * @return the annualFees
     */
    public BigDecimal getAnnualFees() {
        return annualFees;
    }

    /**
     * @param annualFees the annualFees to set
     */
    public void setAnnualFees(BigDecimal annualFees) {
        this.annualFees = annualFees;
    }

    /**
     * @return the interestRules
     */
    public List<Interest> getInterestRules() {
        return interestRules;
    }

    /**
     * @param interestRules the interestRules to set
     */
    public void setInterestRules(List<Interest> interestRules) {
        this.interestRules = interestRules;
    }

    /**
     * @return the waivedMonths
     */
    public Integer getWaivedMonths() {
        return waivedMonths;
    }

    /**
     * @param waivedMonths the waivedMonths to set
     */
    public void setWaivedMonths(Integer waivedMonths) {
        this.waivedMonths = waivedMonths;
    }
}
