/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author qiuxiaqing
 */
@Entity
public class VisaSettlement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fromBankCode;
    private String toBankCode;
    private Double amount;
    private String toBankName;
    private String fromBankName;

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
        if (!(object instanceof VisaSettlement)) {
            return false;
        }
        VisaSettlement other = (VisaSettlement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VisaSettlement[ id=" + id + " ]";
    }

    /**
     * @return the fromBankCode
     */
    public String getFromBankCode() {
        return fromBankCode;
    }

    /**
     * @param fromBankCode the fromBankCode to set
     */
    public void setFromBankCode(String fromBankCode) {
        this.fromBankCode = fromBankCode;
    }

    /**
     * @return the toBankCode
     */
    public String getToBankCode() {
        return toBankCode;
    }

    /**
     * @param toBankCode the toBankCode to set
     */
    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @return the toBankName
     */
    public String getToBankName() {
        return toBankName;
    }

    /**
     * @param toBankName the toBankName to set
     */
    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    /**
     * @return the fromBankName
     */
    public String getFromBankName() {
        return fromBankName;
    }

    /**
     * @param fromBankName the fromBankName to set
     */
    public void setFromBankName(String fromBankName) {
        this.fromBankName = fromBankName;
    }

}
