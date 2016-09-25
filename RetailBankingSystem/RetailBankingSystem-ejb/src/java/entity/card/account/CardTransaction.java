/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import server.utilities.EnumUtils.*;

/**
 *
 * @author wang
 */
@Entity
public class CardTransaction implements Serializable {

    private static long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date transactionTimeStamp;
    private String transactionCode;
    private String transactionDescription;
    private boolean isDebit;
    private boolean isCredit;
    private double debitAmount;
    private double creditAmount;
    private CardTransactionType cardTransactionType;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private CreditCardAccount creditCardAccount;
    

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
        if (!(object instanceof CardTransaction)) {
            return false;
        }
        CardTransaction other = (CardTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.card.account.Transaction[ id=" + id + " ]";
    }

    public Date getTransactionTimeStamp() {
        return transactionTimeStamp;
    }

    public void setTransactionTimeStamp(Date transactionTimeStamp) {
        this.transactionTimeStamp = transactionTimeStamp;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public boolean isIsDebit() {
        return isDebit;
    }

    public void setIsDebit(boolean isDebit) {
        this.isDebit = isDebit;
    }

    public boolean isIsCredit() {
        return isCredit;
    }

    public void setIsCredit(boolean isCredit) {
        this.isCredit = isCredit;
    }

    public double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public CardTransactionType getCardTransactionType() {
        return cardTransactionType;
    }

    public void setCardTransactionType(CardTransactionType cardTransactionType) {
        this.cardTransactionType = cardTransactionType;
    }

    public CreditCardAccount getCreditCardAccount() {
        return creditCardAccount;
    }

    public void setCreditCardAccount(CreditCardAccount creditCardAccount) {
        this.creditCardAccount = creditCardAccount;
    }

}
