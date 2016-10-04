/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author qiuxiaqing
 */
@Entity
public class LoanRepaymentRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date transactionDate;

    private double beginningBalance;
    private double remainingBalance;
    private double paymentAmount;
    private double interestAccrued;
    private double cumulativeInterestAccrued = 0;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanAccount loanAccount;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoanRepaymentRecord)) {
            return false;
        }
        LoanRepaymentRecord other = (LoanRepaymentRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.loan.LoanRepaymentRecord[ id=" + id + " ]";
    }

    public LoanAccount getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(LoanAccount loanAccount) {
        this.loanAccount = loanAccount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(double beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public double getInterestAccrued() {
        return interestAccrued;
    }

    public void setInterestAccrued(double interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public double getCumulativeInterestAccrued() {
        return cumulativeInterestAccrued;
    }

    public void setCumulativeInterestAccrued(double cumulativeInterestAccrued) {
        this.cumulativeInterestAccrued = cumulativeInterestAccrued;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
