/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.counter;

import entity.staff.StaffAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author leiyang
 */
@Entity
public class TellerCounter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(precision=19, scale=4)
    private BigDecimal currentCash = BigDecimal.ZERO;
    
    @ManyToOne(cascade={CascadeType.MERGE})
    private StaffAccount staffAccount;
    
    public void addCash(BigDecimal c) {
        currentCash = currentCash.add(c);
    }
    
    public void removeCash(BigDecimal c) {
        currentCash = currentCash.subtract(c);
    }
    
    public Boolean hasEnoughCash(BigDecimal c) {
        return currentCash.compareTo(c) >= 0;
    }

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
        if (!(object instanceof TellerCounter)) {
            return false;
        }
        TellerCounter other = (TellerCounter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.counter.TellerCounter[ id=" + id + " ]";
    }

    /**
     * @return the currentCash
     */
    public BigDecimal getCurrentCash() {
        return currentCash;
    }

    /**
     * @param currentCash the currentCash to set
     */
    public void setCurrentCash(BigDecimal currentCash) {
        this.currentCash = currentCash;
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
    
}
