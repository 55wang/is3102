/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils.LoanAccountStatus;

/**
 *
 * @author qiuxiaqing
 */
@Entity
public class LoanAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String accountNumber;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date maturityDate;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    private Double monthlyInstallment;
    private Double outstandingPrincipal;
    private Double overduePayment;
    private Double principal;
    private Integer paymentDate;
    private LoanAccountStatus loanAccountStatus;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private StaffAccount loanOfficer;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private CustomerDepositAccount depositAccount;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanProduct loanProduct;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "loanAccount")
    private List<LoanRepaymentRecord> loanRepaymentRecords = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "loanAccount")
    private List<LoanPaymentBreakdown> loanPaymentBreakdown = new ArrayList<>();

    @Override
    public String toString() {
        return "LoanAccount{" + "accountNumber=" + accountNumber + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.accountNumber);
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
        final LoanAccount other = (LoanAccount) obj;
        if (!Objects.equals(this.accountNumber, other.accountNumber)) {
            return false;
        }
        return true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LoanProduct getLoanProduct() {
        return loanProduct;
    }

    public void setLoanProduct(LoanProduct loanProduct) {
        this.loanProduct = loanProduct;
    }

    public Double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(Double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }

    public Double getOutstandingPrincipal() {
        return outstandingPrincipal;
    }

    public void setOutstandingPrincipal(Double outstandingPrincipal) {
        this.outstandingPrincipal = outstandingPrincipal;
    }

    public Double getOverduePayment() {
        return overduePayment;
    }

    public void setOverduePayment(Double overduePayment) {
        this.overduePayment = overduePayment;
    }

    public Integer getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Integer paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public StaffAccount getLoanOfficer() {
        return loanOfficer;
    }

    public void setLoanOfficer(StaffAccount loanOfficer) {
        this.loanOfficer = loanOfficer;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public CustomerDepositAccount getDepositAccount() {
        return depositAccount;
    }

    public void setDepositAccount(CustomerDepositAccount depositAccount) {
        this.depositAccount = depositAccount;
    }

    public LoanAccountStatus getLoanAccountStatus() {
        return loanAccountStatus;
    }

    public void setLoanAccountStatus(LoanAccountStatus loanAccountStatus) {
        this.loanAccountStatus = loanAccountStatus;
    }

    public List<LoanRepaymentRecord> getLoanRepaymentRecords() {
        return loanRepaymentRecords;
    }

    public void setLoanRepaymentRecords(List<LoanRepaymentRecord> loanRepaymentRecords) {
        this.loanRepaymentRecords = loanRepaymentRecords;
    }
    
    public List<LoanPaymentBreakdown> getLoanPaymentBreakdown(){
        return loanPaymentBreakdown;
    }
    
    public void setLoanPaymentBreakdown(List<LoanPaymentBreakdown> loanPaymentBreakdown){
        this.loanPaymentBreakdown=loanPaymentBreakdown;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
