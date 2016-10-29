/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.common;

import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils.TransactionType;

/**
 *
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class TransactionRecord implements Serializable {
    
    @Id
    private String referenceNumber;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    
    // info
    @Column(nullable = false)
    private TransactionType actionType;
    private Boolean credit = true;
    @Column(precision=30, scale=20)
    private BigDecimal amount;
    
    // mapping
    @ManyToOne(cascade={CascadeType.MERGE})
    private DepositAccount fromAccount;
    @ManyToOne(cascade={CascadeType.MERGE})
    private DepositAccount toAccount;// can be null
    
    /**
     * @return the fromAccount
     */
    public DepositAccount getFromAccount() {
        return fromAccount;
    }

    /**
     * @param fromAccount the fromAccount to set
     */
    public void setFromAccount(DepositAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    /**
     * @return the toAccount
     */
    public DepositAccount getToAccount() {
        return toAccount;
    }

    /**
     * @param toAccount the toAccount to set
     */
    public void setToAccount(DepositAccount toAccount) {
        this.toAccount = toAccount;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber toAccount set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the credit
     */
    public Boolean getCredit() {
        return credit;
    }

    /**
     * @param credit the credit toAccount set
     */
    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount toAccount set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * @return the actionType
     */
    public TransactionType getActionType() {
        return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public void setActionType(TransactionType actionType) {
        this.actionType = actionType;
    }
}
