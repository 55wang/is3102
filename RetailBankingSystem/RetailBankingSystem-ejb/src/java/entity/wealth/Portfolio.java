/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.wealth;

import entity.customer.WealthManagementSubscriber;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils.PortfolioStatus;

/**
 *
 * @author VIN-S
 */
@Entity
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    private PortfolioStatus status;

    @OneToOne(cascade = CascadeType.MERGE, optional = false)
    private InvestmentPlan executedInvestmentPlan;
    @ManyToOne(cascade = CascadeType.MERGE)
    private WealthManagementSubscriber wealthManagementSubscriber;
    

    public Double getTotalBuyingValue() {
        Double totalValue = 0.0;

        for(int i = 0;i < executedInvestmentPlan.getSuggestedFinancialInstruments().size();i++)
            totalValue += executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare() * executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getBuyingNumberOfShare();

        return totalValue;
    }

    public Double getTotalCurrentValue() {
        Double totalValue = 0.0;

        for(int i = 0;i < executedInvestmentPlan.getSuggestedFinancialInstruments().size();i++)
            totalValue += executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getCurrentValuePerShare()* executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getBuyingNumberOfShare();
        return totalValue;
    }
    
    public Double getPercentageChange() {
        return getTotalCurrentValue() / getTotalBuyingValue() * 100;
    }

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
        if (!(object instanceof Portfolio)) {
            return false;
        }
        Portfolio other = (Portfolio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.wealth.Portfolio[ id=" + id + " ]";
    }

    public InvestmentPlan getExecutedInvestmentPlan() {
        return executedInvestmentPlan;
    }

    public void setExecutedInvestmentPlan(InvestmentPlan executedInvestmentPlan) {
        this.executedInvestmentPlan = executedInvestmentPlan;
    }

    public WealthManagementSubscriber getWealthManagementSubscriber() {
        return wealthManagementSubscriber;
    }

    public void setWealthManagementSubscriber(WealthManagementSubscriber wealthManagementSubscriber) {
        this.wealthManagementSubscriber = wealthManagementSubscriber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PortfolioStatus getStatus() {
        return status;
    }

    public void setStatus(PortfolioStatus status) {
        this.status = status;
    }

}
