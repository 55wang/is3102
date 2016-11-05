/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.fact.PortfolioFactSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.customer.Customer;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import server.utilities.ColorUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "portfolioManagedBean")
@ViewScoped
public class PortfolioManagedBean implements Serializable {

    @EJB
    LoginSessionBeanLocal loginSessionBean;
    @EJB
    PortfolioSessionBeanLocal portfolioSessionBean;
    @EJB
    CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    PortfolioFactSessionBeanLocal portfolioFactSessionBean;

    private Customer customer;
    private List<Portfolio> portfolios;
    private PieChartModel pieModel;
    private LineChartModel lineModel;
    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;
    private Double chargeFee;

    //dropdown menu
    private String selectedPortfolio;

    private List<String> portfolioOptions = new ArrayList<>();

    public void onDropDownChange() {

        System.out.println("on drop down changed");
        System.out.println(selectedPortfolio);
        if (!selectedPortfolio.equals("None")) {
            System.out.println("activate model");
            String[] selectedPortfolioIdStringSplit = selectedPortfolio.split("\\s+");
            String selectedPortfolioIdString = selectedPortfolioIdStringSplit[selectedPortfolioIdStringSplit.length - 1];
            createLineModels(selectedPortfolioIdString);
        }
    }

    public PortfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
        initDate();
        customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        portfolios = portfolioSessionBean.getListPortfoliosByCustomerId(customer.getId());
        chargeFee = customer.getMainAccount().getWealthManagementSubscriber().getMonthlyAdvisoryFee();
        createPieModels();
        System.out.println("portfolio size: " + portfolios.size());
        try {
            for (Portfolio pOption : portfolios) {
                System.out.println(pOption.getId());
                portfolioOptions.add("Investment Plan " + Long.toString(pOption.getId()));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        System.out.println("### portfolio testing");
        System.out.println(customer.getPortfolioPercentageChange());
        System.out.println(customer.getTotalPortfolioCurrentValue());

    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    private LineChartModel createLineModels(String selectedPortfolioIdString) {
        lineModel = initLinearModel(Long.parseLong(selectedPortfolioIdString));
        lineModel.setTitle("Investment Plan " + selectedPortfolioIdString);
//        lineModel.setLegendPosition("e");
        Axis yAxis = lineModel.getAxis(AxisType.Y);

        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setTickFormat("%Y-%m-%d");
        lineModel.getAxes().put(AxisType.X, axis);
        return lineModel;
    }

    private LineChartModel initLinearModel(Long selectedPortfolioIdString) {
//        String quandl = "https://www.quandl.com/api/v3/datasets/WIKI/FB.json?column_index=4&start_date="+monthStartDate+"&transform=diff&api_key=wh4e1aGKQwZyE4RXWP7s";
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target(quandl);
//
//        // This is the response
//        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
//        System.out.println(jsonString);
//
//        JsonObject dataset = (JsonObject) jsonString.getJsonObject("dataset");
//        JsonArray data = (JsonArray) dataset.getJsonArray("data");

        //sql 
        List<SinglePortfolioFactTable> spf = portfolioFactSessionBean.getListPortfoliosFtByCustomerIdPortfolioId(customer.getId(), selectedPortfolioIdString);
        System.out.println("spf: " + spf.size());
        SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy-MM-dd");
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Portfolio ID"); //insert id number

//        for (int i = 0; i < data.size(); i++) {
//            JsonArray innerData = (JsonArray) data.getJsonArray(i);
//            System.out.println(innerData.get(0) + " " + innerData.get(1));
//            series1.set(innerData.get(0).toString(), innerData.getInt(1));
//        }
        double[] inputData = new double[spf.size()];

        Date lastDate = new Date();
        Double lastValue = null;

        for (int i = 0; i < spf.size(); i++) {
            String dateGraph = simpleformat.format(spf.get(i).getCreationDate());
            System.out.println(spf.get(i).getCreationDate() + " " + simpleformat.format(spf.get(i).getCreationDate()));
            System.out.println(spf.get(i).getTotalCurrentValue());

            inputData[i] = spf.get(i).getTotalCurrentValue();

            series1.set(dateGraph, spf.get(i).getTotalCurrentValue());

            if (i == spf.size() - 1) {
                lastDate = spf.get(i).getCreationDate();
                lastValue = spf.get(i).getTotalCurrentValue();
            }

        }

        model.addSeries(series1);

        //time series
        List<Double> tsResult = portfolioSessionBean.getHoltWinterModel(inputData);
        System.out.println("times series managed bean result: ");
        System.out.println(tsResult.toString());

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Portfolio Forecast"); //insert id number        
        Calendar cal = Calendar.getInstance();
        series2.set(simpleformat.format(lastDate), lastValue);
        for (int i = 0; i < tsResult.size(); i++) {
            cal.setTime(lastDate);
            cal.add(Calendar.DATE, 3);
            lastDate = cal.getTime();
            series2.set(simpleformat.format(lastDate), tsResult.get(i));
        }
        model.addSeries(series2);

        return model;
    }

    public void initDate() {
        Date current = new Date();
        setCurrentDate(new SimpleDateFormat("yyyy-MM-dd").format(current));

        Calendar cStart = Calendar.getInstance();   // this takes current date
        cStart.set(Calendar.DAY_OF_MONTH, 1);
        setMonthStartDate(new SimpleDateFormat("yyyy-MM-dd").format(cStart.getTime()));

        Calendar cEnd = Calendar.getInstance();   // this takes current date
        cEnd.set(Calendar.DAY_OF_MONTH, cEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMonthEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cEnd.getTime()));
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    private void createPieModels() {
        createPieModel();
    }

    private void createPieModel() {
        pieModel = new PieChartModel();

        pieModel.set("Deposit", customer.getTotalDepositAmount());
        pieModel.set("Loan", customer.getTotalLoanAmount());
        pieModel.set("Credit Card Outstanding", customer.getTotalCreditAmount());
        pieModel.set("Investment Value", customer.getTotalPortfolioCurrentValue());

        pieModel.setTitle("Financial Overview");
        pieModel.setLegendPosition("e");
        pieModel.setShowDataLabels(true);
        pieModel.setDiameter(150);
        
        pieModel.setSeriesColors(ColorUtils.getFlatUIColors(7)+","+ColorUtils.getFlatUIColors(9)+","+ColorUtils.getFlatUIColors(11)+","+ColorUtils.getFlatUIColors(15));
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getMonthStartDate() {
        return monthStartDate;
    }

    public void setMonthStartDate(String monthStartDate) {
        this.monthStartDate = monthStartDate;
    }

    public String getMonthEndDate() {
        return monthEndDate;
    }

    public void setMonthEndDate(String monthEndDate) {
        this.monthEndDate = monthEndDate;
    }

    public String getSelectedPortfolio() {
        return selectedPortfolio;
    }

    public void setSelectedPortfolio(String selectedPortfolio) {
        this.selectedPortfolio = selectedPortfolio;
    }

    public List<String> getPortfolioOptions() {
        return portfolioOptions;
    }

    public void setPortfolioOptions(List<String> portfolioOptions) {
        this.portfolioOptions = portfolioOptions;
    }

    public Double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(Double chargeFee) {
        this.chargeFee = chargeFee;
    }
}
