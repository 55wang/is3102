/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leiyang
 */
@Entity
@XmlRootElement
public class PaymentTransfer implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String referenceNumber;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    // info
    @Column(precision=30, scale=20)
    private BigDecimal amount;
    private String toBankCode;
    private String toBranchCode;
    private String accountNumber;
    private String toName;
    private String fromName;
    private String fromBankCode;
    private String myInitial;
    private Boolean settled;

    @Override
    public String toString() {
        return "PaymentTransfer{" + "referenceNumber=" + referenceNumber + ", creationDate=" + creationDate + ", amount=" + amount + ", toBankCode=" + toBankCode + ", toBranchCode=" + toBranchCode + ", accountNumber=" + accountNumber + ", toName=" + toName + ", fromName=" + fromName + ", fromBankCode=" + fromBankCode + ", myInitial=" + myInitial + ", settled=" + settled + '}';
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * @return the toName
     */
    public String getToName() {
        return toName;
    }

    /**
     * @param toName the toName to set
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * @return the settled
     */
    public Boolean getSettled() {
        return settled;
    }

    /**
     * @param settled the settled to set
     */
    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    /**
     * @return the toBankCode
     */
    public String getToBankCode() {
        return toBankCode;
    }

    /**
     * @param toBankCode the toBankCode to set
     */
    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    /**
     * @return the toBranchCode
     */
    public String getToBranchCode() {
        return toBranchCode;
    }

    /**
     * @param toBranchCode the toBranchCode to set
     */
    public void setToBranchCode(String toBranchCode) {
        this.toBranchCode = toBranchCode;
    }

    /**
     * @return the fromBankCode
     */
    public String getFromBankCode() {
        return fromBankCode;
    }

    /**
     * @param fromBankCode the fromBankCode to set
     */
    public void setFromBankCode(String fromBankCode) {
        this.fromBankCode = fromBankCode;
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
