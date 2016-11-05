/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.transfer;

/**
 *
 * @author leiyang
 */
public class PayeeDTO {
    
    private String payeeId;
    private String payeeAccountNumber;
    private String payeeType;
    private String payeeAccountName;

    /**
     * @return the payeeId
     */
    public String getPayeeId() {
        return payeeId;
    }

    /**
     * @param payeeId the payeeId to set
     */
    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    /**
     * @return the payeeAccountNumber
     */
    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    /**
     * @param payeeAccountNumber the payeeAccountNumber to set
     */
    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    /**
     * @return the payeeType
     */
    public String getPayeeType() {
        return payeeType;
    }

    /**
     * @param payeeType the payeeType to set
     */
    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    /**
     * @return the payeeAccountName
     */
    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    /**
     * @param payeeAccountName the payeeAccountName to set
     */
    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }
}
