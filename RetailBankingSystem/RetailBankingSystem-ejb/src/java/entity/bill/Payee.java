/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.bill;

import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Entity
public class Payee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    
    // international messaging code
    private String swiftCode;
    private String clearCode;
    // local
    private String bankCode;
    private String branchCode;
    private String bankAddress;
    // info
    private String name;
    private EnumUtils.PayeeType type;
    private String accountNumber;
    private String fromName;
    private String myInitial;
    
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
     * @return the type
     */
    public EnumUtils.PayeeType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(EnumUtils.PayeeType type) {
        this.type = type;
    }

    /**
     * @return the swiftCode
     */
    public String getSwiftCode() {
        return swiftCode;
    }

    /**
     * @param swiftCode the swiftCode to set
     */
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    /**
     * @return the bankCode
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * @return the branchCode
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * @param branchCode the branchCode to set
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the fromName
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * @param fromName the fromName to set
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * @return the myInitial
     */
    public String getMyInitial() {
        return myInitial;
    }

    /**
     * @param myInitial the myInitial to set
     */
    public void setMyInitial(String myInitial) {
        this.myInitial = myInitial;
    }

    /**
     * @return the clearCode
     */
    public String getClearCode() {
        return clearCode;
    }

    /**
     * @param clearCode the clearCode to set
     */
    public void setClearCode(String clearCode) {
        this.clearCode = clearCode;
    }

    /**
     * @return the bankAddress
     */
    public String getBankAddress() {
        return bankAddress;
    }

    /**
     * @param bankAddress the bankAddress to set
     */
    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }
    
    
    
}
