package entity.fact.customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import entity.customer.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wang
 */
@Entity
public class CustomerFactTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    
    //ManyToOne map to other dimension table
    //customertable
    //portfoliotable
    @ManyToOne
    private Customer customer = new Customer();
    
    //select all portfolio table for that CUSTOMER ONlY to populate this fact table
    //facts
    private Double totalAllPortfoliosCurrentValue; //for customer, only all his portfolio
    private Double totalAllPortfoliosBuyingValue;
    private Double totalAllPortfoliosPercentageChange;
    
    private BigDecimal totalDepositAmount; //for customer, his own total deposit
    private Double totalLoanAmount;         //loan
    private Double totalCreditCardAmount;  //creditcardamount
    
    //transaction fact table of customer
    

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
        if (!(object instanceof CustomerFactTable)) {
            return false;
        }
        CustomerFactTable other = (CustomerFactTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.fact.CustomerFactTable[ id=" + id + " ]";
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getTotalAllPortfoliosCurrentValue() {
        return totalAllPortfoliosCurrentValue;
    }

    public void setTotalAllPortfoliosCurrentValue(Double totalAllPortfoliosCurrentValue) {
        this.totalAllPortfoliosCurrentValue = totalAllPortfoliosCurrentValue;
    }

    public Double getTotalAllPortfoliosBuyingValue() {
        return totalAllPortfoliosBuyingValue;
    }

    public void setTotalAllPortfoliosBuyingValue(Double totalAllPortfoliosBuyingValue) {
        this.totalAllPortfoliosBuyingValue = totalAllPortfoliosBuyingValue;
    }

    public Double getTotalAllPortfoliosPercentageChange() {
        return totalAllPortfoliosPercentageChange;
    }

    public void setTotalAllPortfoliosPercentageChange(Double totalAllPortfoliosPercentageChange) {
        this.totalAllPortfoliosPercentageChange = totalAllPortfoliosPercentageChange;
    }

    public BigDecimal getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(BigDecimal totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
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

    public Double getTotalDebtAmount() {
        return totalCreditCardAmount + totalLoanAmount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

}
