/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.fact.PortfolioFactSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.wealth.DesignInvestmentPlanSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.InvestplanCommunicationSessionBeanLocal;
import entity.customer.WealthManagementSubscriber;
import entity.fact.customer.FinancialInstrumentFactTable;
import entity.wealth.FinancialInstrument;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestmentPlan;
import entity.wealth.InvestplanCommunication;
import entity.wealth.InvestplanMessage;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import server.utilities.ColorUtils;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "investmentPlanDetailManagedBean")
@ViewScoped
public class InvestmentPlanDetailManagedBean implements Serializable{
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private InvestplanCommunicationSessionBeanLocal investplanCommunicationSessionBean;
    @EJB
    private PortfolioFactSessionBeanLocal portfolioFactSessionBean;

    @EJB
    private FinancialInstrumentSessionBeanLocal financialInstrumentSessionBean;
    @EJB
    private DesignInvestmentPlanSessionBeanLocal designInvestmentPlanSessionBean;
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    
    
    @ManagedProperty(value="#{param.plan}")
    private String planID;
    private InvestmentPlan investmentPlan;
    private BarChartModel animatedModel;
    private WealthManagementSubscriber wms;
    private List<FinancialInstrumentAndWeight> suggestedFinancialInstruments;

    //line model
    private LineChartModel lineModel;
    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;
    //dropdown menu
    private String selectedETF;

    private List<String> ETFoptions = new ArrayList<>();
    
    //terminated plan detail
    private HorizontalBarChartModel horizontalBarModel;
    
    //communication module
    private String senderColor = randColor();
    private String receiverColor = randColor();
    private InvestplanCommunication investplanCommunication;
    private InvestplanMessage newMessage = new InvestplanMessage();
    /**
     * Creates a new instance of InvestmentPlanDetailManagedBean
     */
    public InvestmentPlanDetailManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        planID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("plan");
        investmentPlan = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(planID));
        suggestedFinancialInstruments = investmentPlan.getSuggestedFinancialInstruments();
        List<FinancialInstrument> fis = financialInstrumentSessionBean.getAllFinancialInstruments();
        for(int i = 0; i < fis.size(); i++){
            ETFoptions.add(fis.get(i).getETFName());
        }
        wms = investmentPlan.getWealthManagementSubscriber();   
        createAnimatedModel();
        initLineModels();
        if(investmentPlan.getStatus() == InvestmentPlanStatus.TERMINATED)
            createBarModels();
        checkConversation();
    }
    
    private void createAnimatedModel() { 
        animatedModel = initBarModel();
        animatedModel.setTitle("Suggested Investment Plan");
        animatedModel.setAnimate(true);
        animatedModel.setLegendPosition("ne");
        animatedModel.setShowDatatip(false);
        animatedModel.setShowPointLabels(true);
        Axis yAxis = animatedModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(100);
        yAxis.setTickInterval("20");
        yAxis.setLabel("Percentage(%)");
    }
     
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        String barColors = "";
    
        for(int i=0;i<suggestedFinancialInstruments.size();i++){
            if(!suggestedFinancialInstruments.get(i).getWeight().equals(0.0)){
                ChartSeries suggestedinstrument = new ChartSeries();
                suggestedinstrument.setLabel(suggestedFinancialInstruments.get(i).getFi().getName().toString());
                suggestedinstrument.set("Suggested Investment Plan", suggestedFinancialInstruments.get(i).getWeight()*100);
                model.addSeries(suggestedinstrument);
                
                barColors = barColors + ColorUtils.getFlatUIColors(i)+",";
            }
        } 
        
        barColors = barColors.substring(0, barColors.length());
        model.setSeriesColors(barColors);
 
        return model;
    }
    
    public void approve(){
        investmentPlan.setStatus(InvestmentPlanStatus.APPROVAL);
        investmentPlanSessionBean.updateInvestmentPlan(investmentPlan);
        RedirectUtils.redirect("view_investment_plan.xhtml");
    }
    
    public void cancel(){
        investmentPlan.setStatus(InvestmentPlanStatus.CANCELLED);
        investmentPlanSessionBean.updateInvestmentPlan(investmentPlan);
        
        RedirectUtils.redirect("view_investment_plan.xhtml");
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
        lineModel.setTitle(selectedETF);
//        lineModel.setLegendPosition("e");
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
        lineModel.setTitle(selectedETF);
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
    
    private void createBarModels() {
        createHorizontalBarModel();
    }
    
    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();
 
        ChartSeries buyingvalue = new ChartSeries();
        buyingvalue.setLabel("Buying Value");
        buyingvalue.set(new SimpleDateFormat("yyyy-MM-dd").format(investmentPlan.getExecutionDate()), investmentPlan.getPortfolio().getTotalBuyingValue());
        
        ChartSeries soldvalue = new ChartSeries();
        soldvalue.setLabel("Sold Value");
        soldvalue.set(new SimpleDateFormat("yyyy-MM-dd").format(investmentPlan.getSoldDate()), investmentPlan.getPortfolio().getTotalCurrentValue());
        
        
        horizontalBarModel.addSeries(buyingvalue);
        horizontalBarModel.addSeries(soldvalue);
         
        horizontalBarModel.setTitle("Investment Plan");
        horizontalBarModel.setLegendPosition("e");
         
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Value");
        xAxis.setMin(0);
        xAxis.setMax(investmentPlan.getAmountOfInvestment()*1.5);
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Date");   
        
        horizontalBarModel.setSeriesColors(ColorUtils.getFlatUIColors(7)+","+ColorUtils.getFlatUIColors(8));
        horizontalBarModel.setExtender("chartExtender");
        horizontalBarModel.setAnimate(true);
    }
    
    public Boolean isReceiverWms(InvestplanMessage m) {
        return wms.getMainAccount().getCustomer().getFullName().equals(m.getReceiver());
    }
    
    public String getMessageLabel(InvestplanMessage m) {
        return isReceiverWms(m) ? investplanCommunication.getRm().getNameLabel() : wms.getMainAccount().getCustomer().getFullName();
    }
    
    public String randColor() {
        return ColorUtils.randomColor();
    }
    
    public void sendMessage() {
        System.out.println("Message going to be sent ");
        newMessage.setReceiver("relationship_manager");
        newMessage.setSender(wms.getMainAccount().getCustomer().getFullName());
        investplanCommunicationSessionBean.addMessage(investplanCommunication, newMessage);
        investplanCommunication.addMessage(newMessage);
        newMessage = new InvestplanMessage();
    }
    
    public void checkConversation(){
        String communicationID = investplanCommunicationSessionBean.checkIfConversationExists(investmentPlan);
        
        if (communicationID.equals("NOT_FOUND") || communicationID.equals("EXCEPTION")) {
            InvestplanCommunication conversation = new InvestplanCommunication();
            conversation.setWms(wms);
            conversation.setRm(staffAccountSessionBean.getAccountByUsername("relationship_manager"));
            conversation.setIp(investmentPlan);
            conversation.setCreateDate(new Date());
            conversation = investplanCommunicationSessionBean.createCommunication(conversation);
            System.out.println("Message going to be sent ");
            InvestplanMessage im = new InvestplanMessage();
            im.setReceiver(wms.getMainAccount().getCustomer().getFullName());
            im.setSender("relationship_manager");
            im.setMessage("Hello! What can I do to help you?");
            investplanCommunicationSessionBean.addMessage(conversation, im);
            setInvestplanCommunication(conversation);
        }else{
            System.out.println("Existed conversation!");
            setInvestplanCommunication(investplanCommunicationSessionBean.getCommunicationById(Long.parseLong(communicationID)));
        }
    }
    
    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public InvestmentPlan getInvestmentPlan() {
        return investmentPlan;
    }

    public void setInvestmentPlan(InvestmentPlan investmentPlan) {
        this.investmentPlan = investmentPlan;
    }
    
    public BarChartModel getAnimatedModel() {
        return animatedModel;
    }

    public void setAnimatedModel(BarChartModel animatedModel) {
        this.animatedModel = animatedModel;
    }

    public WealthManagementSubscriber getWms() {
        return wms;
    }

    public void setWms(WealthManagementSubscriber wms) {
        this.wms = wms;
    }

    public List<FinancialInstrumentAndWeight> getSuggestedFinancialInstruments() {
        return suggestedFinancialInstruments;
    }

    public void setSuggestedFinancialInstruments(List<FinancialInstrumentAndWeight> suggestedFinancialInstruments) {
        this.suggestedFinancialInstruments = suggestedFinancialInstruments;
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

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
        this.horizontalBarModel = horizontalBarModel;
    }

    public String getSenderColor() {
        return senderColor;
    }

    public void setSenderColor(String senderColor) {
        this.senderColor = senderColor;
    }

    public String getReceiverColor() {
        return receiverColor;
    }

    public void setReceiverColor(String receiverColor) {
        this.receiverColor = receiverColor;
    }

    public InvestplanCommunication getInvestplanCommunication() {
        return investplanCommunication;
    }

    public void setInvestplanCommunication(InvestplanCommunication investplanCommunication) {
        this.investplanCommunication = investplanCommunication;
    }

    public InvestplanMessage getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(InvestplanMessage newMessage) {
        this.newMessage = newMessage;
    }
}
