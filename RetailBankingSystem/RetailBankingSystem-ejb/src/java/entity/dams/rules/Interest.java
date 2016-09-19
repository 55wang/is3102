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
public class Interest extends Rule {
    
    
    @Column(precision=12, scale=2)
    private BigDecimal percentage;
    private Boolean isCumulative = Boolean.TRUE;
    
    /**
     * @return the percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the isCumulative
     */
    public Boolean getIsCumulative() {
        return isCumulative;
    }

    /**
     * @param isCumulative the isCumulative to set
     */
    public void setIsCumulative(Boolean isCumulative) {
        this.isCumulative = isCumulative;
    }
}
