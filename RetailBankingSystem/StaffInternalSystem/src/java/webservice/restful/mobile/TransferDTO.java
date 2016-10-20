/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile;

/**
 *
 * @author leiyang
 */
public class TransferDTO {
    private String referenceNumber;
    private String transferAmount;
    private String transferType;
    private String transferDate;
    private String transferAccount;

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

}
