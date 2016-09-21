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
public class RewardCardProduct extends CreditCardProduct {

    private double localMileRate; // SGD$1 = 4 Miles
    private double localPointRate; //SGD$1 = 10 points

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

}
