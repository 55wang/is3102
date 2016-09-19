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
public class RewardCardAccount extends CreditCardAccount {

    private double merlionMiles;
    private double merlionPoints;
    private double localMileRate; // SGD$1 = 4 Miles
    private double localPointRate; //SGD$1 = 10 points
    private boolean minSpending = false; //default = false
    private double minSpendingAmount = 0;

    public double getMerlionMiles() {
        return merlionMiles;
    }

    public void setMerlionMiles(double merlionMiles) {
        this.merlionMiles = merlionMiles;
    }

    public double getMerlionPoints() {
        return merlionPoints;
    }

    public void setMerlionPoints(double merlionPoints) {
        this.merlionPoints = merlionPoints;
    }

    public double getLocalMileRate() {
        return localMileRate;
    }

    public void setLocalMileRate(double localMileRate) {
        this.localMileRate = localMileRate;
    }

    public double getLocalPointRate() {
        return localPointRate;
    }

    public void setLocalPointRate(double localPointRate) {
        this.localPointRate = localPointRate;
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
