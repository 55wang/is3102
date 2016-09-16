/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
/**
 *
 * @author leiyang
 */
@Entity
public class DepositRules extends Rule {
    
    @Column(precision=12, scale=2)
    private BigDecimal initialDeposit;
    @Column(precision=12, scale=2)
    private BigDecimal minBalance;
    @Column(precision=12, scale=2)
    private BigDecimal charges;

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
}
