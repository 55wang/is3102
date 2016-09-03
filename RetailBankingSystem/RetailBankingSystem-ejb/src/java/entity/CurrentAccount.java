/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class CurrentAccount extends BankAccount {
    // Iinit default limit, User can reset
    private final String type = "CURRENT";
    private BigDecimal dailyWithdrawLimit = new BigDecimal(3000);
    private BigDecimal dailyLocalTransferLimit = new BigDecimal(3000);
    private BigDecimal dailyOverseasTransferLimit = new BigDecimal(10000);
    private BigDecimal minBalance = new BigDecimal(1000);
    private BigDecimal charges = new BigDecimal(2);

    /**
     * @return the dailyWithdrawLimit
     */
    public BigDecimal getDailyWithdrawLimit() {
        return dailyWithdrawLimit;
    }

    /**
     * @param dailyWithdrawLimit the dailyWithdrawLimit to set
     */
    public void setDailyWithdrawLimit(BigDecimal dailyWithdrawLimit) {
        this.dailyWithdrawLimit = dailyWithdrawLimit;
    }

    /**
     * @return the dailyLocalTransferLimit
     */
    public BigDecimal getDailyLocalTransferLimit() {
        return dailyLocalTransferLimit;
    }

    /**
     * @param dailyLocalTransferLimit the dailyLocalTransferLimit to set
     */
    public void setDailyLocalTransferLimit(BigDecimal dailyLocalTransferLimit) {
        this.dailyLocalTransferLimit = dailyLocalTransferLimit;
    }

    /**
     * @return the dailyOverseasTransferLimit
     */
    public BigDecimal getDailyOverseasTransferLimit() {
        return dailyOverseasTransferLimit;
    }

    /**
     * @param dailyOverseasTransferLimit the dailyOverseasTransferLimit to set
     */
    public void setDailyOverseasTransferLimit(BigDecimal dailyOverseasTransferLimit) {
        this.dailyOverseasTransferLimit = dailyOverseasTransferLimit;
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
}
