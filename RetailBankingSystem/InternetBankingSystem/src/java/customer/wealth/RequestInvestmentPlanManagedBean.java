/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.fact.PortfolioFactSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.WealthManegementSubscriberSessionBeanLocal;
import entity.customer.MainAccount;
import entity.customer.WealthManagementSubscriber;
import entity.fact.customer.FinancialInstrumentFactTable;
import entity.wealth.FinancialInstrument;
import entity.wealth.InvestmentPlan;
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
import org.primefaces.model.DualListModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "requestInvestmentPlanManagedBean")
@ViewScoped
public class RequestInvestmentPlanManagedBean implements Serializable {
    @EJB
    private PortfolioFactSessionBeanLocal portfolioFactSessionBean;

    @EJB
    private WealthManegementSubscriberSessionBeanLocal wealthManegementSubscriberSessionBean;
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    @EJB
    private FinancialInstrumentSessionBeanLocal financialInstrumentSessionBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    
    
    private WealthManagementSubscriber wms;
    private MainAccount mainAccount;
    private InvestmentPlan newInvestmenPlan = new InvestmentPlan();
    private List<FinancialInstrument> allFinancialInstruments;
    private DualListModel<FinancialInstrument> financialInstruments;
    private Double chargeFee = 0.0;
    private Integer investAmount = 500;
    
    //line model
    private LineChartModel lineModel;
    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;
    private List<FinancialInstrument> allfis;
    //dropdown menu
    private String selectedETF;

    private List<String> ETFoptions = new ArrayList<>();

    /**
     * Creates a new instance of RequestInvestmentPlanManagedBean
     */
    public RequestInvestmentPlanManagedBean() {
    }
    
    // Followed by @PostConstruct
    @PostConstruct
    public void init() {
        setMainAccount(mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName()));
        setWms(mainAccount.getWealthManagementSubscriber());
        setAllFinancialInstruments(financialInstrumentSessionBean.getAllFinancialInstruments());
        chargeFee = wms.getMonthlyAdvisoryFee();
        
        List<FinancialInstrument> selectedFinancialInstruments = new ArrayList<FinancialInstrument>();
        financialInstruments = new DualListModel<FinancialInstrument>(allFinancialInstruments, selectedFinancialInstruments);     
        
        allfis = financialInstrumentSessionBean.getAllFinancialInstruments();
        for(int i = 0; i < allfis.size(); i++){
            ETFoptions.add(allfis.get(i).getETFName());
        }
        
        initLineModels();
    }
    
    public void submit(){

            List<FinancialInstrument> targetFI = new ArrayList<FinancialInstrument>();
            for(int i = 0; i < financialInstruments.getTarget().size(); i++)
                for(int j=0;j<allFinancialInstruments.size();j++)
                    if(allFinancialInstruments.get(j).getName().getValue().equals(financialInstruments.getTarget().get(i)))
                        targetFI.add(allFinancialInstruments.get(j));

            newInvestmenPlan.setAmountOfInvestment(investAmount);
            newInvestmenPlan.setPreferedFinancialInstrument(targetFI);
            newInvestmenPlan.setWealthManagementSubscriber(wms);
            newInvestmenPlan.setStatus(EnumUtils.InvestmentPlanStatus.PENDING);
            calculateCharge();
            
            wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(wms);

            RedirectUtils.redirect("view_investment_plan.xhtml");

    }
    
    public void calculateCharge(){
        List<InvestmentPlan> ips = wms.getInvestmentPlans();
        Integer totalInvest = 0;
        for(int i = 0; i < ips.size(); i++){
            if(ips.get(i).getStatus() == InvestmentPlanStatus.EXECUTED){
                totalInvest += ips.get(i).getAmountOfInvestment();
            }
        }
        chargeFee = (totalInvest + investAmount -10000)*0.0025*30/365;
    }
    
    public void onDropDownChange() {

        System.out.println("on drop down changed");
        System.out.println(selectedETF);
        if (!selectedETF.equals("None")) {
            System.out.println("activate model");
            createLineModels(selectedETF);
        }
    }
    
    public LineChartModel getLineModel() {
        return lineModel;
    }

    private LineChartModel createLineModels(String selectedETF) {
        lineModel = createLinearModel(selectedETF);
        
        for(int i = 0; i < allfis.size(); i++){
            if(allfis.get(i).getETFName().equals(selectedETF))
                lineModel.setTitle("According Asset Class: " + allfis.get(i).getName());
        }
        
        Axis yAxis = lineModel.getAxis(AxisType.Y);

        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setTickFormat("%Y-%m-%d");
        lineModel.getAxes().put(AxisType.X, axis);
        return lineModel;
    }

    private LineChartModel createLinearModel(String selectedETF) {
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
        List<FinancialInstrumentFactTable> fif = portfolioFactSessionBean.getListFinancialInstrumentFactTableByETFName(selectedETF);
        System.out.println("fif: " + fif.size());
        SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy-MM-dd");
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("ETF"); //insert id number

//        for (int i = 0; i < data.size(); i++) {
//            JsonArray innerData = (JsonArray) data.getJsonArray(i);
//            System.out.println(innerData.get(0) + " " + innerData.get(1));
//            series1.set(innerData.get(0).toString(), innerData.getInt(1));
//        }
        double[] inputData = new double[fif.size()];

        Date lastDate = new Date();
        Double lastValue = null;

        for (int i = 0; i < fif.size(); i++) {
            String dateGraph = simpleformat.format(fif.get(i).getCreationDate());
            System.out.println(fif.get(i).getCreationDate() + " " + simpleformat.format(fif.get(i).getCreationDate()));
            System.out.println(fif.get(i).getInstrumentValueChange());

            inputData[i] = fif.get(i).getInstrumentValueChange();

            series1.set(dateGraph, fif.get(i).getInstrumentValueChange());

            if (i == fif.size() - 1) {
                lastDate = fif.get(i).getCreationDate();
                lastValue = fif.get(i).getInstrumentValueChange();
            }

        }

        model.addSeries(series1);

        return model;
    }
    
    private LineChartModel initLineModels() {
        lineModel = initLinearModel();
        lineModel.setTitle("According Asset Class: US Stocks");
//        lineModel.setLegendPosition("e");
        Axis yAxis = lineModel.getAxis(AxisType.Y);

        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setTickFormat("%Y-%m-%d");
        lineModel.getAxes().put(AxisType.X, axis);
        return lineModel;
    }
    
    private LineChartModel initLinearModel() {
        List<FinancialInstrumentFactTable> fif = portfolioFactSessionBean.getListFinancialInstrumentFactTableByETFName("Vanguard VTI ETF");
        System.out.println("fif: " + fif.size());
        SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy-MM-dd");
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("ETF"); //insert id number

//        for (int i = 0; i < data.size(); i++) {
//            JsonArray innerData = (JsonArray) data.getJsonArray(i);
//            System.out.println(innerData.get(0) + " " + innerData.get(1));
//            series1.set(innerData.get(0).toString(), innerData.getInt(1));
//        }
        double[] inputData = new double[fif.size()];

        Date lastDate = new Date();
        Double lastValue = null;

        for (int i = 0; i < fif.size(); i++) {
            String dateGraph = simpleformat.format(fif.get(i).getCreationDate());
            System.out.println(fif.get(i).getCreationDate() + " " + simpleformat.format(fif.get(i).getCreationDate()));
            System.out.println(fif.get(i).getInstrumentValueChange());

            inputData[i] = fif.get(i).getInstrumentValueChange();

            series1.set(dateGraph, fif.get(i).getInstrumentValueChange());

            if (i == fif.size() - 1) {
                lastDate = fif.get(i).getCreationDate();
                lastValue = fif.get(i).getInstrumentValueChange();
            }

        }

        model.addSeries(series1);

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

    public WealthManagementSubscriber getWms() {
        return wms;
    }

    public void setWms(WealthManagementSubscriber wms) {
        this.wms = wms;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public InvestmentPlan getNewInvestmenPlan() {
        return newInvestmenPlan;
    }

    public void setNewInvestmenPlan(InvestmentPlan newInvestmenPlan) {
        this.newInvestmenPlan = newInvestmenPlan;
    }

    public List<FinancialInstrument> getAllFinancialInstruments() {
        return allFinancialInstruments;
    }

    public void setAllFinancialInstruments(List<FinancialInstrument> allFinancialInstruments) {
        this.allFinancialInstruments = allFinancialInstruments;
    }

    public DualListModel<FinancialInstrument> getFinancialInstruments() {
        return financialInstruments;
    }

    public void setFinancialInstruments(DualListModel<FinancialInstrument> financialInstruments) {
        this.financialInstruments = financialInstruments;
    }

    public Double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(Double chargeFee) {
        this.chargeFee = chargeFee;
    }

    public Integer getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(Integer investAmount) {
        this.investAmount = investAmount;
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

    public String getSelectedETF() {
        return selectedETF;
    }

    public void setSelectedETF(String selectedETF) {
        this.selectedETF = selectedETF;
    }

    public List<String> getETFoptions() {
        return ETFoptions;
    }

    public void setETFoptions(List<String> ETFoptions) {
        this.ETFoptions = ETFoptions;
    }

    public List<FinancialInstrument> getAllfis() {
        return allfis;
    }

    public void setAllfis(List<FinancialInstrument> allfis) {
        this.allfis = allfis;
    }
}
