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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.InvestmentPlanSatisfactionLevel;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import entity.wealth.FinancialInstrumentAndWeight;
import server.utilities.EnumUtils.InvestmentRiskLevel;

/**
 *
 * @author VIN-S
 */
@Entity
public class InvestmentPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    private Integer amountOfInvestment;  
    private Double customerExpectedReturn;
    private Double systemPredictReturn;
    private Double systemPredictRisk;
    private InvestmentRiskLevel riskLevel;
    private List<EnumUtils.FinancialInstrumentClass> preferedFinancialInstrument = new ArrayList<>();
    private String remarks;
    private InvestmentPlanStatus status;
    private InvestmentPlanSatisfactionLevel satisfactionLevel;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    private WealthManagementSubscriber wealthManagementSubscriber;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<FinancialInstrumentAndWeight> suggestedFinancialInstruments;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Portfolio portfolio;

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
        if (!(object instanceof InvestmentPlan)) {
            return false;
        }
        InvestmentPlan other = (InvestmentPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.wealth.InvestmentPlan[ id=" + id + " ]";
    }

    public Integer getAmountOfInvestment() {
        return amountOfInvestment;
    }

    public void setAmountOfInvestment(Integer amountOfInvestment) {
        this.amountOfInvestment = amountOfInvestment;
    }

    public Double getCustomerExpectedReturn() {
        return customerExpectedReturn;
    }

    public void setCustomerExpectedReturn(Double customerExpectedReturn) {
        this.customerExpectedReturn = customerExpectedReturn;
    }

    public Double getSystemPredictReturn() {
        return systemPredictReturn;
    }

    public void setSystemPredictReturn(Double systemPredictReturn) {
        this.systemPredictReturn = systemPredictReturn;
    }

    public Double getSystemPredictRisk() {
        return systemPredictRisk;
    }

    public void setSystemPredictRisk(Double systemPredictRisk) {
        this.systemPredictRisk = systemPredictRisk;
    }

    public InvestmentRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(InvestmentRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<EnumUtils.FinancialInstrumentClass> getPreferedFinancialInstrument() {
        return preferedFinancialInstrument;
    }

    public void setPreferedFinancialInstrument(List<EnumUtils.FinancialInstrumentClass> preferedFinancialInstrument) {
        this.preferedFinancialInstrument = preferedFinancialInstrument;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public WealthManagementSubscriber getWealthManagementSubscriber() {
        return wealthManagementSubscriber;
    }

    public void setWealthManagementSubscriber(WealthManagementSubscriber wealthManagementSubscriber) {
        this.wealthManagementSubscriber = wealthManagementSubscriber;
    }

    public List<FinancialInstrumentAndWeight> getSuggestedFinancialInstruments() {
        return suggestedFinancialInstruments;
    }

    public void setSuggestedFinancialInstruments(List<FinancialInstrumentAndWeight> suggestedFinancialInstruments) {
        this.suggestedFinancialInstruments = suggestedFinancialInstruments;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public InvestmentPlanStatus getStatus() {
        return status;
    }

    public void setStatus(InvestmentPlanStatus status) {
        this.status = status;
    }

    public InvestmentPlanSatisfactionLevel getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public void setSatisfactionLevel(InvestmentPlanSatisfactionLevel satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
