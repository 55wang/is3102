/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wang
 */
@Entity
public class VisaCardTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    
    private String referenceNum;
    private String creditCardNumber;
    private String amount;
    private String description;
    private String message;
    private String transactionCode;
    private String authorizationCode;
    private String cardType;
    private String fromBankCode;
    private String toBankCode;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    private Boolean settledStatus = false;

    public VisaCardTransaction() {
        setId(System.currentTimeMillis());
    }

    public VisaCardTransaction(JsonObject jsonString, Long Id, String referenceNum, String toBankCode) {
        this.id = Id;
        this.creditCardNumber = jsonString.getString("creditCardNumber");
        this.amount = jsonString.getString("amount");
        this.description = jsonString.getString("description");
        this.message = jsonString.getString("message");
        this.transactionCode = jsonString.getString("transactionCode");
        this.authorizationCode = jsonString.getString("authorizationCode");
        this.fromBankCode = "001";
        this.toBankCode = toBankCode;
        this.referenceNum = referenceNum;
        this.creationDate = new Date();
    }

    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the transactionCode
     */
    public String getTransactionCode() {
        return transactionCode;
    }

    /**
     * @param transactionCode the transactionCode to set
     */
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    /**
     * @return the authorizationCode
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * @param authorizationCode the authorizationCode to set
     */
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

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
        if (!(object instanceof VisaCardTransaction)) {
            return false;
        }
        VisaCardTransaction other = (VisaCardTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.VisaCardTransaction[ id=" + id + " ]";
    }

    public Boolean getSettledStatus() {
        return settledStatus;
    }

    public void setSettledStatus(Boolean settledStatus) {
        this.settledStatus = settledStatus;
    }

    /**
     * @return the cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * @param cardType the cardType to set
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
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
     * @return the referenceNum
     */
    public String getReferenceNum() {
        return referenceNum;
    }

    /**
     * @param referenceNum the referenceNum to set
     */
    public void setReferenceNum(String referenceNum) {
        this.referenceNum = referenceNum;
    }

}
