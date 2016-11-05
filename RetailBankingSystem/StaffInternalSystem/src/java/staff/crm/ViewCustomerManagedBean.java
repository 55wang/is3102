/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.fact.BankFactTableSessionBeanLocal;
import entity.fact.bank.BankFactTable;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author wang
 */
@Named(value = "viewCustomerManagedBean")
@ViewScoped
public class ViewCustomerManagedBean implements Serializable {

    @EJB
    BankFactTableSessionBeanLocal bankFactTableSessionBean;

    private BarChartModel customerAcctBarModel;

    public ViewCustomerManagedBean() {
    }

    @PostConstruct
    public void init() {
        createCustomerAcctBarModel();
    }

    private BarChartModel createCustomerAcctBarModel() {
        customerAcctBarModel = initCardTransactionBarModel();
        customerAcctBarModel.setTitle("Credit Card Transaction Amount Monthly Report");
        customerAcctBarModel.setLegendPosition("ne");

        Axis xAxis = customerAcctBarModel.getAxis(AxisType.X);
        Axis yAxis = customerAcctBarModel.getAxis(AxisType.Y);

        return customerAcctBarModel;
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

    public BarChartModel getCustomerAcctBarModel() {
        return customerAcctBarModel;
    }

    public void setCustomerAcctBarModel(BarChartModel customerAcctBarModel) {
        this.customerAcctBarModel = customerAcctBarModel;
    }

}
