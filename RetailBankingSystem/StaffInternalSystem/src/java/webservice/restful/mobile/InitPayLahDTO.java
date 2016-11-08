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
public class InitPayLahDTO {
    
    private String noNewReq;
    private String balance;
    private String walletLimit;
    private String transferLimit;
    private String transferType;
    private String transferDate;
    private String transferAmount;
    private String transferAccount;

    /**
     * @return the balance
     */
    public String getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(String balance) {
        this.balance = balance;
    }

    /**
     * @return the walletLimit
     */
    public String getWalletLimit() {
        return walletLimit;
    }

    /**
     * @param walletLimit the walletLimit to set
     */
    public void setWalletLimit(String walletLimit) {
        this.walletLimit = walletLimit;
    }

    /**
     * @return the transferLimit
     */
    public String getTransferLimit() {
        return transferLimit;
    }

    /**
     * @param transferLimit the transferLimit to set
     */
    public void setTransferLimit(String transferLimit) {
        this.transferLimit = transferLimit;
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
     * @return the noNewReq
     */
    public String getNoNewReq() {
        return noNewReq;
    }

    /**
     * @param noNewReq the noNewReq to set
     */
    public void setNoNewReq(String noNewReq) {
        this.noNewReq = noNewReq;
    }
    
}
