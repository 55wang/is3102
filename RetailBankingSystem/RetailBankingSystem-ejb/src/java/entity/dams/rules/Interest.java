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
    
    public enum InterestType {
        NORMAL {
            @Override
            public String toString() {
                return "NORMAL";
            }
        },
        RANGE {
            @Override
            public String toString() {
                return "RANGE";
            }
        },
        TIMERANGE {
            @Override
            public String toString() {
                return "TIMERANGE";
            }
        },
        CONDITION {
            @Override
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
