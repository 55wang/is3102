/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.rules;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class RangeInterest extends Interest {
    
    // info
    @Column(precision=18, scale=4)
    private BigDecimal minimum;
    @Column(precision=18, scale=4)
    private BigDecimal maximum;

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
}
