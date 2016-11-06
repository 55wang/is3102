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

    private Double churnRate;
    private Double avgCustomerLifeTimeValue;
    private Double avgCustomerAge;
    private Double avgCustomerDepositSavingAmount;
    private Double avgCustomerLoanAmount;

    private Long bankTotalDepositAcct;
    private Long bankTotalLoanAcct;
    private Long bankTotalCardAcct;
    private Long bankTotalExecutedPortfolio;

    private BarChartModel customerAcctBarModel;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date startDate = DateUtils.getBeginOfMonth();
    private Date endDate = DateUtils.getEndOfMonth();

    public ViewCustomerOverviewManagedBean() {
    }

    @PostConstruct
    public void init() {
        churnRate = crmIntelligenceSessionBean.getCustomerChurnRate(startDate, endDate);
        avgCustomerLifeTimeValue = crmIntelligenceSessionBean.getCustomerAvgCLV(startDate, endDate);
        avgCustomerAge = crmIntelligenceSessionBean.getCustomerAvgAge(startDate, endDate);
        avgCustomerDepositSavingAmount = crmIntelligenceSessionBean.getCustomerAvgDepositSavingAmount(startDate, endDate);
        avgCustomerLoanAmount = crmIntelligenceSessionBean.getCustomerAvgLoanAmount(startDate, endDate);
        createCustomerAcctBarModel();
    }

    private BarChartModel createCustomerAcctBarModel() {
        customerAcctBarModel = initCustomerAcctBarModel();
        customerAcctBarModel.setTitle("New Customer Banking Service");
        customerAcctBarModel.setLegendPosition("ne");

        Axis xAxis = customerAcctBarModel.getAxis(AxisType.X);
        Axis yAxis = customerAcctBarModel.getAxis(AxisType.Y);

        return customerAcctBarModel;
    }

    private BarChartModel initCustomerAcctBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("# of new customers");

        bankTotalDepositAcct = bizIntelligenceSessionBean.getBankTotalNewDepositAcct(startDate, endDate);
        bankTotalCardAcct = bizIntelligenceSessionBean.getBankTotalNewCardAcct(startDate, endDate);
        bankTotalLoanAcct = bizIntelligenceSessionBean.getBankTotalNewLoanAcct(startDate, endDate);
        bankTotalExecutedPortfolio = bizIntelligenceSessionBean.getBankNewExecutedPortfolio(startDate, endDate);

        series1.set("Deposit Service", bankTotalDepositAcct);
        series1.set("Card Service", bankTotalCardAcct);
        series1.set("Loan Service", bankTotalLoanAcct);
        series1.set("Portfolio Service", bankTotalExecutedPortfolio);

        model.addSeries(series1);

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

    public Double getChurnRate() {
        return churnRate;
    }

    public void setChurnRate(Double churnRate) {
        this.churnRate = churnRate;
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

}
