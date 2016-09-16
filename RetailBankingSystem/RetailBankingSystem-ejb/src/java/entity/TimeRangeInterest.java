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
public class TimeRangeInterest extends Interest {
    // Only for fixed deposit account
    @Column(precision=12, scale=2)
    private BigDecimal minimum;
    @Column(precision=12, scale=2)
    private BigDecimal maximum;
    // if endMonth is 0, assume > startMonth will apply this interest
    private Integer startMonth;
    private Integer endMonth;
    /**
     * @return the minimum
     */
    public BigDecimal getMinimum() {
        return minimum;
    }

    /**
     * @param minimum the minimum to set
     */
    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    /**
     * @return the maximum
     */
    public BigDecimal getMaximum() {
        return maximum;
    }

    /**
     * @param maximum the maximum to set
     */
    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    /**
     * @return the startMonth
     */
    public Integer getStartMonth() {
        return startMonth;
    }

    /**
     * @param startMonth the startMonth to set
     */
    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * @return the endMonth
     */
    public Integer getEndMonth() {
        return endMonth;
    }

    /**
     * @param endMonth the endMonth to set
     */
    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
    }
}
