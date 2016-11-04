/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bi;

import ejb.session.fact.BankFactTableSessionBeanLocal;
import entity.fact.bank.BankFactTable;
import java.io.Serializable;
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

/**
 *
 * @author wang
 */
@Named(value = "viewCardManagedBean")
@ViewScoped
public class ViewCardManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;

    private BarChartModel cardTransactionBarModel;
    private LineChartModel cardLatePaymentLineModel;

    public ViewCardManagedBean() {
    }

    @PostConstruct
    public void init() {
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
            series1.set(bft.getMonthOfDate().toString(), bft.getTotalCardAmount());
        }

        model.addSeries(series1);

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

}
