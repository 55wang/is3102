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
    
    @Column(precision=30, scale=20)
    private BigDecimal cummulativeAmount = BigDecimal.ZERO;// use to differetiate simple and cummulative interest
    private Integer times = 0;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedDate = new Date();

    public void addCumulatedInterest(BigDecimal i) {
        this.cummulativeAmount = this.cummulativeAmount.add(i);
        this.times++;
        this.updatedDate = new Date();
    }
    
    public void reset() {
        this.times = 0;
        this.cummulativeAmount = BigDecimal.ZERO;
        this.updatedDate = new Date();
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
}
