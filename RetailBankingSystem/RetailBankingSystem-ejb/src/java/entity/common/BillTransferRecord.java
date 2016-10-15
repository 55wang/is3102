/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.common;

import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class BillTransferRecord extends TransactionRecord {
    
    private String partnerBankCode;
    private String shortCode;
    private String organizationName;
    private String billReferenceNumber;
    private Boolean settled;

    /**
     * @return the partnerBankCode
     */
    public String getPartnerBankCode() {
        return partnerBankCode;
    }

    /**
     * @param partnerBankCode the partnerBankCode to set
     */
    public void setPartnerBankCode(String partnerBankCode) {
        this.partnerBankCode = partnerBankCode;
    }

    /**
     * @return the shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @param shortCode the shortCode to set
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the billReferenceNumber
     */
    public String getBillReferenceNumber() {
        return billReferenceNumber;
    }

    /**
     * @param billReferenceNumber the billReferenceNumber to set
     */
    public void setBillReferenceNumber(String billReferenceNumber) {
        this.billReferenceNumber = billReferenceNumber;
    }

    /**
     * @return the settled
     */
    public Boolean getSettled() {
        return settled;
    }

    /**
     * @param settled the settled to set
     */
    public void setSettled(Boolean settled) {
        this.settled = settled;
    }
}
