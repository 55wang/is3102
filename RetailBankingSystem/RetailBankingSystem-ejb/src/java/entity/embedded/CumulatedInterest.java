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
    private BigDecimal amount;
    private Integer times;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedDate = new Date();
    private Boolean isMonthly;

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

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
     * @return the isMonthly
     */
    public Boolean getIsMonthly() {
        return isMonthly;
    }

    /**
     * @param isMonthly the isMonthly to set
     */
    public void setIsMonthly(Boolean isMonthly) {
        this.isMonthly = isMonthly;
    }
}
