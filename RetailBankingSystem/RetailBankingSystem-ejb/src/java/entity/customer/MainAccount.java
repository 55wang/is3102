/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import entity.bill.BillingOrg;
import entity.bill.Payee;
import entity.card.account.CreditCardAccount;
import entity.card.order.CreditCardOrder;
import entity.common.AuditLog;
import entity.dams.account.DepositAccount;
import entity.dams.account.MobileAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
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
import server.utilities.EnumUtils.StatusType;

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
    
    // info
    @Column(unique = true)
    private String userID;
    private String password;
    private StatusType status;
    
    // mappings
    @OneToOne(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private Customer customer;
    @OneToOne(cascade = {CascadeType.PERSIST}, mappedBy = "mainAccount")
    private TransferLimits transferLimits = new TransferLimits(this);
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<DepositAccount> bankAcounts = new ArrayList<>(); 
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<LoanAccount> loanAccounts = new ArrayList<>(); 
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<Payee> payees = new ArrayList<>(); 
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<BillingOrg> billingOrgs = new ArrayList<>(); 
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<CreditCardAccount> creditCardAccounts= new ArrayList<>(); 
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<AuditLog> auditLog = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<CustomerCase> cases = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<CreditCardOrder> creditCardOrder = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private List<LoanApplication> loanApplications = new ArrayList<>();
    @OneToOne(cascade = {CascadeType.MERGE}, mappedBy = "mainAccount")
    private WealthManagementSubscriber wealthManagementSubscriber;
    
    public void addDepositAccount(DepositAccount da) {
        this.bankAcounts.add(da);
    }
    
    public void addLoanAccount(LoanAccount la) {
        this.loanAccounts.add(la);
    }
    
    public void addLoanApplication(LoanApplication la) {
        this.loanApplications.add(la);
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

    public List<CreditCardAccount> getCreditCardAccounts() {
        return creditCardAccounts;
    }

    public void setCreditCardAccounts(List<CreditCardAccount> creditCardAccounts) {
        this.creditCardAccounts = creditCardAccounts;
    }

    public List<CreditCardOrder> getCreditCardOrder() {
        return creditCardOrder;
    }

    public void setCreditCardOrder(List<CreditCardOrder> creditCardOrder) {
        this.creditCardOrder = creditCardOrder;
    }

    public WealthManagementSubscriber getWealthManagementSubscriber() {
        return wealthManagementSubscriber;
    }

    public void setWealthManagementSubscriber(WealthManagementSubscriber wealthManagementSubscriber) {
        this.wealthManagementSubscriber = wealthManagementSubscriber;
    }

    /**
     * @return the payees
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * @param payees the payees to set
     */
    public void setPayees(List<Payee> payees) {
        this.payees = payees;
    }

    /**
     * @return the transferLimits
     */
    public TransferLimits getTransferLimits() {
        return transferLimits;
    }

    /**
     * @param transferLimits the transferLimits to set
     */
    public void setTransferLimits(TransferLimits transferLimits) {
        this.transferLimits = transferLimits;
    }

    /**
     * @return the billingOrgs
     */
    public List<BillingOrg> getBillingOrgs() {
        return billingOrgs;
    }

    /**
     * @param billingOrgs the billingOrgs to set
     */
    public void setBillingOrgs(List<BillingOrg> billingOrgs) {
        this.billingOrgs = billingOrgs;
    }

    /**
     * @return the loanAccounts
     */
    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    /**
     * @param loanAccounts the loanAccounts to set
     */
    public void setLoanAccounts(List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
    }

    /**
     * @return the loanApplications
     */
    public List<LoanApplication> getLoanApplications() {
        return loanApplications;
    }

    /**
     * @param loanApplications the loanApplications to set
     */
    public void setLoanApplications(List<LoanApplication> loanApplications) {
        this.loanApplications = loanApplications;
    }
}
