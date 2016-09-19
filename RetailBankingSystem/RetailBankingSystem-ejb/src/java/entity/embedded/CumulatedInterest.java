/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.embedded;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leiyang
 */
@Embeddable
public class CumulatedInterest implements Serializable {
    
    @Column(precision=12, scale=2)
    private BigDecimal currentAmount = BigDecimal.ZERO;
    @Column(precision=12, scale=2)
    private BigDecimal cummulativeAmount = BigDecimal.ZERO;// use to differetiate simple and cummulative interest
    private Integer times = 0;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedDate = new Date();
    private Integer intervalMonth = 1;


    /**
     * @return the times
     */
    public Integer getTimes() {
        return times;
    }

    /**
     * @param times the times to set
     */
    public void setTimes(Integer times) {
        this.times = times;
    }

    /**
     * @return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return the currentAmount
     */
    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    /**
     * @param currentAmount the currentAmount to set
     */
    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    /**
     * @return the cummulativeAmount
     */
    public BigDecimal getCummulativeAmount() {
        return cummulativeAmount;
    }

    /**
     * @param cummulativeAmount the cummulativeAmount to set
     */
    public void setCummulativeAmount(BigDecimal cummulativeAmount) {
        this.cummulativeAmount = cummulativeAmount;
    }

    /**
     * @return the intervalMonth
     */
    public Integer getIntervalMonth() {
        return intervalMonth;
    }

    /**
     * @param intervalMonth the intervalMonth to set
     */
    public void setIntervalMonth(Integer intervalMonth) {
        this.intervalMonth = intervalMonth;
    }
}
