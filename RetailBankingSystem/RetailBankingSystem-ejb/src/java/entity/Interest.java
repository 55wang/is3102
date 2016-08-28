/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author leiyang
 */
@Entity
public class Interest implements Serializable {
    
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
    @Id
    private String name;
    @Column(precision=12, scale=2)
    private BigDecimal percentage = new BigDecimal(0.01);
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
