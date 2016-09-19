/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import entity.card.account.CreditCardAccount;
import entity.common.AuditLog;
import entity.dams.account.DepositAccount;
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
import utils.EnumUtils.StatusType;

/**
 *
 * @author wang
 */
@Entity
public class MainAccount implements Serializable {

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
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<DepositAccount> bankAcounts = new ArrayList<DepositAccount>(); 
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "mainAccount")
    private List<CreditCardAccount> creditCardAccounts= new ArrayList<CreditCardAccount>(); 
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "mainAccount")
    private List<AuditLog> auditLog = new ArrayList<AuditLog>();
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "mainAccount")
    private List<CustomerCase> cases = new ArrayList<CustomerCase>();
    
    public void addDepositAccount(DepositAccount da) {
        this.bankAcounts.add(da);
    }
    
    public void addCase(CustomerCase cc){
        this.cases.add(cc);
    }

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

    public List<DepositAccount> getBankAcounts() {
        return bankAcounts;
    }

    public void setBankAcounts(List<DepositAccount> bankAcounts) {
        this.bankAcounts = bankAcounts;
    }

    public List<AuditLog> getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(List<AuditLog> auditLog) {
        this.auditLog = auditLog;
    }

    public List<CustomerCase> getCases() {
        return cases;
    }

    public void setCases(List<CustomerCase> cases) {
        this.cases = cases;
    }
}
