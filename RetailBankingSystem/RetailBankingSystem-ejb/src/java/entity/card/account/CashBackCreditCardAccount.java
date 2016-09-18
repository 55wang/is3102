/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author wang
 */
@Entity
public class CashBackCreditCardAccount extends CreditCardAccount {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double cashBackAmount;
    private double petrolCashBackRate = 0.08; // 8%
    private double groceryCashBackRate = 0.08; //8%
    private double diningCashBackRate = 0.08; //8%
    private boolean minSpending = false; //default = false
    private double minSpendingAmount = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CashBackCreditCardAccount)) {
            return false;
        }
        CashBackCreditCardAccount other = (CashBackCreditCardAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.card.account.CashBackCreditCardAccount[ id=" + id + " ]";
    }

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
