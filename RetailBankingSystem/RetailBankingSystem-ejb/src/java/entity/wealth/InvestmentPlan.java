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
import server.utilities.EnumUtils.InvestmentPlanSatisfactionLevel;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import static server.utilities.CommonUtils.round;
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
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date executionDate;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date soldDate;
    private Integer amountOfInvestment; 
    private Double systemPredictReturn;
    private Integer systemPredictRisk;
    private InvestmentRiskLevel riskLevel;
    private List<FinancialInstrument> preferedFinancialInstrument = new ArrayList<>();
    private String remarks = "";
    private InvestmentPlanStatus status;
    private InvestmentPlanSatisfactionLevel satisfactionLevel;  
    private Double montylyETFfee = 0.0;
    private Double totalETFfee = 0.0;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "executedInvestmentPlan")
    private Portfolio portfolio;
    @ManyToOne(cascade = CascadeType.MERGE)
    private WealthManagementSubscriber wealthManagementSubscriber;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<FinancialInstrumentAndWeight> suggestedFinancialInstruments;
    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "ip")
    private InvestplanCommunication ic;

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

    public Double getSystemPredictReturn() {
        return round(systemPredictReturn, 3);
    }

    public void setSystemPredictReturn(Double systemPredictReturn) {
        this.systemPredictReturn = systemPredictReturn;
    }

    public Integer getSystemPredictRisk() {
        return systemPredictRisk;
    }

    public void setSystemPredictRisk(Integer systemPredictRisk) {
        this.systemPredictRisk = systemPredictRisk;
    }

    public InvestmentRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(InvestmentRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<FinancialInstrument> getPreferedFinancialInstrument() {
        return preferedFinancialInstrument;
    }

    public void setPreferedFinancialInstrument(List<FinancialInstrument> preferedFinancialInstrument) {
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

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Double getMontylyETFfee() {
        return montylyETFfee;
    }

    public void setMontylyETFfee(Double montylyETFfee) {
        this.montylyETFfee = montylyETFfee;
    }

    public Double getTotalETFfee() {
        return totalETFfee;
    }

    public void setTotalETFfee(Double totalETFfee) {
        this.totalETFfee = totalETFfee;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Date getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(Date soldDate) {
        this.soldDate = soldDate;
    }

    public InvestplanCommunication getIc() {
        return ic;
    }

    public void setIc(InvestplanCommunication ic) {
        this.ic = ic;
    }
}
