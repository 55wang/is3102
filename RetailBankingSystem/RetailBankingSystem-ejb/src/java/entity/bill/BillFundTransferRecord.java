/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author qiuxiaqing
 */
@Entity
public class BillFundTransferRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String referenceNumber;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    // info
    @Column(precision = 30, scale = 20)
    private BigDecimal amount;
    private String toBankCode;
    private String toBankAccount;
    private String fromBankCode;
    private String fromBankAccount;
    private String shortCode;
    private String organizationName;
    private String billReferenceNumber;
    private Boolean settled;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.referenceNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BillFundTransferRecord other = (BillFundTransferRecord) obj;
        if (!Objects.equals(this.referenceNumber, other.referenceNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BillFundTransferRecord{" + "referenceNumber=" + referenceNumber + '}';
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getToBankCode() {
        return toBankCode;
    }

    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    public String getToBankAccount() {
        return toBankAccount;
    }

    public void setToBankAccount(String toBankAccount) {
        this.toBankAccount = toBankAccount;
    }

    public String getFromBankCode() {
        return fromBankCode;
    }

    public void setFromBankCode(String fromBankCode) {
        this.fromBankCode = fromBankCode;
    }

    public String getFromBankAccount() {
        return fromBankAccount;
    }

    public void setFromBankAccount(String fromBankAccount) {
        this.fromBankAccount = fromBankAccount;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getBillReferenceNumber() {
        return billReferenceNumber;
    }

    public void setBillReferenceNumber(String billReferenceNumber) {
        this.billReferenceNumber = billReferenceNumber;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }
    
    

}
