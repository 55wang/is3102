/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.product;

import entity.card.product.CreditCardProduct;
import javax.persistence.Entity;

/**
 *
 * @author wang
 */
@Entity
public class RewardCardProduct extends CreditCardProduct {

    private Double localMileRate; // SGD$1 = 4 Miles
    private Double localPointRate; //SGD$1 = 10 points

    public Double getLocalMileRate() {
        return localMileRate;
    }

    public void setLocalMileRate(Double localMileRate) {
        this.localMileRate = localMileRate;
    }

    public Double getLocalPointRate() {
        return localPointRate;
    }

    public void setLocalPointRate(Double localPointRate) {
        this.localPointRate = localPointRate;
    }

}
