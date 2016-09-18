/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author wang
 */
@Entity
public class MileCardAccount extends CreditCardAccount {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double merlionMiles;
    private double overseaMileRate; // USD$1 = 2 Miles
    private double localMileRate; // SGD$1 = 1.3 Miles
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
        if (!(object instanceof MileCardAccount)) {
            return false;
        }
        MileCardAccount other = (MileCardAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.card.account.MileCardAccount[ id=" + id + " ]";
    }

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
