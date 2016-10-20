/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import entity.dams.account.CustomerDepositAccount;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Entity
public class DebitCardAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    
    // info
    @Temporal(value = TemporalType.DATE)
    private Date validDate;
    private EnumUtils.CardNetwork cardNetwork;
    private EnumUtils.CardAccountStatus CardStatus;
    private String creditCardNum;
    private Integer cvv; // LY: Use Integer instead of int
    private String nameOnCard;
    
    private double transactionMonthlyLimit;
    private double transactionDailyLimit;
    
    
    // mappings
    @ManyToOne()
    private CustomerDepositAccount customerDepositAccount;

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
        if (!(object instanceof DebitCardAccount)) {
            return false;
        }
        DebitCardAccount other = (DebitCardAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.card.account.DebitCardAccount[ id=" + id + " ]";
    }

    public EnumUtils.CardNetwork getCardNetwork() {
        return cardNetwork;
    }

    public void setCardNetwork(EnumUtils.CardNetwork cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public EnumUtils.CardAccountStatus getCardStatus() {
        return CardStatus;
    }

    public void setCardStatus(EnumUtils.CardAccountStatus CardStatus) {
        this.CardStatus = CardStatus;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public double getTransactionMonthlyLimit() {
        return transactionMonthlyLimit;
    }

    public void setTransactionMonthlyLimit(double transactionMonthlyLimit) {
        this.transactionMonthlyLimit = transactionMonthlyLimit;
    }

    public double getTransactionDailyLimit() {
        return transactionDailyLimit;
    }

    public void setTransactionDailyLimit(double transactionDailyLimit) {
        this.transactionDailyLimit = transactionDailyLimit;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public CustomerDepositAccount getCustomerDepositAccount() {
        return customerDepositAccount;
    }

    public void setCustomerDepositAccount(CustomerDepositAccount customerDepositAccount) {
        this.customerDepositAccount = customerDepositAccount;
    }

}
