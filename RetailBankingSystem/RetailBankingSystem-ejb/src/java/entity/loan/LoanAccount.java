/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

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
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.LoanAccountStatus;

/**
 *
 * @author leiyang
 */
@Entity
public class LoanAccount implements Serializable {
    
//http://stackoverflow.com/questions/15638248/save-image-file-in-specific-directory-jsf-primefaces-project
//http://stackoverflow.com/questions/3428039/download-a-file-with-jsf
    
    @Id
    private String accountNumber;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date closeDate = new Date();
    
    // info
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date maturityDate; // end date
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date paymentStartDate; // start date
    private Integer tenure;
    // payment day in month for example, 
    // every 23rd of the month is the payment due date
    private Integer paymentDate; 
    private Integer currentPeriod = 0;
    private Double monthlyInstallment; // monthly payment
    private Double outstandingPrincipal; // 
    private Double overduePayment = 0.0; // late payment
    private Double amountPaidBeforeDueDate = 0.0; // early payment
    private Double principal; // total loan amount
    // works as loan order too.
    private LoanAccountStatus loanAccountStatus = EnumUtils.LoanAccountStatus.NEW;
    // list of files
    
    // mapping
    // TODO: Assign algorithm
    // assigned loan officer to take note
    @ManyToOne(cascade = {CascadeType.MERGE})
    private StaffAccount loanOfficer;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;
    // the deposit account that will get the money
    @ManyToOne(cascade = {CascadeType.MERGE})
    private CustomerDepositAccount depositAccount;
    // the rules of the loan
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanProduct loanProduct;
    // previous payment record
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "loanAccount")
    private List<LoanRepaymentRecord> loanRepaymentRecords = new ArrayList<>();
    // overview of the breakdown
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "loanAccount")
    private List<LoanPaymentBreakdown> loanPaymentBreakdown = new ArrayList<>();
    @OneToOne(cascade = {CascadeType.MERGE}, mappedBy = "loanAccount")
    private LoanAdjustmentApplication loanAdjustmentApplication;
    
    public Integer tenureInMonth() {
        return tenure * 12 - currentPeriod;
    }
    
    public void addRepaymentRecord(LoanRepaymentRecord record) {
        loanRepaymentRecords.add(record);
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

    public Date getPaymentStartDate() {
        return paymentStartDate;
    }

    public void setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
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

    public List<LoanPaymentBreakdown> getLoanPaymentBreakdown() {
        return loanPaymentBreakdown;
    }

    public void setLoanPaymentBreakdown(List<LoanPaymentBreakdown> loanPaymentBreakdown) {
        this.loanPaymentBreakdown = loanPaymentBreakdown;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the paymentDate
     */
    public Integer getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(Integer paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the tenure
     */
    public Integer getTenure() {
        return tenure;
    }

    /**
     * @param tenure the tenure to set
     */
    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    /**
     * @return the currentPeriod
     */
    public Integer getCurrentPeriod() {
        return currentPeriod;
    }

    /**
     * @param currentPeriod the currentPeriod to set
     */
    public void setCurrentPeriod(Integer currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    /**
     * @return the amountPaidBeforeDueDate
     */
    public Double getAmountPaidBeforeDueDate() {
        return amountPaidBeforeDueDate;
    }

    /**
     * @param amountPaidBeforeDueDate the amountPaidBeforeDueDate to set
     */
    public void setAmountPaidBeforeDueDate(Double amountPaidBeforeDueDate) {
        this.amountPaidBeforeDueDate = amountPaidBeforeDueDate;
    }

    /**
     * @return the loanAdjustmentApplication
     */
    public LoanAdjustmentApplication getLoanAdjustmentApplication() {
        return loanAdjustmentApplication;
    }

    /**
     * @param loanAdjustmentApplication the loanAdjustmentApplication to set
     */
    public void setLoanAdjustmentApplication(LoanAdjustmentApplication loanAdjustmentApplication) {
        this.loanAdjustmentApplication = loanAdjustmentApplication;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

}
