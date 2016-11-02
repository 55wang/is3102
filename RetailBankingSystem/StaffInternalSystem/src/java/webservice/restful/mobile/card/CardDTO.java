/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.card;

/**
 *
 * @author leiyang
 */
public class CardDTO {
    
    private String ccNumber;
    private String nameOnCard;
    private String ccType;
    private String outstandingAmount;
    private String expiryDate;
    private String transferType;
    private String transferDate;
    private String transferAmount;
    private String transferAccount;

    /**
     * @return the ccNumber
     */
    public String getCcNumber() {
        return ccNumber;
    }

    /**
     * @param ccNumber the ccNumber to set
     */
    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    /**
     * @return the nameOnCard
     */
    public String getNameOnCard() {
        return nameOnCard;
    }

    /**
     * @param nameOnCard the nameOnCard to set
     */
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    /**
     * @return the ccType
     */
    public String getCcType() {
        return ccType;
    }

    /**
     * @param ccType the ccType to set
     */
    public void setCcType(String ccType) {
        this.ccType = ccType;
    }

    /**
     * @return the outstandingAmount
     */
    public String getOutstandingAmount() {
        return outstandingAmount;
    }

    /**
     * @param outstandingAmount the outstandingAmount to set
     */
    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    /**
     * @return the transferType
     */
    public String getTransferType() {
        return transferType;
    }

    /**
     * @param transferType the transferType to set
     */
    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    /**
     * @return the transferDate
     */
    public String getTransferDate() {
        return transferDate;
    }

    /**
     * @param transferDate the transferDate to set
     */
    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    /**
     * @return the transferAmount
     */
    public String getTransferAmount() {
        return transferAmount;
    }

    /**
     * @param transferAmount the transferAmount to set
     */
    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    /**
     * @return the transferAccount
     */
    public String getTransferAccount() {
        return transferAccount;
    }

    /**
     * @param transferAccount the transferAccount to set
     */
    public void setTransferAccount(String transferAccount) {
        this.transferAccount = transferAccount;
    }

    /**
     * @return the expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
