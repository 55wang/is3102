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
public class CashBackCardAccount extends CreditCardAccount {

    private double cashBackAmount;
    private double petrolCashBackRate = 0.08; // 8%
    private double groceryCashBackRate = 0.08; //8%
    private double diningCashBackRate = 0.08; //8%
    private boolean minSpending = false; //default = false
    private double minSpendingAmount = 0;

    public double getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(double cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }

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
