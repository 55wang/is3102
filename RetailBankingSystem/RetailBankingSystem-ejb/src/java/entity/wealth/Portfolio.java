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
import server.utilities.EnumUtils;

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

//    private final EnumUtils.FinancialInstrumentClass name_US_STOCKS = EnumUtils.FinancialInstrumentClass.US_STOCKS;
//    private Double currentValuePerShare_US_STOCKS = 0.0;
//    private Integer currentNumberOfShare_US_STOCKS = 0;
//    private Double buyingValuePerShare_US_STOCKS = 0.0;
//    private Integer buyingNumberOfShare_US_STOCKS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_FOREIGN_DEVELOPED_STOCKS = EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS;
//    private Double currentValuePerShare_FOREIGN_DEVELOPED_STOCKS = 0.0;
//    private Integer currentNumberOfShare_FOREIGN_DEVELOPED_STOCKS = 0;
//    private Double buyingValuePerShare_FOREIGN_DEVELOPED_STOCKS = 0.0;
//    private Integer buyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_EMERGING_MARKET_STOCKS = EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_STOCKS;
//    private Double currentValuePerShare_EMERGING_MARKET_STOCKS = 0.0;
//    private Integer currentNumberOfShare_EMERGING_MARKET_STOCKS = 0;
//    private Double buyingValuePerShare_EMERGING_MARKET_STOCKS = 0.0;
//    private Integer buyingNumberOfShare_EMERGING_MARKET_STOCKS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_DIVIDEND_GROWTH_STOCKS = EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS;
//    private Double currentValuePerShare_DIVIDEND_GROWTH_STOCKS = 0.0;
//    private Integer currentNumberOfShare_DIVIDEND_GROWTH_STOCKS = 0;
//    private Double buyingValuePerShare_DIVIDEND_GROWTH_STOCKS = 0.0;
//    private Integer buyingNumberOfShare_DIVIDEND_GROWTH_STOCKS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_US_GOVERNMENT_BONDS = EnumUtils.FinancialInstrumentClass.US_GOVERNMENT_BONDS;
//    private Double currentValuePerShare_US_GOVERNMENT_BONDS = 0.0;
//    private Integer currentNumberOfShare_US_GOVERNMENT_BONDS = 0;
//    private Double buyingValuePerShare_US_GOVERNMENT_BONDS = 0.0;
//    private Integer buyingNumberOfShare_US_GOVERNMENT_BONDS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_CORPORATE_BONDS = EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS;
//    private Double currentValuePerShare_CORPORATE_BONDS = 0.0;
//    private Integer currentNumberOfShare_CORPORATE_BONDS = 0;
//    private Double buyingValuePerShare_CORPORATE_BONDS = 0.0;
//    private Integer buyingNumberOfShare_CORPORATE_BONDS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_EMERGING_MARKET_BONDS = EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_BONDS;
//    private Double currentValuePerShare_EMERGING_MARKET_BONDS = 0.0;
//    private Integer currentNumberOfShare_EMERGING_MARKET_BONDS = 0;
//    private Double buyingValuePerShare_EMERGING_MARKET_BONDS = 0.0;
//    private Integer buyingNumberOfShare_EMERGING_MARKET_BONDS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_MUNICIPAL_BONDS = EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS;
//    private Double currentValuePerShare_MUNICIPAL_BONDS = 0.0;
//    private Integer currentNumberOfShare_MUNICIPAL_BONDS = 0;
//    private Double buyingValuePerShare_MUNICIPAL_BONDS = 0.0;
//    private Integer buyingNumberOfShare_MUNICIPAL_BONDS = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_TREASURY_INFLATION_PROTECTED_SECURITIES = EnumUtils.FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES;
//    private Double currentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES = 0.0;
//    private Integer currentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES = 0;
//    private Double buyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES = 0.0;
//    private Integer buyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_REAL_ESTATE = EnumUtils.FinancialInstrumentClass.REAL_ESTATE;
//    private Double currentValuePerShare_REAL_ESTATE = 0.0;
//    private Integer currentNumberOfShare_REAL_ESTATE = 0;
//    private Double buyingValuePerShare_REAL_ESTATE = 0.0;
//    private Integer buyingNumberOfShare_REAL_ESTATE = 0;
//
//    private final EnumUtils.FinancialInstrumentClass name_NATURAL_RESOURCES = EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES;
//    private Double currentValuePerShare_NATURAL_RESOURCES = 0.0;
//    private Integer currentNumberOfShare_NATURAL_RESOURCES = 0;
//    private Double buyingValuePerShare_NATURAL_RESOURCES = 0.0;
//    private Integer buyingNumberOfShare_NATURAL_RESOURCES = 0;

    @OneToOne(cascade = CascadeType.MERGE, optional = false)
    private InvestmentPlan executedInvestmentPlan;
    @OneToMany(mappedBy = "portfolio")
    private List<MovingAverage> movingAverages;
    @ManyToOne(cascade = CascadeType.MERGE)
    private WealthManagementSubscriber wealthManagementSubscriber;

    public Double getTotalBuyingValue() {
        Double totalValue = 0.0;
//        totalValue += buyingValuePerShare_US_STOCKS * buyingNumberOfShare_US_STOCKS;
//        totalValue += buyingValuePerShare_FOREIGN_DEVELOPED_STOCKS * buyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS;
//        totalValue += buyingValuePerShare_EMERGING_MARKET_STOCKS * buyingNumberOfShare_EMERGING_MARKET_STOCKS;
//        totalValue += buyingValuePerShare_DIVIDEND_GROWTH_STOCKS * buyingNumberOfShare_DIVIDEND_GROWTH_STOCKS;
//        totalValue += buyingValuePerShare_US_GOVERNMENT_BONDS * buyingNumberOfShare_US_GOVERNMENT_BONDS;
//        totalValue += buyingValuePerShare_CORPORATE_BONDS * buyingNumberOfShare_CORPORATE_BONDS;
//        totalValue += buyingValuePerShare_EMERGING_MARKET_BONDS * buyingNumberOfShare_EMERGING_MARKET_BONDS;
//        totalValue += buyingValuePerShare_MUNICIPAL_BONDS * buyingNumberOfShare_MUNICIPAL_BONDS;
//        totalValue += buyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES * buyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//        totalValue += buyingValuePerShare_REAL_ESTATE * buyingNumberOfShare_REAL_ESTATE;
//        totalValue += buyingValuePerShare_NATURAL_RESOURCES * buyingNumberOfShare_NATURAL_RESOURCES;
        for(int i = 0;i < executedInvestmentPlan.getSuggestedFinancialInstruments().size();i++)
            totalValue += executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare() * executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getBuyingNumberOfShare();

        return totalValue;
    }

    public Double getTotalCurrentValue() {
        Double totalValue = 0.0;
//        totalValue += currentValuePerShare_US_STOCKS * currentNumberOfShare_US_STOCKS;
//        totalValue += currentValuePerShare_FOREIGN_DEVELOPED_STOCKS * currentNumberOfShare_FOREIGN_DEVELOPED_STOCKS;
//        totalValue += currentValuePerShare_EMERGING_MARKET_STOCKS * currentNumberOfShare_EMERGING_MARKET_STOCKS;
//        totalValue += currentValuePerShare_DIVIDEND_GROWTH_STOCKS * currentNumberOfShare_DIVIDEND_GROWTH_STOCKS;
//        totalValue += currentValuePerShare_US_GOVERNMENT_BONDS * currentNumberOfShare_US_GOVERNMENT_BONDS;
//        totalValue += currentValuePerShare_CORPORATE_BONDS * currentNumberOfShare_CORPORATE_BONDS;
//        totalValue += currentValuePerShare_EMERGING_MARKET_BONDS * currentNumberOfShare_EMERGING_MARKET_BONDS;
//        totalValue += currentValuePerShare_MUNICIPAL_BONDS * currentNumberOfShare_MUNICIPAL_BONDS;
//        totalValue += currentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES * currentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//        totalValue += currentValuePerShare_REAL_ESTATE * currentNumberOfShare_REAL_ESTATE;
//        totalValue += currentValuePerShare_NATURAL_RESOURCES * currentNumberOfShare_NATURAL_RESOURCES;
        for(int i = 0;i < executedInvestmentPlan.getSuggestedFinancialInstruments().size();i++)
            totalValue += executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getCurrentValuePerShare()* executedInvestmentPlan.getSuggestedFinancialInstruments().get(i).getCurrentNumberOfShare();
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

//    public Double getCurrentValuePerShare_US_STOCKS() {
//        return currentValuePerShare_US_STOCKS;
//    }
//
//    public void setCurrentValuePerShare_US_STOCKS(Double currentValuePerShare_US_STOCKS) {
//        this.currentValuePerShare_US_STOCKS = currentValuePerShare_US_STOCKS;
//    }
//
//    public Integer getCurrentNumberOfShare_US_STOCKS() {
//        return currentNumberOfShare_US_STOCKS;
//    }
//
//    public void setCurrentNumberOfShare_US_STOCKS(Integer currentNumberOfShare_US_STOCKS) {
//        this.currentNumberOfShare_US_STOCKS = currentNumberOfShare_US_STOCKS;
//    }
//
//    public Double getBuyingValuePerShare_US_STOCKS() {
//        return buyingValuePerShare_US_STOCKS;
//    }
//
//    public void setBuyingValuePerShare_US_STOCKS(Double buyingValuePerShare_US_STOCKS) {
//        this.buyingValuePerShare_US_STOCKS = buyingValuePerShare_US_STOCKS;
//    }
//
//    public Integer getBuyingNumberOfShare_US_STOCKS() {
//        return buyingNumberOfShare_US_STOCKS;
//    }
//
//    public void setBuyingNumberOfShare_US_STOCKS(Integer buyingNumberOfShare_US_STOCKS) {
//        this.buyingNumberOfShare_US_STOCKS = buyingNumberOfShare_US_STOCKS;
//    }
//
//    public Double getCurrentValuePerShare_FOREIGN_DEVELOPED_STOCKS() {
//        return currentValuePerShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public void setCurrentValuePerShare_FOREIGN_DEVELOPED_STOCKS(Double currentValuePerShare_FOREIGN_DEVELOPED_STOCKS) {
//        this.currentValuePerShare_FOREIGN_DEVELOPED_STOCKS = currentValuePerShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public Integer getCurrentNumberOfShare_FOREIGN_DEVELOPED_STOCKS() {
//        return currentNumberOfShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public void setCurrentNumberOfShare_FOREIGN_DEVELOPED_STOCKS(Integer currentNumberOfShare_FOREIGN_DEVELOPED_STOCKS) {
//        this.currentNumberOfShare_FOREIGN_DEVELOPED_STOCKS = currentNumberOfShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public Double getBuyingValuePerShare_FOREIGN_DEVELOPED_STOCKS() {
//        return buyingValuePerShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public void setBuyingValuePerShare_FOREIGN_DEVELOPED_STOCKS(Double buyingValuePerShare_FOREIGN_DEVELOPED_STOCKS) {
//        this.buyingValuePerShare_FOREIGN_DEVELOPED_STOCKS = buyingValuePerShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public Integer getBuyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS() {
//        return buyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public void setBuyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS(Integer buyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS) {
//        this.buyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS = buyingNumberOfShare_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public Double getCurrentValuePerShare_EMERGING_MARKET_STOCKS() {
//        return currentValuePerShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public void setCurrentValuePerShare_EMERGING_MARKET_STOCKS(Double currentValuePerShare_EMERGING_MARKET_STOCKS) {
//        this.currentValuePerShare_EMERGING_MARKET_STOCKS = currentValuePerShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public Integer getCurrentNumberOfShare_EMERGING_MARKET_STOCKS() {
//        return currentNumberOfShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public void setCurrentNumberOfShare_EMERGING_MARKET_STOCKS(Integer currentNumberOfShare_EMERGING_MARKET_STOCKS) {
//        this.currentNumberOfShare_EMERGING_MARKET_STOCKS = currentNumberOfShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public Double getBuyingValuePerShare_EMERGING_MARKET_STOCKS() {
//        return buyingValuePerShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public void setBuyingValuePerShare_EMERGING_MARKET_STOCKS(Double buyingValuePerShare_EMERGING_MARKET_STOCKS) {
//        this.buyingValuePerShare_EMERGING_MARKET_STOCKS = buyingValuePerShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public Integer getBuyingNumberOfShare_EMERGING_MARKET_STOCKS() {
//        return buyingNumberOfShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public void setBuyingNumberOfShare_EMERGING_MARKET_STOCKS(Integer buyingNumberOfShare_EMERGING_MARKET_STOCKS) {
//        this.buyingNumberOfShare_EMERGING_MARKET_STOCKS = buyingNumberOfShare_EMERGING_MARKET_STOCKS;
//    }
//
//    public Double getCurrentValuePerShare_DIVIDEND_GROWTH_STOCKS() {
//        return currentValuePerShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public void setCurrentValuePerShare_DIVIDEND_GROWTH_STOCKS(Double currentValuePerShare_DIVIDEND_GROWTH_STOCKS) {
//        this.currentValuePerShare_DIVIDEND_GROWTH_STOCKS = currentValuePerShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public Integer getCurrentNumberOfShare_DIVIDEND_GROWTH_STOCKS() {
//        return currentNumberOfShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public void setCurrentNumberOfShare_DIVIDEND_GROWTH_STOCKS(Integer currentNumberOfShare_DIVIDEND_GROWTH_STOCKS) {
//        this.currentNumberOfShare_DIVIDEND_GROWTH_STOCKS = currentNumberOfShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public Double getBuyingValuePerShare_DIVIDEND_GROWTH_STOCKS() {
//        return buyingValuePerShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public void setBuyingValuePerShare_DIVIDEND_GROWTH_STOCKS(Double buyingValuePerShare_DIVIDEND_GROWTH_STOCKS) {
//        this.buyingValuePerShare_DIVIDEND_GROWTH_STOCKS = buyingValuePerShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public Integer getBuyingNumberOfShare_DIVIDEND_GROWTH_STOCKS() {
//        return buyingNumberOfShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public void setBuyingNumberOfShare_DIVIDEND_GROWTH_STOCKS(Integer buyingNumberOfShare_DIVIDEND_GROWTH_STOCKS) {
//        this.buyingNumberOfShare_DIVIDEND_GROWTH_STOCKS = buyingNumberOfShare_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public Double getCurrentValuePerShare_US_GOVERNMENT_BONDS() {
//        return currentValuePerShare_US_GOVERNMENT_BONDS;
//    }
//
//    public void setCurrentValuePerShare_US_GOVERNMENT_BONDS(Double currentValuePerShare_US_GOVERNMENT_BONDS) {
//        this.currentValuePerShare_US_GOVERNMENT_BONDS = currentValuePerShare_US_GOVERNMENT_BONDS;
//    }
//
//    public Integer getCurrentNumberOfShare_US_GOVERNMENT_BONDS() {
//        return currentNumberOfShare_US_GOVERNMENT_BONDS;
//    }
//
//    public void setCurrentNumberOfShare_US_GOVERNMENT_BONDS(Integer currentNumberOfShare_US_GOVERNMENT_BONDS) {
//        this.currentNumberOfShare_US_GOVERNMENT_BONDS = currentNumberOfShare_US_GOVERNMENT_BONDS;
//    }
//
//    public Double getBuyingValuePerShare_US_GOVERNMENT_BONDS() {
//        return buyingValuePerShare_US_GOVERNMENT_BONDS;
//    }
//
//    public void setBuyingValuePerShare_US_GOVERNMENT_BONDS(Double buyingValuePerShare_US_GOVERNMENT_BONDS) {
//        this.buyingValuePerShare_US_GOVERNMENT_BONDS = buyingValuePerShare_US_GOVERNMENT_BONDS;
//    }
//
//    public Integer getBuyingNumberOfShare_US_GOVERNMENT_BONDS() {
//        return buyingNumberOfShare_US_GOVERNMENT_BONDS;
//    }
//
//    public void setBuyingNumberOfShare_US_GOVERNMENT_BONDS(Integer buyingNumberOfShare_US_GOVERNMENT_BONDS) {
//        this.buyingNumberOfShare_US_GOVERNMENT_BONDS = buyingNumberOfShare_US_GOVERNMENT_BONDS;
//    }
//
//    public Double getCurrentValuePerShare_CORPORATE_BONDS() {
//        return currentValuePerShare_CORPORATE_BONDS;
//    }
//
//    public void setCurrentValuePerShare_CORPORATE_BONDS(Double currentValuePerShare_CORPORATE_BONDS) {
//        this.currentValuePerShare_CORPORATE_BONDS = currentValuePerShare_CORPORATE_BONDS;
//    }
//
//    public Integer getCurrentNumberOfShare_CORPORATE_BONDS() {
//        return currentNumberOfShare_CORPORATE_BONDS;
//    }
//
//    public void setCurrentNumberOfShare_CORPORATE_BONDS(Integer currentNumberOfShare_CORPORATE_BONDS) {
//        this.currentNumberOfShare_CORPORATE_BONDS = currentNumberOfShare_CORPORATE_BONDS;
//    }
//
//    public Double getBuyingValuePerShare_CORPORATE_BONDS() {
//        return buyingValuePerShare_CORPORATE_BONDS;
//    }
//
//    public void setBuyingValuePerShare_CORPORATE_BONDS(Double buyingValuePerShare_CORPORATE_BONDS) {
//        this.buyingValuePerShare_CORPORATE_BONDS = buyingValuePerShare_CORPORATE_BONDS;
//    }
//
//    public Integer getBuyingNumberOfShare_CORPORATE_BONDS() {
//        return buyingNumberOfShare_CORPORATE_BONDS;
//    }
//
//    public void setBuyingNumberOfShare_CORPORATE_BONDS(Integer buyingNumberOfShare_CORPORATE_BONDS) {
//        this.buyingNumberOfShare_CORPORATE_BONDS = buyingNumberOfShare_CORPORATE_BONDS;
//    }
//
//    public Double getCurrentValuePerShare_EMERGING_MARKET_BONDS() {
//        return currentValuePerShare_EMERGING_MARKET_BONDS;
//    }
//
//    public void setCurrentValuePerShare_EMERGING_MARKET_BONDS(Double currentValuePerShare_EMERGING_MARKET_BONDS) {
//        this.currentValuePerShare_EMERGING_MARKET_BONDS = currentValuePerShare_EMERGING_MARKET_BONDS;
//    }
//
//    public Integer getCurrentNumberOfShare_EMERGING_MARKET_BONDS() {
//        return currentNumberOfShare_EMERGING_MARKET_BONDS;
//    }
//
//    public void setCurrentNumberOfShare_EMERGING_MARKET_BONDS(Integer currentNumberOfShare_EMERGING_MARKET_BONDS) {
//        this.currentNumberOfShare_EMERGING_MARKET_BONDS = currentNumberOfShare_EMERGING_MARKET_BONDS;
//    }
//
//    public Double getBuyingValuePerShare_EMERGING_MARKET_BONDS() {
//        return buyingValuePerShare_EMERGING_MARKET_BONDS;
//    }
//
//    public void setBuyingValuePerShare_EMERGING_MARKET_BONDS(Double buyingValuePerShare_EMERGING_MARKET_BONDS) {
//        this.buyingValuePerShare_EMERGING_MARKET_BONDS = buyingValuePerShare_EMERGING_MARKET_BONDS;
//    }
//
//    public Integer getBuyingNumberOfShare_EMERGING_MARKET_BONDS() {
//        return buyingNumberOfShare_EMERGING_MARKET_BONDS;
//    }
//
//    public void setBuyingNumberOfShare_EMERGING_MARKET_BONDS(Integer buyingNumberOfShare_EMERGING_MARKET_BONDS) {
//        this.buyingNumberOfShare_EMERGING_MARKET_BONDS = buyingNumberOfShare_EMERGING_MARKET_BONDS;
//    }
//
//    public Double getCurrentValuePerShare_MUNICIPAL_BONDS() {
//        return currentValuePerShare_MUNICIPAL_BONDS;
//    }
//
//    public void setCurrentValuePerShare_MUNICIPAL_BONDS(Double currentValuePerShare_MUNICIPAL_BONDS) {
//        this.currentValuePerShare_MUNICIPAL_BONDS = currentValuePerShare_MUNICIPAL_BONDS;
//    }
//
//    public Integer getCurrentNumberOfShare_MUNICIPAL_BONDS() {
//        return currentNumberOfShare_MUNICIPAL_BONDS;
//    }
//
//    public void setCurrentNumberOfShare_MUNICIPAL_BONDS(Integer currentNumberOfShare_MUNICIPAL_BONDS) {
//        this.currentNumberOfShare_MUNICIPAL_BONDS = currentNumberOfShare_MUNICIPAL_BONDS;
//    }
//
//    public Double getBuyingValuePerShare_MUNICIPAL_BONDS() {
//        return buyingValuePerShare_MUNICIPAL_BONDS;
//    }
//
//    public void setBuyingValuePerShare_MUNICIPAL_BONDS(Double buyingValuePerShare_MUNICIPAL_BONDS) {
//        this.buyingValuePerShare_MUNICIPAL_BONDS = buyingValuePerShare_MUNICIPAL_BONDS;
//    }
//
//    public Integer getBuyingNumberOfShare_MUNICIPAL_BONDS() {
//        return buyingNumberOfShare_MUNICIPAL_BONDS;
//    }
//
//    public void setBuyingNumberOfShare_MUNICIPAL_BONDS(Integer buyingNumberOfShare_MUNICIPAL_BONDS) {
//        this.buyingNumberOfShare_MUNICIPAL_BONDS = buyingNumberOfShare_MUNICIPAL_BONDS;
//    }
//
//    public Double getCurrentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES() {
//        return currentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public void setCurrentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES(Double currentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES) {
//        this.currentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES = currentValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public Integer getCurrentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES() {
//        return currentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public void setCurrentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES(Integer currentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES) {
//        this.currentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES = currentNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public Double getBuyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES() {
//        return buyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public void setBuyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES(Double buyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES) {
//        this.buyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES = buyingValuePerShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public Integer getBuyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES() {
//        return buyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public void setBuyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES(Integer buyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES) {
//        this.buyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES = buyingNumberOfShare_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public Double getCurrentValuePerShare_REAL_ESTATE() {
//        return currentValuePerShare_REAL_ESTATE;
//    }
//
//    public void setCurrentValuePerShare_REAL_ESTATE(Double currentValuePerShare_REAL_ESTATE) {
//        this.currentValuePerShare_REAL_ESTATE = currentValuePerShare_REAL_ESTATE;
//    }
//
//    public Integer getCurrentNumberOfShare_REAL_ESTATE() {
//        return currentNumberOfShare_REAL_ESTATE;
//    }
//
//    public void setCurrentNumberOfShare_REAL_ESTATE(Integer currentNumberOfShare_REAL_ESTATE) {
//        this.currentNumberOfShare_REAL_ESTATE = currentNumberOfShare_REAL_ESTATE;
//    }
//
//    public Double getBuyingValuePerShare_REAL_ESTATE() {
//        return buyingValuePerShare_REAL_ESTATE;
//    }
//
//    public void setBuyingValuePerShare_REAL_ESTATE(Double buyingValuePerShare_REAL_ESTATE) {
//        this.buyingValuePerShare_REAL_ESTATE = buyingValuePerShare_REAL_ESTATE;
//    }
//
//    public Integer getBuyingNumberOfShare_REAL_ESTATE() {
//        return buyingNumberOfShare_REAL_ESTATE;
//    }
//
//    public void setBuyingNumberOfShare_REAL_ESTATE(Integer buyingNumberOfShare_REAL_ESTATE) {
//        this.buyingNumberOfShare_REAL_ESTATE = buyingNumberOfShare_REAL_ESTATE;
//    }
//
//    public Double getCurrentValuePerShare_NATURAL_RESOURCES() {
//        return currentValuePerShare_NATURAL_RESOURCES;
//    }
//
//    public void setCurrentValuePerShare_NATURAL_RESOURCES(Double currentValuePerShare_NATURAL_RESOURCES) {
//        this.currentValuePerShare_NATURAL_RESOURCES = currentValuePerShare_NATURAL_RESOURCES;
//    }
//
//    public Integer getCurrentNumberOfShare_NATURAL_RESOURCES() {
//        return currentNumberOfShare_NATURAL_RESOURCES;
//    }
//
//    public void setCurrentNumberOfShare_NATURAL_RESOURCES(Integer currentNumberOfShare_NATURAL_RESOURCES) {
//        this.currentNumberOfShare_NATURAL_RESOURCES = currentNumberOfShare_NATURAL_RESOURCES;
//    }
//
//    public Double getBuyingValuePerShare_NATURAL_RESOURCES() {
//        return buyingValuePerShare_NATURAL_RESOURCES;
//    }
//
//    public void setBuyingValuePerShare_NATURAL_RESOURCES(Double buyingValuePerShare_NATURAL_RESOURCES) {
//        this.buyingValuePerShare_NATURAL_RESOURCES = buyingValuePerShare_NATURAL_RESOURCES;
//    }
//
//    public Integer getBuyingNumberOfShare_NATURAL_RESOURCES() {
//        return buyingNumberOfShare_NATURAL_RESOURCES;
//    }
//
//    public void setBuyingNumberOfShare_NATURAL_RESOURCES(Integer buyingNumberOfShare_NATURAL_RESOURCES) {
//        this.buyingNumberOfShare_NATURAL_RESOURCES = buyingNumberOfShare_NATURAL_RESOURCES;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_US_STOCKS() {
//        return name_US_STOCKS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_FOREIGN_DEVELOPED_STOCKS() {
//        return name_FOREIGN_DEVELOPED_STOCKS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_EMERGING_MARKET_STOCKS() {
//        return name_EMERGING_MARKET_STOCKS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_DIVIDEND_GROWTH_STOCKS() {
//        return name_DIVIDEND_GROWTH_STOCKS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_US_GOVERNMENT_BONDS() {
//        return name_US_GOVERNMENT_BONDS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_CORPORATE_BONDS() {
//        return name_CORPORATE_BONDS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_EMERGING_MARKET_BONDS() {
//        return name_EMERGING_MARKET_BONDS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_MUNICIPAL_BONDS() {
//        return name_MUNICIPAL_BONDS;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_TREASURY_INFLATION_PROTECTED_SECURITIES() {
//        return name_TREASURY_INFLATION_PROTECTED_SECURITIES;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_REAL_ESTATE() {
//        return name_REAL_ESTATE;
//    }
//
//    public EnumUtils.FinancialInstrumentClass getName_NATURAL_RESOURCES() {
//        return name_NATURAL_RESOURCES;
//    }

    public List<MovingAverage> getMovingAverages() {
        return movingAverages;
    }

    public void setMovingAverages(List<MovingAverage> movingAverages) {
        this.movingAverages = movingAverages;
    }

}
