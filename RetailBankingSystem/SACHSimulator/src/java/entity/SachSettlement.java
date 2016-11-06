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
    private String bankCode;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    @Column(precision = 19, scale = 4)
    private BigDecimal amount;
    private String name;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.bankCode);
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
        if (!Objects.equals(this.bankCode, other.bankCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SachSettlement{" + "bankCode=" + bankCode + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
