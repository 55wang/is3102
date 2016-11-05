/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bi;

import ejb.session.bi.BizIntelligenceSessionBeanLocal;
import ejb.session.fact.BankFactTableSessionBeanLocal;
import entity.fact.bank.BankFactTable;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Named(value = "viewDepositLoanManagedBean")
@ViewScoped
public class ViewDepositLoanManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;
    @EJB
    BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;

    private BarChartModel depositLoanAmtBarModel;
    private BarChartModel depositLoanIntBarModel;
    private PieChartModel badLoanPieModel;

    private Date startDate = DateUtils.getBeginOfMonth();
    private Date endDate = DateUtils.getEndOfMonth();

    //deposit and loan service
    private Long bankTotalDepositAcct;
    private Long bankTotalActiveDepositAcct;
    private Long bankTotalNewDepositAcct;
    private Double bankTotalDepositAmount;
    private Double bankDepositInterestAmount;

    private Long bankTotalLoanAcct;
    private Long bankTotalNewLoanAcct;
    private Double bankTotalLoanAmount;
    private Double bankLoanInterestEarned;
    private Double bankLoanInterestUnearned;
    private Long bankTotalDefaultLoanAcct;

    public ViewDepositLoanManagedBean() {
    }

    @PostConstruct
    public void init() {

        bankTotalDepositAcct = bizIntelligenceSessionBean.getBankTotalDepositAcct(startDate, endDate);
        bankTotalActiveDepositAcct = bizIntelligenceSessionBean.getBankTotalActiveDepositAcct(startDate, endDate);
        bankTotalNewDepositAcct = bizIntelligenceSessionBean.getBankTotalNewDepositAcct(startDate, endDate);
        bankTotalDepositAmount = bizIntelligenceSessionBean.getBankTotalDepositAmount(startDate, endDate);
        bankDepositInterestAmount = bizIntelligenceSessionBean.getBankTotalDepositInterestAmount(startDate, endDate);

        bankTotalLoanAcct = bizIntelligenceSessionBean.getBankTotalLoanAcct(startDate, endDate);
        bankTotalNewLoanAcct = bizIntelligenceSessionBean.getBankTotalNewLoanAcct(startDate, endDate);
        bankTotalLoanAmount = bizIntelligenceSessionBean.getBankTotalLoanAmount(startDate, endDate);
        bankLoanInterestEarned = bizIntelligenceSessionBean.getBankLoanInterestEarned(startDate, endDate);
        bankLoanInterestUnearned = bizIntelligenceSessionBean.getBankLoanInterestUnearned(startDate, endDate);
        bankTotalDefaultLoanAcct = bizIntelligenceSessionBean.getBankTotalDefaultLoanAcct(startDate, endDate);

        createDepositLoanAmtBarModel();
        createDepositLoanIntBarModel();
        createBadLoanPieModels();
    }

    public PieChartModel getBadLoanPieModel() {
        return badLoanPieModel;
    }

    private void createBadLoanPieModels() {
        createBadLoanPieModel();
    }

    public BarChartModel getDepositLoanIntBarModel() {
        return depositLoanIntBarModel;
    }

    private BarChartModel createDepositLoanAmtBarModel() {
        depositLoanAmtBarModel = initDepositLoanAmtBarModel();
        depositLoanAmtBarModel.setTitle("Total Deposit Amount & Total Loan Amount");
        depositLoanAmtBarModel.setLegendPosition("ne");

        Axis xAxis = depositLoanAmtBarModel.getAxis(AxisType.X);
        Axis yAxis = depositLoanAmtBarModel.getAxis(AxisType.Y);

        return depositLoanAmtBarModel;
    }

    private BarChartModel createDepositLoanIntBarModel() {
        depositLoanIntBarModel = initDepositLoanIntBarModel();
        depositLoanIntBarModel.setTitle("Total Deposit Interest Amount & Total Loan Interest Earned");
        depositLoanIntBarModel.setLegendPosition("ne");

        Axis xAxis = depositLoanIntBarModel.getAxis(AxisType.X);
        Axis yAxis = depositLoanIntBarModel.getAxis(AxisType.Y);

        return depositLoanIntBarModel;
    }

    private BarChartModel initDepositLoanAmtBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("Deposit Amount");
        ChartSeries series2 = new ChartSeries();
        series2.setLabel("Loan Amount");

        List<BankFactTable> bfts = bankFactTableSessionBean.getListBankFactTables();

        for (BankFactTable bft : bfts) {
            series1.set(bft.getMonthOfDate(), bft.getTotalDepositAmount());
            series2.set(bft.getMonthOfDate(), bft.getTotalLoanAmount());
        }

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }

    private BarChartModel initDepositLoanIntBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("Total Deposit Interest Amount");
        ChartSeries series2 = new ChartSeries();
        series2.setLabel("Total Loan Interest Earned");

        List<BankFactTable> bfts = bankFactTableSessionBean.getListBankFactTables();

        for (BankFactTable bft : bfts) {
            series1.set(bft.getMonthOfDate(), bft.getTotalDepositInterestAmount());
            series2.set(bft.getMonthOfDate(), bft.getTotalLoanInterestEarned());
        }

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }

    private void createBadLoanPieModel() {
        badLoanPieModel = new PieChartModel();

        badLoanPieModel.set("Default Loan", 325);
        badLoanPieModel.set("Non-Default Loan", 900);

        badLoanPieModel.setTitle("Default Rate");
        badLoanPieModel.setLegendPosition("e");
        badLoanPieModel.setFill(true);
        badLoanPieModel.setShowDataLabels(true);
        badLoanPieModel.setDiameter(150);
    }

    public BarChartModel getDepositLoanAmtBarModel() {
        return depositLoanAmtBarModel;
    }

    public Double getBankTotalLoanAmount() {
        return bankTotalLoanAmount;
    }

    public void setBankTotalLoanAmount(Double bankTotalLoanAmount) {
        this.bankTotalLoanAmount = bankTotalLoanAmount;
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

    public Long getBankTotalActiveDepositAcct() {
        return bankTotalActiveDepositAcct;
    }

    public void setBankTotalActiveDepositAcct(Long bankTotalActiveDepositAcct) {
        this.bankTotalActiveDepositAcct = bankTotalActiveDepositAcct;
    }

    public Long getBankTotalNewDepositAcct() {
        return bankTotalNewDepositAcct;
    }

    public void setBankTotalNewDepositAcct(Long bankTotalNewDepositAcct) {
        this.bankTotalNewDepositAcct = bankTotalNewDepositAcct;
    }

    public Double getBankTotalDepositAmount() {
        return bankTotalDepositAmount;
    }

    public void setBankTotalDepositAmount(Double bankTotalDepositAmount) {
        this.bankTotalDepositAmount = bankTotalDepositAmount;
    }

    public Double getBankDepositInterestAmount() {
        return bankDepositInterestAmount;
    }

    public void setBankDepositInterestAmount(Double bankDepositInterestAmount) {
        this.bankDepositInterestAmount = bankDepositInterestAmount;
    }

    public Long getBankTotalLoanAcct() {
        return bankTotalLoanAcct;
    }

    public void setBankTotalLoanAcct(Long bankTotalLoanAcct) {
        this.bankTotalLoanAcct = bankTotalLoanAcct;
    }

    public Long getBankTotalNewLoanAcct() {
        return bankTotalNewLoanAcct;
    }

    public void setBankTotalNewLoanAcct(Long bankTotalNewLoanAcct) {
        this.bankTotalNewLoanAcct = bankTotalNewLoanAcct;
    }

    public Long getBankTotalDefaultLoanAcct() {
        return bankTotalDefaultLoanAcct;
    }

    public void setBankTotalDefaultLoanAcct(Long bankTotalDefaultLoanAcct) {
        this.bankTotalDefaultLoanAcct = bankTotalDefaultLoanAcct;
    }

    public Double getBankLoanInterestEarned() {
        return bankLoanInterestEarned;
    }

    public void setBankLoanInterestEarned(Double bankLoanInterestEarned) {
        this.bankLoanInterestEarned = bankLoanInterestEarned;
    }

    public Double getBankLoanInterestUnearned() {
        return bankLoanInterestUnearned;
    }

    public void setBankLoanInterestUnearned(Double bankLoanInterestUnearned) {
        this.bankLoanInterestUnearned = bankLoanInterestUnearned;
    }

}
