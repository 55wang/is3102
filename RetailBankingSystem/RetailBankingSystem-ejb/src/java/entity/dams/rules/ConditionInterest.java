/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.rules;

import java.math.BigDecimal;
import javax.persistence.Entity;
import utils.EnumUtils.InterestConditionType;

/**
 *
 * @author leiyang
 */
@Entity
public class ConditionInterest extends Interest {
    
    private InterestConditionType conditionType;
    private BigDecimal ceiling = new BigDecimal(60000);
    private BigDecimal amount = BigDecimal.ZERO;
    private Boolean stack = Boolean.TRUE;
    private Integer benefitMonths = 1;

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
    public InterestConditionType getConditionType() {
        return conditionType;
    }

    /**
     * @param conditionType the conditionType to set
     */
    public void setConditionType(InterestConditionType conditionType) {
        this.conditionType = conditionType;
    }
    
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

    /**
     * @return the benefitMonths
     */
    public Integer getBenefitMonths() {
        return benefitMonths;
    }

    /**
     * @param benefitMonths the benefitMonths to set
     */
    public void setBenefitMonths(Integer benefitMonths) {
        this.benefitMonths = benefitMonths;
    }
}
