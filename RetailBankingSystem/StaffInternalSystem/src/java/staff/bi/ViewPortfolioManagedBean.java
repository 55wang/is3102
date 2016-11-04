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
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author wang
 */
@Named(value = "viewPortfolioManagedBean")
@ViewScoped
public class ViewPortfolioManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;

    private BarChartModel portfolioAmtBarModel;

    public ViewPortfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
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

        return model;
    }

    public BarChartModel getPortfolioAmtBarModel() {
        return portfolioAmtBarModel;
    }

    public void setPortfolioAmtBarModel(BarChartModel portfolioAmtBarModel) {
        this.portfolioAmtBarModel = portfolioAmtBarModel;
    }

}
