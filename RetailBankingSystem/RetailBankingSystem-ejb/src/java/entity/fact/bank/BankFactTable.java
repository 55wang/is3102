/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.fact.bank;

import java.io.Serializable;
import java.math.BigDecimal;
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

    private Integer totalDepositAcct;
    private Integer totalActiveDepositAcct;
    private Integer newDepositAcct; //for this month
    private BigDecimal totalDepositAmount;
    private Double avgDepositInterestRate;
    private Double totalDepositInterestAmount;

    private Integer totalLoanAcct;
    private Integer totalActiveLoanAcct;
    private Integer newLoanAcct;
    private Double totalLoanAmount;
    private Double avgLoanInterestRate;
    private Double totalLoanInterestAmount;
    private Integer numOfDefaultLoanAccount;

    private Integer totalCardAcct; //only for credit card
    private Integer totalActiveCardAcct;
    private Integer newCardAcct;
    private Double totalCardAmount;
    private Double totalOutstandingAmount;
    private Integer numOfBadCardAccount;

    private Integer totalExecutedPortfolio;
    private Integer newExecutedPortfolio;
    private Double totalPortfolioAmount;
    private Double totalPortfolioProfitAmount;

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

    public Integer getTotalDepositAcct() {
        return totalDepositAcct;
    }

    public void setTotalDepositAcct(Integer totalDepositAcct) {
        this.totalDepositAcct = totalDepositAcct;
    }

    public Integer getTotalActiveDepositAcct() {
        return totalActiveDepositAcct;
    }

    public void setTotalActiveDepositAcct(Integer totalActiveDepositAcct) {
        this.totalActiveDepositAcct = totalActiveDepositAcct;
    }

    public Integer getNewDepositAcct() {
        return newDepositAcct;
    }

    public void setNewDepositAcct(Integer newDepositAcct) {
        this.newDepositAcct = newDepositAcct;
    }

    public BigDecimal getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(BigDecimal totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public Integer getTotalLoanAcct() {
        return totalLoanAcct;
    }

    public void setTotalLoanAcct(Integer totalLoanAcct) {
        this.totalLoanAcct = totalLoanAcct;
    }

    public Integer getTotalActiveLoanAcct() {
        return totalActiveLoanAcct;
    }

    public void setTotalActiveLoanAcct(Integer totalActiveLoanAcct) {
        this.totalActiveLoanAcct = totalActiveLoanAcct;
    }

    public Integer getNewLoanAcct() {
        return newLoanAcct;
    }

    public void setNewLoanAcct(Integer newLoanAcct) {
        this.newLoanAcct = newLoanAcct;
    }

    public Double getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(Double totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public Integer getNumOfDefaultLoanAccount() {
        return numOfDefaultLoanAccount;
    }

    public void setNumOfDefaultLoanAccount(Integer numOfDefaultLoanAccount) {
        this.numOfDefaultLoanAccount = numOfDefaultLoanAccount;
    }

    public Integer getTotalCardAcct() {
        return totalCardAcct;
    }

    public void setTotalCardAcct(Integer totalCardAcct) {
        this.totalCardAcct = totalCardAcct;
    }

    public Integer getTotalActiveCardAcct() {
        return totalActiveCardAcct;
    }

    public void setTotalActiveCardAcct(Integer totalActiveCardAcct) {
        this.totalActiveCardAcct = totalActiveCardAcct;
    }

    public Integer getNewCardAcct() {
        return newCardAcct;
    }

    public void setNewCardAcct(Integer newCardAcct) {
        this.newCardAcct = newCardAcct;
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

    public Integer getNumOfBadCardAccount() {
        return numOfBadCardAccount;
    }

    public void setNumOfBadCardAccount(Integer numOfBadCardAccount) {
        this.numOfBadCardAccount = numOfBadCardAccount;
    }

    public Integer getTotalExecutedPortfolio() {
        return totalExecutedPortfolio;
    }

    public void setTotalExecutedPortfolio(Integer totalExecutedPortfolio) {
        this.totalExecutedPortfolio = totalExecutedPortfolio;
    }

    public Integer getNewExecutedPortfolio() {
        return newExecutedPortfolio;
    }

    public void setNewExecutedPortfolio(Integer newExecutedPortfolio) {
        this.newExecutedPortfolio = newExecutedPortfolio;
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

    public Double getAvgDepositInterestRate() {
        return avgDepositInterestRate;
    }

    public void setAvgDepositInterestRate(Double avgDepositInterestRate) {
        this.avgDepositInterestRate = avgDepositInterestRate;
    }

    public Double getTotalDepositInterestAmount() {
        return totalDepositInterestAmount;
    }

    public void setTotalDepositInterestAmount(Double totalDepositInterestAmount) {
        this.totalDepositInterestAmount = totalDepositInterestAmount;
    }

    public Double getAvgLoanInterestRate() {
        return avgLoanInterestRate;
    }

    public void setAvgLoanInterestRate(Double avgLoanInterestRate) {
        this.avgLoanInterestRate = avgLoanInterestRate;
    }

    public Double getTotalLoanInterestAmount() {
        return totalLoanInterestAmount;
    }

    public void setTotalLoanInterestAmount(Double totalLoanInterestAmount) {
        this.totalLoanInterestAmount = totalLoanInterestAmount;
    }

}
