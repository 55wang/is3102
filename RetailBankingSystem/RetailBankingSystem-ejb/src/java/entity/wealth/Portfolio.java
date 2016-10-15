/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.wealth;

import entity.customer.WealthManagementSubscriber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

    @OneToOne(cascade = {CascadeType.MERGE}, mappedBy = "portfolio")
    private InvestmentPlan executedInvestmentPlan;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "portfolio")
    private List<InvestmentPlan> investmentPlans = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.MERGE)
    private WealthManagementSubscriber wealthManagementSubscriber;

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

    public List<InvestmentPlan> getInvestmentPlans() {
        return investmentPlans;
    }

    public void setInvestmentPlans(List<InvestmentPlan> investmentPlans) {
        this.investmentPlans = investmentPlans;
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

}
