/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import javax.persistence.Entity;

/**
 *
 * @author wang
 */
@Entity
public class MileCardAccount extends CreditCardAccount {
    
    private double merlionMiles;
    private double overseaMileRate; // USD$1 = 2 Miles
    private double localMileRate; // SGD$1 = 1.3 Miles
    private boolean minSpending = false; //default = false
    private double minSpendingAmount = 0; 

    public double getMerlionMiles() {
        return merlionMiles;
    }

    public void setMerlionMiles(double merlionMiles) {
        this.merlionMiles = merlionMiles;
    }

    public double getOverseaMileRate() {
        return overseaMileRate;
    }

    public void setOverseaMileRate(double overseaMileRate) {
        this.overseaMileRate = overseaMileRate;
    }

    public double getLocalMileRate() {
        return localMileRate;
    }

    public void setLocalMileRate(double localMileRate) {
        this.localMileRate = localMileRate;
    }

    public boolean isMinSpending() {
        return minSpending;
    }

    public void setMinSpending(boolean minSpending) {
        this.minSpending = minSpending;
    }

    public double getMinSpendingAmount() {
        return minSpendingAmount;
    }

    public void setMinSpendingAmount(double minSpendingAmount) {
        this.minSpendingAmount = minSpendingAmount;
    }
    
}
