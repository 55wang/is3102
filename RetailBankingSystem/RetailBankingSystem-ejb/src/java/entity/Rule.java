/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Rule implements Serializable {
    @Id
    private String name;
    private Boolean saved;
    private Boolean defaultCurrentAccount;
    private Boolean defaultFixedDepositAccount;
    private Boolean defaultSavingAccount;
    private Boolean defaultMobileAccount;
    private Boolean defaultLoanAccount;
    private String defaultCustomizedAccountName;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date createDate = new Date();
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date updateDate = new Date();

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
    
    /**
     * @return the saved
     */
    public Boolean getSaved() {
        return saved;
    }

    /**
     * @param saved the saved to set
     */
    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }
    
    /**
     * @return the defaultCustomizedAccountName
     */
    public String getDefaultCustomizedAccountName() {
        return defaultCustomizedAccountName;
    }

    /**
     * @param defaultCustomizedAccountName the defaultCustomizedAccountName to set
     */
    public void setDefaultCustomizedAccountName(String defaultCustomizedAccountName) {
        this.defaultCustomizedAccountName = defaultCustomizedAccountName;
    }

}
