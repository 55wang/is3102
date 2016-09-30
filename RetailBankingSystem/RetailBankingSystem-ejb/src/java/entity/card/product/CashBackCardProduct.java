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
public class CashBackCardProduct extends CreditCardProduct {

    private double petrolCashBackRate = 0.08; // 8%
    private double groceryCashBackRate = 0.08; //8%
    private double diningCashBackRate = 0.08; //8%

    public double getPetrolCashBackRate() {
        return petrolCashBackRate;
    }

    public void setPetrolCashBackRate(double petrolCashBackRate) {
        this.petrolCashBackRate = petrolCashBackRate;
    }

    public double getGroceryCashBackRate() {
        return groceryCashBackRate;
    }

    public void setGroceryCashBackRate(double groceryCashBackRate) {
        this.groceryCashBackRate = groceryCashBackRate;
    }

    public double getDiningCashBackRate() {
        return diningCashBackRate;
    }

    public void setDiningCashBackRate(double diningCashBackRate) {
        this.diningCashBackRate = diningCashBackRate;
    }

}
