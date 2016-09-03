/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author leiyang
 */
@Entity
public class Interest implements Serializable {

    public enum InterestType {
        NORMAL {
            public String toString() {
                return "NORMAL";
            }
        },
        RANGE {
            public String toString() {
                return "RANGE";
            }
        },
        CONDITION {
            public String toString() {
                return "CONDITION";
            }
        }
    }
    @Id
    private String name;
    @Column(precision=12, scale=2)
    private BigDecimal percentage = new BigDecimal(0.01);
    private Boolean defaultCurrentAccount;
    private Boolean defaultFixedDepositAccount;
    private Boolean defaultSavingAccount;
    private Boolean defaultMobileAccount;
    private Boolean defaultLoanAccount;
    /**
     * @return the percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the defaultCurrentAccount
     */
    public Boolean getDefaultCurrentAccount() {
        return defaultCurrentAccount;
    }

    /**
     * @param defaultCurrentAccount the defaultCurrentAccount to set
     */
    public void setDefaultCurrentAccount(Boolean defaultCurrentAccount) {
        this.defaultCurrentAccount = defaultCurrentAccount;
    }

    /**
     * @return the defaultFixedDepositAccount
     */
    public Boolean getDefaultFixedDepositAccount() {
        return defaultFixedDepositAccount;
    }

    /**
     * @param defaultFixedDepositAccount the defaultFixedDepositAccount to set
     */
    public void setDefaultFixedDepositAccount(Boolean defaultFixedDepositAccount) {
        this.defaultFixedDepositAccount = defaultFixedDepositAccount;
    }

    /**
     * @return the defaultSavingAccount
     */
    public Boolean getDefaultSavingAccount() {
        return defaultSavingAccount;
    }

    /**
     * @param defaultSavingAccount the defaultSavingAccount to set
     */
    public void setDefaultSavingAccount(Boolean defaultSavingAccount) {
        this.defaultSavingAccount = defaultSavingAccount;
    }

    /**
     * @return the defaultMobileAccount
     */
    public Boolean getDefaultMobileAccount() {
        return defaultMobileAccount;
    }

    /**
     * @param defaultMobileAccount the defaultMobileAccount to set
     */
    public void setDefaultMobileAccount(Boolean defaultMobileAccount) {
        this.defaultMobileAccount = defaultMobileAccount;
    }

    /**
     * @return the defaultLoanAccount
     */
    public Boolean getDefaultLoanAccount() {
        return defaultLoanAccount;
    }

    /**
     * @param defaultLoanAccount the defaultLoanAccount to set
     */
    public void setDefaultLoanAccount(Boolean defaultLoanAccount) {
        this.defaultLoanAccount = defaultLoanAccount;
    }
    
}
