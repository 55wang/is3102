/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.fact.bank;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Entity
public class BankFactTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(value = TemporalType.DATE)
    private Date creationDate;
    private EnumUtils.Month monthOfDate;
    private Integer yearOfDate;

    private Long totalDepositAcct;
    private Long totalActiveDepositAcct;
    private Long newDepositAcct; //for this month
    private Double totalDepositAmount;
    private Double totalDepositInterestAmount;

    private Long totalLoanAcct;
    private Long totalActiveLoanAcct;
    private Long newLoanAcct;
    private Double totalLoanAmount;
    private Double totalLoanInterestEarned;
    private Double totalLoanInterestUnearned;
    private Long numOfDefaultLoanAccount;

    private Long totalCardAcct; //only for credit card
    private Long totalActiveCardAcct;
    private Long newCardAcct;
    private Double totalCardAmount;
    private Double totalOutstandingAmount;
    private Long numOfBadCardAccount;

    private Long totalExecutedPortfolio;
    private Long newExecutedPortfolio;
    private Double totalPortfolioAmount;
    private Double totalPortfolioProfitAmount;
    
    private Double churnRate;
    private Double CaseResponseTime;

    //    private Double totalCardTransactionAmount;
    
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
        if (!(object instanceof BankFactTable)) {
            return false;
        }
        BankFactTable other = (BankFactTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.fact.DebtFacttable[ id=" + id + " ]";
    }

    public EnumUtils.Month getMonthOfDate() {
        return monthOfDate;
    }

    public void setMonthOfDate(EnumUtils.Month monthOfDate) {
        this.monthOfDate = monthOfDate;
    }

    public Integer getYearOfDate() {
        return yearOfDate;
    }

    public void setYearOfDate(Integer yearOfDate) {
        this.yearOfDate = yearOfDate;
    }

    public Double getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(Double totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public Double getTotalCardAmount() {
        return totalCardAmount;
    }

    public void setTotalCardAmount(Double totalCardAmount) {
        this.totalCardAmount = totalCardAmount;
    }

    public Double getTotalOutstandingAmount() {
        return totalOutstandingAmount;
    }

    public void setTotalOutstandingAmount(Double totalOutstandingAmount) {
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    public Double getTotalPortfolioAmount() {
        return totalPortfolioAmount;
    }

    public void setTotalPortfolioAmount(Double totalPortfolioAmount) {
        this.totalPortfolioAmount = totalPortfolioAmount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Double getTotalPortfolioProfitAmount() {
        return totalPortfolioProfitAmount;
    }

    public void setTotalPortfolioProfitAmount(Double totalPortfolioProfitAmount) {
        this.totalPortfolioProfitAmount = totalPortfolioProfitAmount;
    }

    public Double getTotalDepositInterestAmount() {
        return totalDepositInterestAmount;
    }

    public void setTotalDepositInterestAmount(Double totalDepositInterestAmount) {
        this.totalDepositInterestAmount = totalDepositInterestAmount;
    }

    public Long getTotalDepositAcct() {
        return totalDepositAcct;
    }

    public void setTotalDepositAcct(Long totalDepositAcct) {
        this.totalDepositAcct = totalDepositAcct;
    }

    public Long getTotalActiveDepositAcct() {
        return totalActiveDepositAcct;
    }

    public void setTotalActiveDepositAcct(Long totalActiveDepositAcct) {
        this.totalActiveDepositAcct = totalActiveDepositAcct;
    }

    public Long getNewDepositAcct() {
        return newDepositAcct;
    }

    public void setNewDepositAcct(Long newDepositAcct) {
        this.newDepositAcct = newDepositAcct;
    }

    public Double getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(Double totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public Long getTotalLoanAcct() {
        return totalLoanAcct;
    }

    public void setTotalLoanAcct(Long totalLoanAcct) {
        this.totalLoanAcct = totalLoanAcct;
    }

    public Long getTotalActiveLoanAcct() {
        return totalActiveLoanAcct;
    }

    public void setTotalActiveLoanAcct(Long totalActiveLoanAcct) {
        this.totalActiveLoanAcct = totalActiveLoanAcct;
    }

    public Long getNewLoanAcct() {
        return newLoanAcct;
    }

    public void setNewLoanAcct(Long newLoanAcct) {
        this.newLoanAcct = newLoanAcct;
    }

    public Double getTotalLoanInterestEarned() {
        return totalLoanInterestEarned;
    }

    public void setTotalLoanInterestEarned(Double totalLoanInterestEarned) {
        this.totalLoanInterestEarned = totalLoanInterestEarned;
    }

    public Double getTotalLoanInterestUnearned() {
        return totalLoanInterestUnearned;
    }

    public void setTotalLoanInterestUnearned(Double totalLoanInterestUnearned) {
        this.totalLoanInterestUnearned = totalLoanInterestUnearned;
    }

    public Long getNumOfDefaultLoanAccount() {
        return numOfDefaultLoanAccount;
    }

    public void setNumOfDefaultLoanAccount(Long numOfDefaultLoanAccount) {
        this.numOfDefaultLoanAccount = numOfDefaultLoanAccount;
    }

    public Long getTotalCardAcct() {
        return totalCardAcct;
    }

    public void setTotalCardAcct(Long totalCardAcct) {
        this.totalCardAcct = totalCardAcct;
    }

    public Long getTotalActiveCardAcct() {
        return totalActiveCardAcct;
    }

    public void setTotalActiveCardAcct(Long totalActiveCardAcct) {
        this.totalActiveCardAcct = totalActiveCardAcct;
    }

    public Long getNewCardAcct() {
        return newCardAcct;
    }

    public void setNewCardAcct(Long newCardAcct) {
        this.newCardAcct = newCardAcct;
    }

    public Long getNumOfBadCardAccount() {
        return numOfBadCardAccount;
    }

    public void setNumOfBadCardAccount(Long numOfBadCardAccount) {
        this.numOfBadCardAccount = numOfBadCardAccount;
    }

    public Long getTotalExecutedPortfolio() {
        return totalExecutedPortfolio;
    }

    public void setTotalExecutedPortfolio(Long totalExecutedPortfolio) {
        this.totalExecutedPortfolio = totalExecutedPortfolio;
    }

    public Long getNewExecutedPortfolio() {
        return newExecutedPortfolio;
    }

    public void setNewExecutedPortfolio(Long newExecutedPortfolio) {
        this.newExecutedPortfolio = newExecutedPortfolio;
    }

    public Double getChurnRate() {
        return churnRate;
    }

    public void setChurnRate(Double churnRate) {
        this.churnRate = churnRate;
    }

    public Double getCaseResponseTime() {
        return CaseResponseTime;
    }

    public void setCaseResponseTime(Double CaseResponseTime) {
        this.CaseResponseTime = CaseResponseTime;
    }

}
