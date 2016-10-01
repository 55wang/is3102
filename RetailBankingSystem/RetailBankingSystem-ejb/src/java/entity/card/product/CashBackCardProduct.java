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

    private Double petrolCashBackRate = 0.08; // 8%
    private Double groceryCashBackRate = 0.08; //8%
    private Double diningCashBackRate = 0.08; //8%

    public Double getPetrolCashBackRate() {
        return petrolCashBackRate;
    }

    public void setPetrolCashBackRate(Double petrolCashBackRate) {
        this.petrolCashBackRate = petrolCashBackRate;
    }

    public Double getGroceryCashBackRate() {
        return groceryCashBackRate;
    }

    public void setGroceryCashBackRate(Double groceryCashBackRate) {
        this.groceryCashBackRate = groceryCashBackRate;
    }

    public Double getDiningCashBackRate() {
        return diningCashBackRate;
    }

    public void setDiningCashBackRate(Double diningCashBackRate) {
        this.diningCashBackRate = diningCashBackRate;
    }

}
