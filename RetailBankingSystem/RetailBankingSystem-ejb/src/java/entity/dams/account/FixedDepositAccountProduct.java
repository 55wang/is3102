/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class FixedDepositAccountProduct extends DepositProduct {
    
    // info
    @Column(precision=18, scale=4)
    private BigDecimal minAmount;
    @Column(precision=18, scale=4)
    private BigDecimal maxAmount;
    private Integer maximumMaturityMonths = 36; // interest follow this rule
    private Integer minimumMaturityMonths = 1;

    /**
     * @return the minAmount
     */
    public BigDecimal getMinAmount() {
        return minAmount;
    }

    /**
     * @param minAmount the minAmount to set
     */
    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    /**
     * @return the maxAmount
     */
    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    /**
     * @param maxAmount the maxAmount to set
     */
    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    /**
     * @return the maximumMaturityMonths
     */
    public Integer getMaximumMaturityMonths() {
        return maximumMaturityMonths;
    }

    /**
     * @param maximumMaturityMonths the maximumMaturityMonths to set
     */
    public void setMaximumMaturityMonths(Integer maximumMaturityMonths) {
        this.maximumMaturityMonths = maximumMaturityMonths;
    }

    /**
     * @return the minimumMaturityMonths
     */
    public Integer getMinimumMaturityMonths() {
        return minimumMaturityMonths;
    }

    /**
     * @param minimumMaturityMonths the minimumMaturityMonths to set
     */
    public void setMinimumMaturityMonths(Integer minimumMaturityMonths) {
        this.minimumMaturityMonths = minimumMaturityMonths;
    }
}
