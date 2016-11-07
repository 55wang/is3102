/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.request;

/**
 *
 * @author leiyang
 */
public class PaymeRequestDTO {
    
    private String requestId;
    private String requestDate;
    private String requestRemark;
    private String requestPaid;
    private String requestAmount;
    private String fromAccountNumber;
    private String fromName;
    private String toAccountNumber;
    private String toName;

    /**
     * @return the requestDate
     */
    public String getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the requestRemark
     */
    public String getRequestRemark() {
        return requestRemark;
    }

    /**
     * @param requestRemark the requestRemark to set
     */
    public void setRequestRemark(String requestRemark) {
        this.requestRemark = requestRemark;
    }

    /**
     * @return the requestPaid
     */
    public String getRequestPaid() {
        return requestPaid;
    }

    /**
     * @param requestPaid the requestPaid to set
     */
    public void setRequestPaid(String requestPaid) {
        this.requestPaid = requestPaid;
    }

    /**
     * @return the fromAccountNumber
     */
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    /**
     * @param fromAccountNumber the fromAccountNumber to set
     */
    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    /**
     * @return the toAccountNumber
     */
    public String getToAccountNumber() {
        return toAccountNumber;
    }

    /**
     * @param toAccountNumber the toAccountNumber to set
     */
    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    /**
     * @return the requestAmount
     */
    public String getRequestAmount() {
        return requestAmount;
    }

    /**
     * @param requestAmount the requestAmount to set
     */
    public void setRequestAmount(String requestAmount) {
        this.requestAmount = requestAmount;
    }

    /**
     * @return the fromName
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * @param fromName the fromName to set
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * @return the toName
     */
    public String getToName() {
        return toName;
    }

    /**
     * @param toName the toName to set
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * @return the requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
