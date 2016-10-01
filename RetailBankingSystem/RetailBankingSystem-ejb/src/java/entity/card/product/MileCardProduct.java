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
public class MileCardProduct extends CreditCardProduct {

    private Double overseaMileRate; // USD$1 = 2 Miles
    private Double localMileRate; // SGD$1 = 1.3 Miles

    public Double getOverseaMileRate() {
        return overseaMileRate;
    }

    public void setOverseaMileRate(Double overseaMileRate) {
        this.overseaMileRate = overseaMileRate;
    }

    public Double getLocalMileRate() {
        return localMileRate;
    }

    public void setLocalMileRate(Double localMileRate) {
        this.localMileRate = localMileRate;
    }

}
