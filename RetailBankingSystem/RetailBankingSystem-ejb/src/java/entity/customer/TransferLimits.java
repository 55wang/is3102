/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils.InterBankTransferLimit;
import server.utilities.EnumUtils.IntraBankTransferLimit;
import server.utilities.EnumUtils.OverseasBankTransferLimit;

/**
 *
 * @author leiyang
 */
@Entity
public class TransferLimits implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date updateDate = new Date();
    
    // info
    private IntraBankTransferLimit dailyIntraBankLimit = IntraBankTransferLimit._3000;
    private InterBankTransferLimit dailyInterBankLimit = InterBankTransferLimit._1000;
    private OverseasBankTransferLimit dailyOverseasBankLimit = OverseasBankTransferLimit._1000;
    
    // mapping
    @OneToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;
    
    public TransferLimits() {
        
    }
    
    public TransferLimits(MainAccount ma) {
        this.mainAccount = ma;
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
        if (!(object instanceof TransferLimits)) {
            return false;
        }
        TransferLimits other = (TransferLimits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.customer.TransferLimits[ id=" + id + " ]";
    }
    
    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @return the dailyIntraBankLimit
     */
    public IntraBankTransferLimit getDailyIntraBankLimit() {
        return dailyIntraBankLimit;
    }

    /**
     * @param dailyIntraBankLimit the dailyIntraBankLimit to set
     */
    public void setDailyIntraBankLimit(IntraBankTransferLimit dailyIntraBankLimit) {
        this.dailyIntraBankLimit = dailyIntraBankLimit;
    }

    /**
     * @return the dailyInterBankLimit
     */
    public InterBankTransferLimit getDailyInterBankLimit() {
        return dailyInterBankLimit;
    }

    /**
     * @param dailyInterBankLimit the dailyInterBankLimit to set
     */
    public void setDailyInterBankLimit(InterBankTransferLimit dailyInterBankLimit) {
        this.dailyInterBankLimit = dailyInterBankLimit;
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
     * @return the dailyOverseasBankLimit
     */
    public OverseasBankTransferLimit getDailyOverseasBankLimit() {
        return dailyOverseasBankLimit;
    }

    /**
     * @param dailyOverseasBankLimit the dailyOverseasBankLimit to set
     */
    public void setDailyOverseasBankLimit(OverseasBankTransferLimit dailyOverseasBankLimit) {
        this.dailyOverseasBankLimit = dailyOverseasBankLimit;
    }
    
}
