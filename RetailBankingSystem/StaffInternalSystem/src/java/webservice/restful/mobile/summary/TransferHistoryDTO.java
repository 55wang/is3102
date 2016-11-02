/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.summary;

/**
 *
 * @author leiyang
 */
public class TransferHistoryDTO {
    
    private String transferType;
    private String transferAmount;
    private String transferDate;
    private String transferCredit;

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
     * @return the transferCredit
     */
    public String getTransferCredit() {
        return transferCredit;
    }

    /**
     * @param transferCredit the transferCredit to set
     */
    public void setTransferCredit(String transferCredit) {
        this.transferCredit = transferCredit;
    }
}
