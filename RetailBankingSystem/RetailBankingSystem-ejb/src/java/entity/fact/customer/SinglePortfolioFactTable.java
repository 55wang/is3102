/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.fact.customer;

import entity.customer.Customer;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Entity
public class SinglePortfolioFactTable implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(value = TemporalType.DATE)
    private Date creationDate;
    
    //ManyToOne map to other dimension table
    //customertable
    //portfoliotable
    @ManyToOne(optional = false)
    private Customer customer;
    @ManyToOne(optional = false)
    private Portfolio portfolio;
    
    //select all portfolio table to populate this fact table
    //facts
    private Double totalCurrentValue;
    private Double totalBuyingValue;

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
        if (!(object instanceof SinglePortfolioFactTable)) {
            return false;
        }
        SinglePortfolioFactTable other = (SinglePortfolioFactTable) object;
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

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Double getTotalCurrentValue() {
        return totalCurrentValue;
    }

    public void setTotalCurrentValue(Double totalCurrentValue) {
        this.totalCurrentValue = totalCurrentValue;
    }

    public Double getTotalBuyingValue() {
        return totalBuyingValue;
    }

    public void setTotalBuyingValue(Double totalBuyingValue) {
        this.totalBuyingValue = totalBuyingValue;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
