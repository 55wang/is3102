/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils.ChequeStatus;

/**
 *
 * @author leiyang
 */
@Entity
public class Cheque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private ChequeStatus status = ChequeStatus.UNTOUCHED;
    @Column(precision=12, scale=2)
    private BigDecimal amount = BigDecimal.ZERO;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date receivedDate = null;
    @ManyToOne(cascade={CascadeType.MERGE})
    private CustomerDepositAccount account;

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
        if (!(object instanceof Cheque)) {
            return false;
        }
        Cheque other = (Cheque) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.dams.account.Cheque[ id=" + id + " ]";
    }

    /**
     * @return the account
     */
    public CustomerDepositAccount getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(CustomerDepositAccount account) {
        this.account = account;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the receivedDate
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the status
     */
    public ChequeStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ChequeStatus status) {
        this.status = status;
    }
    
}
