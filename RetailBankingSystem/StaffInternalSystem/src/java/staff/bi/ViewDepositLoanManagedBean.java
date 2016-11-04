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
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author wang
 */
@Named(value = "viewDepositLoanManagedBean")
@ViewScoped
public class ViewDepositLoanManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;

    private BarChartModel depositLoanAmtBarModel;
    private BarChartModel depositLoanIntBarModel;
    private PieChartModel badLoanPieModel;

    public ViewDepositLoanManagedBean() {
    }

    @PostConstruct
    public void init() {
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
        depositLoanIntBarModel.setTitle("Avg Deposit Interest & Avg Loan Interest");
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
        series1.setLabel("Avg Deposit Interest");
        ChartSeries series2 = new ChartSeries();
        series2.setLabel("Avg Loan Interest");

        List<BankFactTable> bfts = bankFactTableSessionBean.getListBankFactTables();

        for (BankFactTable bft : bfts) {
            series1.set(bft.getMonthOfDate(), bft.getAvgDepositInterestRate());
            series2.set(bft.getMonthOfDate(), bft.getAvgLoanInterestRate());
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

}
