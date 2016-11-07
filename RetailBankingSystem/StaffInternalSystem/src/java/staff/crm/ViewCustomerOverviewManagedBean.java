/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.bi.BizIntelligenceSessionBeanLocal;
import ejb.session.crm.CrmIntelligenceSessionBeanLocal;
import ejb.session.fact.BankFactTableSessionBeanLocal;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import server.utilities.ColorUtils;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Named(value = "viewCustomerOverviewManagedBean")
@ViewScoped
public class ViewCustomerOverviewManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;
    @EJB
    BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;
    @EJB
    CrmIntelligenceSessionBeanLocal crmIntelligenceSessionBean;

    private Long depositChurnCustomer;
    private Long cardChurnCustomer;
    private Long loanChurnCustomer;
    private Long wealthChurnCustomer;
    private Double avgCustomerLifeTimeValue;
    private Double avgCustomerAge;
    private Double avgCustomerDepositSavingAmount;
    private Double avgCustomerLoanAmount;

    private Long bankTotalDepositAcct;
    private Long bankTotalLoanAcct;
    private Long bankTotalCardAcct;
    private Long bankTotalExecutedPortfolio;

    private final String AGE_0_20 = "0 - 20";
    private final String AGE_20_30 = "20 - 30";
    private final String AGE_30_40 = "30 - 40";
    private final String AGE_40_50 = "40 - 50";
    private final String AGE_50_60 = "50 - 60";
    private final String AGE_60_70 = "60 - 70";
    private final String AGE_70_100 = "70 - 100";

    private Long numAGE_0_20;
    private Long numAGE_20_30;
    private Long numAGE_30_40;
    private Long numAGE_40_50;
    private Long numAGE_50_60;
    private Long numAGE_60_70;
    private Long numAGE_70_100;

    private Long numNewAGE_0_20;
    private Long numNewAGE_20_30;
    private Long numNewAGE_30_40;
    private Long numNewAGE_40_50;
    private Long numNewAGE_50_60;
    private Long numNewAGE_60_70;
    private Long numNewAGE_70_100;

    private BarChartModel customerAcctBarModel;
    private BarChartModel customerAgeBarModel;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date startDate;
    private Date endDate;

    public ViewCustomerOverviewManagedBean() {
    }

    @PostConstruct
    public void init() {
        startDate = DateUtils.getBeginOfMonth();
        endDate = DateUtils.getEndOfMonth();
        
        System.out.println(startDate);
        System.out.println(endDate);
        
        depositChurnCustomer = crmIntelligenceSessionBean.getDepositChurnCustomer(startDate, endDate);
        cardChurnCustomer = crmIntelligenceSessionBean.getCardChurnCustomer(startDate, endDate);
        loanChurnCustomer = crmIntelligenceSessionBean.getLoanChurnCustomer(startDate, endDate);
        wealthChurnCustomer = crmIntelligenceSessionBean.getWealthChurnCustomer(startDate, endDate);
        avgCustomerDepositSavingAmount = crmIntelligenceSessionBean.getCustomerAvgDepositSavingAmount(endDate);
        avgCustomerLoanAmount = crmIntelligenceSessionBean.getCustomerAvgLoanAmount(endDate);
        createCustomerAcctBarModel();
        createCustomerAgeBarModel();
    }
    
    public void viewDataTable(){
        depositChurnCustomer = crmIntelligenceSessionBean.getDepositChurnCustomer(startDate, endDate);
        cardChurnCustomer = crmIntelligenceSessionBean.getCardChurnCustomer(startDate, endDate);
        loanChurnCustomer = crmIntelligenceSessionBean.getLoanChurnCustomer(startDate, endDate);
        wealthChurnCustomer = crmIntelligenceSessionBean.getWealthChurnCustomer(startDate, endDate);
        avgCustomerDepositSavingAmount = crmIntelligenceSessionBean.getCustomerAvgDepositSavingAmount(endDate);
        avgCustomerLoanAmount = crmIntelligenceSessionBean.getCustomerAvgLoanAmount(endDate);
        
        createCustomerAcctBarModel();
        createCustomerAgeBarModel();
    }

    private BarChartModel createCustomerAgeBarModel() {
        customerAgeBarModel = initCustomerAgeBarModel();
        customerAgeBarModel.setTitle("New Customer Age Distribution");
        customerAgeBarModel.setLegendPosition("ne");

        Axis xAxis = customerAgeBarModel.getAxis(AxisType.X);
        Axis yAxis = customerAgeBarModel.getAxis(AxisType.Y);

        return customerAgeBarModel;
    }

    private BarChartModel createCustomerAcctBarModel() {
        customerAcctBarModel = initCustomerAcctBarModel();
        customerAcctBarModel.setTitle("New Banking Service");
        customerAcctBarModel.setLegendPosition("ne");

        Axis xAxis = customerAcctBarModel.getAxis(AxisType.X);
        Axis yAxis = customerAcctBarModel.getAxis(AxisType.Y);

        return customerAcctBarModel;
    }

    private BarChartModel initCustomerAgeBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("# of Total Customers");

        ChartSeries series2 = new ChartSeries();
        series2.setLabel("# of New Customers");

        numAGE_0_20 = crmIntelligenceSessionBean.getTotalCustomerAgeGroup(0, 20);
        numAGE_20_30 = crmIntelligenceSessionBean.getTotalCustomerAgeGroup(20, 30);
        numAGE_30_40 = crmIntelligenceSessionBean.getTotalCustomerAgeGroup(30, 40);
        numAGE_40_50 = crmIntelligenceSessionBean.getTotalCustomerAgeGroup(40, 50);
        numAGE_50_60 = crmIntelligenceSessionBean.getTotalCustomerAgeGroup(50, 60);
        numAGE_60_70 = crmIntelligenceSessionBean.getTotalCustomerAgeGroup(60, 70);
        numAGE_70_100 = crmIntelligenceSessionBean.getTotalCustomerAgeGroup(70, 100);

        numNewAGE_0_20 = crmIntelligenceSessionBean.getNewCustomerAgeGroup(0, 20, startDate, endDate);
        numNewAGE_20_30 = crmIntelligenceSessionBean.getNewCustomerAgeGroup(20, 30, startDate, endDate);
        numNewAGE_30_40 = crmIntelligenceSessionBean.getNewCustomerAgeGroup(30, 40, startDate, endDate);
        numNewAGE_40_50 = crmIntelligenceSessionBean.getNewCustomerAgeGroup(40, 50, startDate, endDate);
        numNewAGE_50_60 = crmIntelligenceSessionBean.getNewCustomerAgeGroup(50, 60, startDate, endDate);
        numNewAGE_60_70 = crmIntelligenceSessionBean.getNewCustomerAgeGroup(60, 70, startDate, endDate);
        numNewAGE_70_100 = crmIntelligenceSessionBean.getNewCustomerAgeGroup(70, 100, startDate, endDate);

        series1.set(AGE_0_20, numAGE_0_20);
        series1.set(AGE_20_30, numAGE_20_30);
        series1.set(AGE_30_40, numAGE_30_40);
        series1.set(AGE_40_50, numAGE_40_50);
        series1.set(AGE_50_60, numAGE_50_60);
        series1.set(AGE_60_70, numAGE_60_70);
        series1.set(AGE_70_100, numAGE_70_100);

        series2.set(AGE_0_20, numNewAGE_0_20);
        series2.set(AGE_20_30, numNewAGE_20_30);
        series2.set(AGE_30_40, numNewAGE_30_40);
        series2.set(AGE_40_50, numNewAGE_40_50);
        series2.set(AGE_50_60, numNewAGE_50_60);
        series2.set(AGE_60_70, numNewAGE_60_70);
        series2.set(AGE_70_100, numNewAGE_70_100);

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }

    private BarChartModel initCustomerAcctBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        ChartSeries series2 = new ChartSeries();
        ChartSeries series3 = new ChartSeries();
        ChartSeries series4 = new ChartSeries();
        series1.setLabel("# of new deposit accounts");
        series2.setLabel("# of new credit card accounts");
        series3.setLabel("# of new loan accounts");
        series4.setLabel("# of new executed investment plans");

        bankTotalDepositAcct = bizIntelligenceSessionBean.getBankTotalNewDepositAcct(startDate, endDate);
        bankTotalCardAcct = bizIntelligenceSessionBean.getBankTotalNewCardAcct(startDate, endDate);
        bankTotalLoanAcct = bizIntelligenceSessionBean.getBankTotalNewLoanAcct(startDate, endDate);
        bankTotalExecutedPortfolio = bizIntelligenceSessionBean.getBankNewExecutedPortfolio(startDate, endDate);

        series1.set("Deposit Service", bankTotalDepositAcct);
        series2.set("Card Service", bankTotalCardAcct);
        series3.set("Loan Service", bankTotalLoanAcct);
        series4.set("Portfolio Service", bankTotalExecutedPortfolio);

        model.addSeries(series1);
        model.addSeries(series2);
        model.addSeries(series3);
        model.addSeries(series4);
        model.setSeriesColors(ColorUtils.getFlatUIColors(0) + "," + ColorUtils.getFlatUIColors(5) + "," + ColorUtils.getFlatUIColors(10) + "," + ColorUtils.getFlatUIColors(15) );
        model.setExtender("customExtender");

        return model;
    }

    public BarChartModel getCustomerAcctBarModel() {
        return customerAcctBarModel;
    }

    public void setCustomerAcctBarModel(BarChartModel customerAcctBarModel) {
        this.customerAcctBarModel = customerAcctBarModel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getBankTotalDepositAcct() {
        return bankTotalDepositAcct;
    }

    public void setBankTotalDepositAcct(Long bankTotalDepositAcct) {
        this.bankTotalDepositAcct = bankTotalDepositAcct;
    }

    public Long getBankTotalLoanAcct() {
        return bankTotalLoanAcct;
    }

    public void setBankTotalLoanAcct(Long bankTotalLoanAcct) {
        this.bankTotalLoanAcct = bankTotalLoanAcct;
    }

    public Long getBankTotalCardAcct() {
        return bankTotalCardAcct;
    }

    public void setBankTotalCardAcct(Long bankTotalCardAcct) {
        this.bankTotalCardAcct = bankTotalCardAcct;
    }

    public Long getBankTotalExecutedPortfolio() {
        return bankTotalExecutedPortfolio;
    }

    public void setBankTotalExecutedPortfolio(Long bankTotalExecutedPortfolio) {
        this.bankTotalExecutedPortfolio = bankTotalExecutedPortfolio;
    }

    public Double getAvgCustomerLifeTimeValue() {
        return avgCustomerLifeTimeValue;
    }

    public void setAvgCustomerLifeTimeValue(Double avgCustomerLifeTimeValue) {
        this.avgCustomerLifeTimeValue = avgCustomerLifeTimeValue;
    }

    public Double getAvgCustomerAge() {
        return avgCustomerAge;
    }

    public void setAvgCustomerAge(Double avgCustomerAge) {
        this.avgCustomerAge = avgCustomerAge;
    }

    public Double getAvgCustomerDepositSavingAmount() {
        return avgCustomerDepositSavingAmount;
    }

    public void setAvgCustomerDepositSavingAmount(Double avgCustomerDepositSavingAmount) {
        this.avgCustomerDepositSavingAmount = avgCustomerDepositSavingAmount;
    }

    public Double getAvgCustomerLoanAmount() {
        return avgCustomerLoanAmount;
    }

    public void setAvgCustomerLoanAmount(Double avgCustomerLoanAmount) {
        this.avgCustomerLoanAmount = avgCustomerLoanAmount;
    }

    public BarChartModel getCustomerAgeBarModel() {
        return customerAgeBarModel;
    }

    public void setCustomerAgeBarModel(BarChartModel customerAgeBarModel) {
        this.customerAgeBarModel = customerAgeBarModel;
    }

    public String getAGE_0_20() {
        return AGE_0_20;
    }

    public String getAGE_20_30() {
        return AGE_20_30;
    }

    public String getAGE_30_40() {
        return AGE_30_40;
    }

    public String getAGE_40_50() {
        return AGE_40_50;
    }

    public String getAGE_50_60() {
        return AGE_50_60;
    }

    public String getAGE_60_70() {
        return AGE_60_70;
    }

    public String getAGE_70_100() {
        return AGE_70_100;
    }

    public Long getDepositChurnCustomer() {
        return depositChurnCustomer;
    }

    public void setDepositChurnCustomer(Long depositChurnCustomer) {
        this.depositChurnCustomer = depositChurnCustomer;
    }

    public Long getCardChurnCustomer() {
        return cardChurnCustomer;
    }

    public void setCardChurnCustomer(Long cardChurnCustomer) {
        this.cardChurnCustomer = cardChurnCustomer;
    }

    public Long getLoanChurnCustomer() {
        return loanChurnCustomer;
    }

    public void setLoanChurnCustomer(Long loanChurnCustomer) {
        this.loanChurnCustomer = loanChurnCustomer;
    }

    public Long getWealthChurnCustomer() {
        return wealthChurnCustomer;
    }

    public void setWealthChurnCustomer(Long wealthChurnCustomer) {
        this.wealthChurnCustomer = wealthChurnCustomer;
    }

    public Long getNumAGE_0_20() {
        return numAGE_0_20;
    }

    public void setNumAGE_0_20(Long numAGE_0_20) {
        this.numAGE_0_20 = numAGE_0_20;
    }

    public Long getNumAGE_20_30() {
        return numAGE_20_30;
    }

    public void setNumAGE_20_30(Long numAGE_20_30) {
        this.numAGE_20_30 = numAGE_20_30;
    }

    public Long getNumAGE_30_40() {
        return numAGE_30_40;
    }

    public void setNumAGE_30_40(Long numAGE_30_40) {
        this.numAGE_30_40 = numAGE_30_40;
    }

    public Long getNumAGE_40_50() {
        return numAGE_40_50;
    }

    public void setNumAGE_40_50(Long numAGE_40_50) {
        this.numAGE_40_50 = numAGE_40_50;
    }

    public Long getNumAGE_50_60() {
        return numAGE_50_60;
    }

    public void setNumAGE_50_60(Long numAGE_50_60) {
        this.numAGE_50_60 = numAGE_50_60;
    }

    public Long getNumAGE_60_70() {
        return numAGE_60_70;
    }

    public void setNumAGE_60_70(Long numAGE_60_70) {
        this.numAGE_60_70 = numAGE_60_70;
    }

    public Long getNumAGE_70_100() {
        return numAGE_70_100;
    }

    public void setNumAGE_70_100(Long numAGE_70_100) {
        this.numAGE_70_100 = numAGE_70_100;
    }

    public Long getNumNewAGE_0_20() {
        return numNewAGE_0_20;
    }

    public void setNumNewAGE_0_20(Long numNewAGE_0_20) {
        this.numNewAGE_0_20 = numNewAGE_0_20;
    }

    public Long getNumNewAGE_20_30() {
        return numNewAGE_20_30;
    }

    public void setNumNewAGE_20_30(Long numNewAGE_20_30) {
        this.numNewAGE_20_30 = numNewAGE_20_30;
    }

    public Long getNumNewAGE_30_40() {
        return numNewAGE_30_40;
    }

    public void setNumNewAGE_30_40(Long numNewAGE_30_40) {
        this.numNewAGE_30_40 = numNewAGE_30_40;
    }

    public Long getNumNewAGE_40_50() {
        return numNewAGE_40_50;
    }

    public void setNumNewAGE_40_50(Long numNewAGE_40_50) {
        this.numNewAGE_40_50 = numNewAGE_40_50;
    }

    public Long getNumNewAGE_50_60() {
        return numNewAGE_50_60;
    }

    public void setNumNewAGE_50_60(Long numNewAGE_50_60) {
        this.numNewAGE_50_60 = numNewAGE_50_60;
    }

    public Long getNumNewAGE_60_70() {
        return numNewAGE_60_70;
    }

    public void setNumNewAGE_60_70(Long numNewAGE_60_70) {
        this.numNewAGE_60_70 = numNewAGE_60_70;
    }

    public Long getNumNewAGE_70_100() {
        return numNewAGE_70_100;
    }

    public void setNumNewAGE_70_100(Long numNewAGE_70_100) {
        this.numNewAGE_70_100 = numNewAGE_70_100;
    }

}
