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
public class BillOrgDTO {
    
    private String billId;
    private String billNumber;
    private String billType;
    private String billName;

    /**
     * @return the billId
     */
    public String getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(String billId) {
        this.billId = billId;
    }

    /**
     * @return the billNumber
     */
    public String getBillNumber() {
        return billNumber;
    }

    /**
     * @param billNumber the billNumber to set
     */
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    /**
     * @return the billType
     */
    public String getBillType() {
        return billType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * @return the billName
     */
    public String getBillName() {
        return billName;
    }

    /**
     * @param billName the billName to set
     */
    public void setBillName(String billName) {
        this.billName = billName;
    }
    
}
