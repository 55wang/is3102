/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author qiuxiaqing
 */
@Entity
@XmlRootElement
public class SachSettlement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromBankCode;
    private String toBankCode;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    @Column(precision = 19, scale = 4)
    private BigDecimal amount;
    private String toBankName;
    private String fromBankName;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SachSettlement other = (SachSettlement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SachSettlement{" + "id=" + id + '}';
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
