/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.bill;

import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author leiyang
 */
@Entity
public class BillingOrg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    
    // info
    private String billReference;
    private Double billLimit;
    
    // mapping
    @OneToOne
    private Organization organization;
    @OneToOne
    private CustomerDepositAccount depositAccount;
    @ManyToOne(cascade = CascadeType.MERGE)
    private MainAccount mainAccount;

    
    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * @return the billReference
     */
    public String getBillReference() {
        return billReference;
    }

    /**
     * @param billReference the billReference to set
     */
    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    /**
     * @return the billLimit
     */
    public Double getBillLimit() {
        return billLimit;
    }

    /**
     * @param billLimit the billLimit to set
     */
    public void setBillLimit(Double billLimit) {
        this.billLimit = billLimit;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the depositAccount
     */
    public CustomerDepositAccount getDepositAccount() {
        return depositAccount;
    }

    /**
     * @param depositAccount the depositAccount to set
     */
    public void setDepositAccount(CustomerDepositAccount depositAccount) {
        this.depositAccount = depositAccount;
    }

}
