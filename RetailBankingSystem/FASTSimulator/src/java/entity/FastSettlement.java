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

/**
 *
 * @author qiuxiaqing
 */
@Entity
public class FastSettlement implements Serializable {

    @Id
    private String bankCode;
    private String name;
    @Column(precision = 30, scale = 20)
    private BigDecimal amount;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

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
        final FastSettlement other = (FastSettlement) obj;
        if (!Objects.equals(this.bankCode, other.bankCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FastSettlement{" + "bankCode=" + bankCode + '}';
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
