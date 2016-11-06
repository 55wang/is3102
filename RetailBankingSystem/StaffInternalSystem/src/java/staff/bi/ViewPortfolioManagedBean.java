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
import java.text.SimpleDateFormat;
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
import server.utilities.ColorUtils;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Named(value = "viewPortfolioManagedBean")
@ViewScoped
public class ViewPortfolioManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;
    @EJB
    BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;

    private BarChartModel portfolioAmtBarModel;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //portfolio service
    private Long bankTotalWealthManagementSubscriber;
    private Long bankTotalExecutedPortfolio;
    private Long bankNewExecutedPortfolio;
    private Double bankTotalInvestmentAmount;
    private Double bankTotalProfitAmount;

    private Date startDate;
    private Date endDate;

    public ViewPortfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
        startDate = DateUtils.getBeginOfMonth();
        endDate = DateUtils.getEndOfMonth();
        
        System.out.println(startDate);
        System.out.println(endDate);
        
        bankTotalWealthManagementSubscriber = bizIntelligenceSessionBean.getBankTotalWealthManagementSubsciber();
        bankTotalExecutedPortfolio = bizIntelligenceSessionBean.getBankTotalExecutedPortfolio(endDate);
        bankNewExecutedPortfolio = bizIntelligenceSessionBean.getBankNewExecutedPortfolio(startDate, endDate);
        bankTotalInvestmentAmount = bizIntelligenceSessionBean.getBankTotalInvestmentAmount(endDate);
        bankTotalProfitAmount = bizIntelligenceSessionBean.getBankTotalProfitAmount(endDate);
        
        if(bankTotalProfitAmount == null)
            bankTotalProfitAmount = 0.0;

        createCardTransactionBarModel();
    }
    
    public void viewDataTable() {
        System.out.println(sdf.format(startDate));
        System.out.println(sdf.format(endDate));
        
        bankTotalWealthManagementSubscriber = bizIntelligenceSessionBean.getBankTotalWealthManagementSubsciber();
        bankTotalExecutedPortfolio = bizIntelligenceSessionBean.getBankTotalExecutedPortfolio(endDate);
        bankNewExecutedPortfolio = bizIntelligenceSessionBean.getBankNewExecutedPortfolio(startDate, endDate);
        bankTotalInvestmentAmount = bizIntelligenceSessionBean.getBankTotalInvestmentAmount(endDate);
        bankTotalProfitAmount = bizIntelligenceSessionBean.getBankTotalProfitAmount(endDate);
        
        if(bankTotalProfitAmount == null)
            bankTotalProfitAmount = 0.0;

        createCardTransactionBarModel();
    }

    private BarChartModel createCardTransactionBarModel() {
        portfolioAmtBarModel = initPortfolioAmtBarModel();
        portfolioAmtBarModel.setTitle("Portfolio Amount Monthly Report");
        portfolioAmtBarModel.setLegendPosition("ne");

        Axis xAxis = portfolioAmtBarModel.getAxis(AxisType.X);
        Axis yAxis = portfolioAmtBarModel.getAxis(AxisType.Y);

        return portfolioAmtBarModel;
    }

    private BarChartModel initPortfolioAmtBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("Portfolio Amount");

        List<BankFactTable> bfts = bankFactTableSessionBean.getListBankFactTables();

        for (BankFactTable bft : bfts) {
            series1.set(bft.getMonthOfDate(), bft.getTotalPortfolioAmount());
        }

        model.addSeries(series1);
        model.setSeriesColors(ColorUtils.getFlatUIColors(6));
        model.setExtender("customExtender");

        return model;
    }

    public BarChartModel getPortfolioAmtBarModel() {
        return portfolioAmtBarModel;
    }

    public void setPortfolioAmtBarModel(BarChartModel portfolioAmtBarModel) {
        this.portfolioAmtBarModel = portfolioAmtBarModel;
    }

    public Double getBankTotalInvestmentAmount() {
        return bankTotalInvestmentAmount;
    }

    public void setBankTotalInvestmentAmount(Double bankTotalInvestmentAmount) {
        this.bankTotalInvestmentAmount = bankTotalInvestmentAmount;
    }

    public Double getBankTotalProfitAmount() {
        return bankTotalProfitAmount;
    }

    public void setBankTotalProfitAmount(Double bankTotalProfitAmount) {
        this.bankTotalProfitAmount = bankTotalProfitAmount;
    }

    public Long getBankTotalExecutedPortfolio() {
        return bankTotalExecutedPortfolio;
    }

    public void setBankTotalExecutedPortfolio(Long bankTotalExecutedPortfolio) {
        this.bankTotalExecutedPortfolio = bankTotalExecutedPortfolio;
    }

    public Long getBankNewExecutedPortfolio() {
        return bankNewExecutedPortfolio;
    }

    public void setBankNewExecutedPortfolio(Long bankNewExecutedPortfolio) {
        this.bankNewExecutedPortfolio = bankNewExecutedPortfolio;
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

    public Long getBankTotalWealthManagementSubscriber() {
        return bankTotalWealthManagementSubscriber;
    }

    public void setBankTotalWealthManagementSubscriber(Long bankTotalWealthManagementSubscriber) {
        this.bankTotalWealthManagementSubscriber = bankTotalWealthManagementSubscriber;
    }
}
