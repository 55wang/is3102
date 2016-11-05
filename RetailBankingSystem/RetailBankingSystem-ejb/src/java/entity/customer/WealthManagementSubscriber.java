/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import entity.staff.StaffAccount;
import entity.wealth.InvestmentPlan;
import entity.wealth.InvestplanCommunication;
import entity.wealth.Portfolio;
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
import server.utilities.EnumUtils.RiskToleranceLevel;

/**
 *
 * @author VIN-S
 */
@Entity
public class WealthManagementSubscriber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer riskToleranceScore = null;
    private RiskToleranceLevel riskToleranceLevel = null;
    
    private Double monthlyAdvisoryFee = 0.0;
    private Double accumulatedAdvisoryFee = 0.0;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date advisoryFeeClearDate;
   
    @OneToOne(cascade = CascadeType.MERGE)
    private MainAccount mainAccount;
    @ManyToOne(cascade = CascadeType.MERGE)
    private StaffAccount relationshipManager;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "wealthManagementSubscriber")
    private List<InvestmentPlan> investmentPlans = new ArrayList<>();
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "wealthManagementSubscriber")
    private List<Portfolio> portfolios = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "wms")
    private List<InvestplanCommunication> investplanCommunications = new ArrayList<>();
    
    public Double getTotalPortfolioValue() {
        Double totalBalance = 0.0;
        for (Portfolio p: portfolios) {
            totalBalance += p.getTotalCurrentValue();
        }
        return totalBalance;
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
        if (!(object instanceof WealthManagementSubscriber)) {
            return false;
        }
        WealthManagementSubscriber other = (WealthManagementSubscriber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.customer.WealthManagementSubscriber[ id=" + id + " ]";
    }

    public Integer getRiskToleranceScore() {
        return riskToleranceScore;
    }

    public void setRiskToleranceScore(Integer riskToleranceScore) {
        this.riskToleranceScore = riskToleranceScore;
    }

    public RiskToleranceLevel getRiskToleranceLevel() {
        return riskToleranceLevel;
    }

    public void setRiskToleranceLevel(RiskToleranceLevel riskToleranceLevel) {
        this.riskToleranceLevel = riskToleranceLevel;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public StaffAccount getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(StaffAccount relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public List<InvestmentPlan> getInvestmentPlans() {
        return investmentPlans;
    }

    public void setInvestmentPlans(List<InvestmentPlan> investmentPlans) {
        this.investmentPlans = investmentPlans;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }
    
    public Double getMonthlyAdvisoryFee() {
        return monthlyAdvisoryFee;
    }

    public void setMonthlyAdvisoryFee(Double monthlyAdvisoryFee) {
        this.monthlyAdvisoryFee = monthlyAdvisoryFee;
    }

    public Double getAccumulatedAdvisoryFee() {
        return accumulatedAdvisoryFee;
    }

    public void setAccumulatedAdvisoryFee(Double accumulatedAdvisoryFee) {
        this.accumulatedAdvisoryFee = accumulatedAdvisoryFee;
    }

    public Date getAdvisoryFeeClearDate() {
        return advisoryFeeClearDate;
    }

    public void setAdvisoryFeeClearDate(Date advisoryFeeClearDate) {
        this.advisoryFeeClearDate = advisoryFeeClearDate;
    }
}
