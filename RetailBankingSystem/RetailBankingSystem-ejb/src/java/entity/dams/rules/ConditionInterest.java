/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.rules;

import java.math.BigDecimal;
import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class ConditionInterest extends Interest {

    /**
     * @return the stack
     */
    public Boolean getStack() {
        return stack;
    }

    /**
     * @param stack the stack to set
     */
    public void setStack(Boolean stack) {
        this.stack = stack;
    }

    public enum ConditionType {
        BILL {
            @Override
            public String toString() {
                return "BILL";
            }
        },
        CCSPENDING {
            @Override
            public String toString() {
                return "CCSPENDING";
            }
        },
        SALARY {
            @Override
            public String toString() {
                return "SALARY";
            }
        },
        INVEST {
            @Override
            public String toString() {
                return "INVEST";
            }
        },
        INCREASE {
            @Override
            public String toString() {
                return "INCREASE";
            }
        }
    }
    private String conditionType = "";
    private BigDecimal ceiling = new BigDecimal("60000");
    private BigDecimal amount;
    private Boolean stack;

    /**
     * @return the ceiling
     */
    public BigDecimal getCeiling() {
        return ceiling;
    }

    /**
     * @param ceiling the ceiling to set
     */
    public void setCeiling(BigDecimal ceiling) {
        this.ceiling = ceiling;
    }
    
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
     * @return the conditionType
     */
    public String getConditionType() {
        return conditionType;
    }

    /**
     * @param conditionType the conditionType to set
     */
    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }
}