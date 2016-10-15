/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.bill;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Entity
public class BankEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String bankCode;
    private Boolean inFast;
    private EnumUtils.StatusType status = EnumUtils.StatusType.ACTIVE;

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
        if (!(object instanceof BankEntity)) {
            return false;
        }
        BankEntity other = (BankEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.bill.BankEntity[ id=" + id + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the bankCode
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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
     * @return the status
     */
    public EnumUtils.StatusType getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(EnumUtils.StatusType status) {
        this.status = status;
    }

    /**
     * @return the inFast
     */
    public Boolean getInFast() {
        return inFast;
    }

    /**
     * @param inFast the inFast to set
     */
    public void setInFast(Boolean inFast) {
        this.inFast = inFast;
    }
    
}
