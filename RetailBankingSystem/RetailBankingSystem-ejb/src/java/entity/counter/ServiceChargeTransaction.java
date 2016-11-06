/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.counter;

import entity.customer.MainAccount;
import entity.staff.ServiceCharge;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leiyang
 */
@Entity
public class ServiceChargeTransaction implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    
    private String customerIC;
    
    @ManyToOne(cascade={CascadeType.MERGE})
    private ServiceCharge serviceCharge;
    @ManyToOne(cascade={CascadeType.MERGE})
    private StaffAccount staffAccount;
    @ManyToOne(cascade={CascadeType.MERGE})
    private MainAccount mainAccount;

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
        if (!(object instanceof ServiceChargeTransaction)) {
            return false;
        }
        ServiceChargeTransaction other = (ServiceChargeTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.staff.ServiceChargeTransaction[ id=" + id + " ]";
    }

    /**
     * @return the serviceCharge
     */
    public ServiceCharge getServiceCharge() {
        return serviceCharge;
    }

    /**
     * @param serviceCharge the serviceCharge to set
     */
    public void setServiceCharge(ServiceCharge serviceCharge) {
        this.serviceCharge = serviceCharge;
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
     * @return the staffAccount
     */
    public StaffAccount getStaffAccount() {
        return staffAccount;
    }

    /**
     * @param staffAccount the staffAccount to set
     */
    public void setStaffAccount(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
    }

    /**
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    /**
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }
    
}
