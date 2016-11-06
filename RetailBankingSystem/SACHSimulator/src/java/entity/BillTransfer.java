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
public class BillTransfer implements Serializable {
    @Id
    private String referenceNumber;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    // info
    @Column(precision=30, scale=20)
    private BigDecimal amount;
    private String partnerBankCode;
    private String partnerBankAccount;
    private String fromBankCode;
    private String shortCode;
    private String organizationName;
    private String billReferenceNumber;
    private Boolean settled;

    @Override
    public String toString() {
        return "BillTransfer{" + "referenceNumber=" + referenceNumber + ", creationDate=" + creationDate + ", amount=" + amount + ", partnerBankCode=" + partnerBankCode + ", fromBankCode=" + fromBankCode + ", shortCode=" + shortCode + ", organizationName=" + organizationName + ", billReferenceNumber=" + billReferenceNumber + ", settled=" + settled + '}';
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
     * @return the partnerBankCode
     */
    public String getPartnerBankCode() {
        return partnerBankCode;
    }

    /**
     * @param partnerBankCode the partnerBankCode to set
     */
    public void setPartnerBankCode(String partnerBankCode) {
        this.partnerBankCode = partnerBankCode;
    }

    /**
     * @return the shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @param shortCode the shortCode to set
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the billReferenceNumber
     */
    public String getBillReferenceNumber() {
        return billReferenceNumber;
    }

    /**
     * @param billReferenceNumber the billReferenceNumber to set
     */
    public void setBillReferenceNumber(String billReferenceNumber) {
        this.billReferenceNumber = billReferenceNumber;
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
     * @return the partnerBankAccount
     */
    public String getPartnerBankAccount() {
        return partnerBankAccount;
    }

    /**
     * @param partnerBankAccount the partnerBankAccount to set
     */
    public void setPartnerBankAccount(String partnerBankAccount) {
        this.partnerBankAccount = partnerBankAccount;
    }


    
}
