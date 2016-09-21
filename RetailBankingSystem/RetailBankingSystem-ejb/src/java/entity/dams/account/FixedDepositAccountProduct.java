/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class FixedDepositAccountProduct extends DepositProduct {
    
    private Integer interestInterval = 3;

    @Override
    public String toString() {
        return "FixedDepositAccountProduct{" + "interestInterval=" + interestInterval + '}';
    }
    
    /**
     * @return the interestInterval
     */
    public Integer getInterestInterval() {
        return interestInterval;
    }

    /**
     * @param interestInterval the interestInterval to set
     */
    public void setInterestInterval(Integer interestInterval) {
        this.interestInterval = interestInterval;
    }
}
