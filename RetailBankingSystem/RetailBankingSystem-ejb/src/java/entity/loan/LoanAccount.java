/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import entity.customer.Customer;
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
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanProduct loanProduct;
    private double monthlyInstallment;
    private double outstandingPrincipal;
    private double overduePayment;
    private double principal;
    private Integer paymentDate;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private StaffAccount loanOfficer;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Customer customer;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private CustomerDepositAccount depositAccount;
    private LoanAccountStatus loanAccountStatus;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "loanAccount")
    private List<LoanRepaymentRecord> loanRepaymentRecords = new ArrayList<>();
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

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

    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }

    public double getOutstandingPrincipal() {
        return outstandingPrincipal;
    }

    public void setOutstandingPrincipal(double outstandingPrincipal) {
        this.outstandingPrincipal = outstandingPrincipal;
    }

    public double getOverduePayment() {
        return overduePayment;
    }

    public void setOverduePayment(double overduePayment) {
        this.overduePayment = overduePayment;
    }

    public Integer getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Integer paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public StaffAccount getLoanOfficer() {
        return loanOfficer;
    }

    public void setLoanOfficer(StaffAccount loanOfficer) {
        this.loanOfficer = loanOfficer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

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

}
