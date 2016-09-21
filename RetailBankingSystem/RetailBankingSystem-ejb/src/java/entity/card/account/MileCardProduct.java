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
public class MileCardProduct extends CreditCardProduct {

    private double overseaMileRate; // USD$1 = 2 Miles
    private double localMileRate; // SGD$1 = 1.3 Miles

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

}
