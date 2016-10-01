/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.embedded;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author leiyang
 */
@Embeddable
public class TransferLimits implements Serializable {
    
    // info
    @Column(precision=12, scale=2)
    private BigDecimal dailyWithdrawLimit = new BigDecimal(3000);
    @Column(precision=12, scale=2)
    private BigDecimal dailyLocalTransferLimit = new BigDecimal(3000);
    @Column(precision=12, scale=2)
    private BigDecimal dailyOverseasTransferLimit = new BigDecimal(10000);

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
}
