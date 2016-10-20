/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.fact.bank;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    
    private BigDecimal totalDepositAmount;
    private Double totalCreditCardAmount; 
    private Double totalLoanAmount;
    private Double allPortfolioAmount;
    private Double totalCardTransactionAmount;
    

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

    public Double getTotalDebtAmount() {
        return totalCreditCardAmount + totalLoanAmount;
    }

    public Double getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(Double totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public Double getTotalCreditCardAmount() {
        return totalCreditCardAmount;
    }

    public void setTotalCreditCardAmount(Double totalCreditCardAmount) {
        this.totalCreditCardAmount = totalCreditCardAmount;
    }

    public BigDecimal getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(BigDecimal totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public Double getAllPortfolioAmount() {
        return allPortfolioAmount;
    }

    public void setAllPortfolioAmount(Double allPortfolioAmount) {
        this.allPortfolioAmount = allPortfolioAmount;
    }

    public Double getTotalCardTransactionAmount() {
        return totalCardTransactionAmount;
    }

    public void setTotalCardTransactionAmount(Double totalCardTransactionAmount) {
        this.totalCardTransactionAmount = totalCardTransactionAmount;
    }
    
}
