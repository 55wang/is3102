/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author wang
 */
@Entity
public class MainAccount implements Serializable {

    public List<BankAccount> getBankAcounts() {
        return bankAcounts;
    }

    public void setBankAcounts(List<BankAccount> bankAcounts) {
        this.bankAcounts = bankAcounts;
    }

    public List<AuditLog> getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(List<AuditLog> auditLog) {
        this.auditLog = auditLog;
    }

    public enum StatusType {
        ACTIVE{
            public String toString() {
                return "ACTIVE";
            }
        }, 
        PENDING{
            public String toString() {
                return "PENDING";
            }
        }, 
        FREEZE{
            public String toString() {
                return "FREEZE";
            }
        }, 
        CLOSED{
            public String toString() {
                return "CLOSED";
            }
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userID;
    private String password;
    private StatusType status;
    @OneToOne(cascade = {CascadeType.PERSIST}, mappedBy = "mainAccount")
    private Customer customer;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "mainAccount")
    private List<BankAccount> bankAcounts = new ArrayList<BankAccount>(); 
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "mainAccount")
    private List<AuditLog> auditLog = new ArrayList<AuditLog>();

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        if (!(object instanceof MainAccount)) {
            return false;
        }
        MainAccount other = (MainAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Account[ id=" + id + " ]";
    }

}
