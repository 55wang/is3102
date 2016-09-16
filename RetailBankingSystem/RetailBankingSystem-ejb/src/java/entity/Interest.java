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
public class Interest extends Rule {
    
    public enum InterestType {
        NORMAL {
            public String toString() {
                return "NORMAL";
            }
        },
        RANGE {
            public String toString() {
                return "RANGE";
            }
        },
        CONDITION {
            public String toString() {
                return "CONDITION";
            }
        }
    }
    @Column(precision=12, scale=2)
    private BigDecimal percentage;
    
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
}
