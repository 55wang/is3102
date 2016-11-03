/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bi;

import ejb.session.fact.BizIntelligenceSessionBeanLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author wang
 */
@Named(value = "viewBIManagedBean")
@ViewScoped
public class ViewBIManagedBean implements Serializable {

    @EJB
    private BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;

    private LineChartModel depositLoanAmtLineModel;
    private LineChartModel depositLoanIntLineModel;
    private PieChartModel badLoanPieModel;

    public ViewBIManagedBean() {
    }

    @PostConstruct
    public void init() {
        createDepositLoanAmtLineModels();
        createDepositLoanIntLineModels();
        createBadLoanPieModels();
    }

    public PieChartModel getBadLoanPiePieModel() {
        return badLoanPieModel;
    }

    private void createBadLoanPieModels() {
        createBadLoanPieModel();
    }

    public LineChartModel getDepositLoanAmtLineModel() {
        return depositLoanAmtLineModel;
    }

    public LineChartModel getDepositLoanIntLineModel() {
        return depositLoanIntLineModel;
    }

    private LineChartModel createDepositLoanAmtLineModels() {
        depositLoanAmtLineModel = initDepositLoanAmtLinearModel();
        depositLoanAmtLineModel.setTitle("Total Deposit Amount & Total Loan Amount");
        depositLoanAmtLineModel.setLegendPosition("e");
        Axis yAxis = depositLoanAmtLineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
        return depositLoanAmtLineModel;
    }

    private LineChartModel createDepositLoanIntLineModels() {
        depositLoanIntLineModel = initDepositLoanIntLinearModel();
        depositLoanIntLineModel.setTitle("Total Deposit Interest & Total Loan Interest");
        depositLoanIntLineModel.setLegendPosition("e");
        Axis yAxis = depositLoanIntLineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
        return depositLoanIntLineModel;
    }

    private LineChartModel initDepositLoanAmtLinearModel() {
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Deposit Amount");

        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Loan Amount");

        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }

    private LineChartModel initDepositLoanIntLinearModel() {
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Deposit Amount");

        series1.set(1, 3);
        series1.set(2, 2);
        series1.set(3, 1);
        series1.set(4, 4);
        series1.set(5, 5);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Loan Amount");

        series2.set(1, 1);
        series2.set(2, 2);
        series2.set(3, 3);
        series2.set(4, 4);
        series2.set(5, 5);

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

}
