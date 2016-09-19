/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.rules;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public abstract class Rule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // cannot be instantiated
    private String name; // name format "name" + version
    // REMARK:Update will create a new version and leave a copy
    private Integer version = 0;
    private Boolean isHistory = Boolean.FALSE;
    private Boolean saved = Boolean.FALSE;
    private Boolean defaultCurrentAccount = Boolean.FALSE;
    private Boolean defaultFixedDepositAccount = Boolean.FALSE;
    private Boolean defaultSavingAccount = Boolean.FALSE;
    private Boolean defaultMobileAccount = Boolean.FALSE;
    private Boolean defaultLoanAccount = Boolean.FALSE;
    private String defaultCustomizedAccountName;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    // Getters and Setters
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

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the isHistory
     */
    public Boolean getIsHistory() {
        return isHistory;
    }

    /**
     * @param isHistory the isHistory to set
     */
    public void setIsHistory(Boolean isHistory) {
        this.isHistory = isHistory;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
