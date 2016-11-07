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
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import server.utilities.ColorUtils;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Named(value = "viewCardManagedBean")
@ViewScoped
public class ViewCardManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;
    @EJB
    BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;

    private BarChartModel cardTransactionBarModel;
    private LineChartModel cardLatePaymentLineModel;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //card service
    private Long bankTotalCardAcct;
    private Long bankTotalActiveCardAcct;
    private Long bankTotalNewCardAcct;
    private Double bankTotalCardCurrentAmount;
    private Double bankTotalCardOutstandingAmount;

    private Date startDate;
    private Date endDate;

    public ViewCardManagedBean() {
    }

    @PostConstruct
    public void init() {
        startDate = DateUtils.getBeginOfMonth();
        endDate = DateUtils.getEndOfMonth();
        
        System.out.println(startDate);
        System.out.println(endDate);
        
        bankTotalCardAcct = bizIntelligenceSessionBean.getBankTotalCardAcct(endDate);
        bankTotalActiveCardAcct = bizIntelligenceSessionBean.getBankTotalActiveCardAcct(startDate, endDate);
        bankTotalNewCardAcct = bizIntelligenceSessionBean.getBankTotalNewCardAcct(startDate, endDate);
        bankTotalCardCurrentAmount = bizIntelligenceSessionBean.getBankTotalCardCurrentAmount(endDate);
        bankTotalCardOutstandingAmount = bizIntelligenceSessionBean.getBankTotalCardOutstandingAmount(endDate);

        if(bankTotalCardCurrentAmount == null)
            bankTotalCardCurrentAmount = 0.0;
        if(bankTotalCardOutstandingAmount == null)
            bankTotalCardOutstandingAmount = 0.0;
        
        createCardTransactionBarModel();
        createLatePaymentLineModels();
    }
    
    public void viewDataTable() {
        System.out.println(sdf.format(startDate));
        System.out.println(sdf.format(endDate));
        
        bankTotalCardAcct = bizIntelligenceSessionBean.getBankTotalCardAcct(endDate);
        bankTotalActiveCardAcct = bizIntelligenceSessionBean.getBankTotalActiveCardAcct(startDate, endDate);
        bankTotalNewCardAcct = bizIntelligenceSessionBean.getBankTotalNewCardAcct(startDate, endDate);
        bankTotalCardCurrentAmount = bizIntelligenceSessionBean.getBankTotalCardCurrentAmount(endDate);
        bankTotalCardOutstandingAmount = bizIntelligenceSessionBean.getBankTotalCardOutstandingAmount(endDate);
        
        if(bankTotalCardCurrentAmount == null)
            bankTotalCardCurrentAmount = 0.0;
        if(bankTotalCardOutstandingAmount == null)
            bankTotalCardOutstandingAmount = 0.0;
        
        createCardTransactionBarModel();
        createLatePaymentLineModels();
    }

    private void createLatePaymentLineModels() {
        cardLatePaymentLineModel = initLatePaymentLineModel();
        cardLatePaymentLineModel.setTitle("Late Payment Monthly Report");
        cardLatePaymentLineModel.setLegendPosition("e");
        cardLatePaymentLineModel.setShowPointLabels(true);
        cardLatePaymentLineModel.getAxes().put(AxisType.X, new CategoryAxis("Months"));
        Axis yAxis = cardLatePaymentLineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Count");
    }

    private LineChartModel initLatePaymentLineModel() {
        cardLatePaymentLineModel = new LineChartModel();

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("Late Payment Accounts"); //cumulative
        List<BankFactTable> bfts = bankFactTableSessionBean.getListBankFactTables();

        for (BankFactTable bft : bfts) {
            series1.set(bft.getMonthOfDate().toString(), bft.getNumOfBadCardAccount());
        }

        cardLatePaymentLineModel.addSeries(series1);
        cardLatePaymentLineModel.setSeriesColors(ColorUtils.getFlatUIColors(6));
        cardLatePaymentLineModel.setExtender("customExtender");

        return cardLatePaymentLineModel;
    }

    private BarChartModel createCardTransactionBarModel() {
        cardTransactionBarModel = initCardTransactionBarModel();
        cardTransactionBarModel.setTitle("Credit Card Transaction Amount Monthly Report");
        cardTransactionBarModel.setLegendPosition("ne");

        Axis xAxis = cardTransactionBarModel.getAxis(AxisType.X);
        Axis yAxis = cardTransactionBarModel.getAxis(AxisType.Y);

        return cardTransactionBarModel;
    }

    private BarChartModel initCardTransactionBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("Credit Card Settled Transaction Amount");

        List<BankFactTable> bfts = bankFactTableSessionBean.getListBankFactTables();
        for (BankFactTable bft : bfts) {
            series1.set(bft.getMonthOfDate().toString(), bft.getTotalCardCurrentAmount());
        }

        model.addSeries(series1);
        model.setSeriesColors(ColorUtils.getFlatUIColors(0));
        model.setExtender("customExtender");

        return model;
    }

    public BarChartModel getCardTransactionBarModel() {
        return cardTransactionBarModel;
    }

    public void setCardTransactionBarModel(BarChartModel cardTransactionBarModel) {
        this.cardTransactionBarModel = cardTransactionBarModel;
    }

    public LineChartModel getCardLatePaymentLineModel() {
        return cardLatePaymentLineModel;
    }

    public void setCardLatePaymentLineModel(LineChartModel cardLatePaymentLineModel) {
        this.cardLatePaymentLineModel = cardLatePaymentLineModel;
    }

    public Double getBankTotalCardCurrentAmount() {
        return bankTotalCardCurrentAmount;
    }

    public void setBankTotalCardCurrentAmount(Double bankTotalCardCurrentAmount) {
        this.bankTotalCardCurrentAmount = bankTotalCardCurrentAmount;
    }

    public Double getBankTotalCardOutstandingAmount() {
        return bankTotalCardOutstandingAmount;
    }

    public void setBankTotalCardOutstandingAmount(Double bankTotalCardOutstandingAmount) {
        this.bankTotalCardOutstandingAmount = bankTotalCardOutstandingAmount;
    }

    public Long getBankTotalCardAcct() {
        return bankTotalCardAcct;
    }

    public void setBankTotalCardAcct(Long bankTotalCardAcct) {
        this.bankTotalCardAcct = bankTotalCardAcct;
    }

    public Long getBankTotalActiveCardAcct() {
        return bankTotalActiveCardAcct;
    }

    public void setBankTotalActiveCardAcct(Long bankTotalActiveCardAcct) {
        this.bankTotalActiveCardAcct = bankTotalActiveCardAcct;
    }

    public Long getBankTotalNewCardAcct() {
        return bankTotalNewCardAcct;
    }

    public void setBankTotalNewCardAcct(Long bankTotalNewCardAcct) {
        this.bankTotalNewCardAcct = bankTotalNewCardAcct;
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

}
