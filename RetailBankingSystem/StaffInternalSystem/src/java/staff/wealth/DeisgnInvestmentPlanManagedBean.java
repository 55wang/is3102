/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.wealth.DesignInvestmentPlanSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.InvestplanCommunicationSessionBeanLocal;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.InvestmentPlan;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestplanCommunication;
import entity.wealth.InvestplanMessage;
import java.io.Serializable;
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
import server.utilities.ColorUtils;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import server.utilities.EnumUtils.InvestmentRiskLevel;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "deisgnInvestmentPlanManagedBean")
@ViewScoped
public class DeisgnInvestmentPlanManagedBean implements Serializable{
    @EJB
    private DesignInvestmentPlanSessionBeanLocal designInvestmentPlanSessionBean;
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private InvestplanCommunicationSessionBeanLocal investplanCommunicationSessionBean;
    
    
    @ManagedProperty(value="#{param.plan}")
    private String requestPlanID;
    private InvestmentPlan requestPlan;
    private BarChartModel animatedModel;
    private WealthManagementSubscriber wms;
    private List<FinancialInstrumentAndWeight> suggestedFinancialInstruments;
    private List<Double> suggestedPercentages;
    private Integer toleranceScore;
    private InvestmentRiskLevel riskLevel;
    
    //communication module
    private String senderColor = randColor();
    private String receiverColor = randColor();
    private InvestplanCommunication investplanCommunication;
    private InvestplanMessage newMessage = new InvestplanMessage();

    /**
     * Creates a new instance of DeisgnInvestmentPlanManagedBean
     */
    public DeisgnInvestmentPlanManagedBean() {
    }
    @PostConstruct
    public void init() {
        requestPlanID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("plan");
        requestPlan = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(requestPlanID));
        requestPlan = designInvestmentPlanSessionBean.generateSuggestedInvestmentPlan(requestPlan);
        suggestedFinancialInstruments = requestPlan.getSuggestedFinancialInstruments();
        wms = requestPlan.getWealthManagementSubscriber();   
        toleranceScore = wms.getRiskToleranceScore();
        updateRiskLevel();
        createAnimatedModel();
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
    
    public void updateRiskLevel(){
        if(toleranceScore<18)
            setRiskLevel(InvestmentRiskLevel.LOW_RISK);
        else if(toleranceScore<22)
            setRiskLevel(InvestmentRiskLevel.BELOW_AVERAGE_RISK);
        else if(toleranceScore<28)
            setRiskLevel(InvestmentRiskLevel.AVERAGE_RISK);
        else if(toleranceScore<32)
            setRiskLevel(InvestmentRiskLevel.ABOVE_AVERAGE_RISK);
        else
            setRiskLevel(InvestmentRiskLevel.HIGH_RISK);
    }
    
    public void requestNewPlan(){
        requestPlan.getWealthManagementSubscriber().setRiskToleranceScore(toleranceScore);
        requestPlan = designInvestmentPlanSessionBean.generateSuggestedInvestmentPlan(requestPlan);
        suggestedFinancialInstruments = requestPlan.getSuggestedFinancialInstruments();
        wms = requestPlan.getWealthManagementSubscriber();   
        createAnimatedModel();
    }
    
    public void reset(){
        requestPlan = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(requestPlanID));
        requestPlan = designInvestmentPlanSessionBean.generateSuggestedInvestmentPlan(requestPlan);
        suggestedFinancialInstruments = requestPlan.getSuggestedFinancialInstruments();
        wms = requestPlan.getWealthManagementSubscriber();   
        toleranceScore = wms.getRiskToleranceScore();
        updateRiskLevel();
        createAnimatedModel();
    }
    
    public void update(){
        if(validator()){
            requestPlan = designInvestmentPlanSessionBean.updateSuggestedInvestmentPlan(requestPlan);
            suggestedFinancialInstruments = requestPlan.getSuggestedFinancialInstruments();
            wms = requestPlan.getWealthManagementSubscriber();   
            toleranceScore = requestPlan.getSystemPredictRisk();
            updateRiskLevel();
            createAnimatedModel();
        }
        else
            reset();
   
    }
    
    public void submit(){
        requestPlan.setStatus(InvestmentPlanStatus.WAITING);
        designInvestmentPlanSessionBean.submitSuggestedInvestmentPlan(requestPlan);
        RedirectUtils.redirect("staff-view-investment-request.xhtml");
    }
    
    private Boolean validator(){
        Double weightSumUp = 0.0;
        
        for(int i=0; i < suggestedFinancialInstruments.size(); i++){
            if(suggestedFinancialInstruments.get(i).getWeight() > 1.0){
                MessageUtils.displayError("Individual weight > 1");
                return false;
            }else if(suggestedFinancialInstruments.get(i).getWeight() < 0.0){
                MessageUtils.displayError("Individual weight < 0");
                return false;
            }
            weightSumUp += suggestedFinancialInstruments.get(i).getWeight();
        }
        
        if(weightSumUp > 1.0){
            MessageUtils.displayError("Total weight > 1");
            return false;
        }else if(weightSumUp < 1.0){
            MessageUtils.displayError("Total weight < 1");
            return false;
        }
        
        return true;
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
        newMessage.setSender("relationship_manager");
        newMessage.setReceiver(wms.getMainAccount().getCustomer().getFullName());
        investplanCommunicationSessionBean.addMessage(investplanCommunication, newMessage);
        investplanCommunication.addMessage(newMessage);
        newMessage = new InvestplanMessage();
    }
    
    public void checkConversation(){
        String communicationID = investplanCommunicationSessionBean.checkIfConversationExists(requestPlan);
        
        if (communicationID.equals("NOT_FOUND") || communicationID.equals("EXCEPTION")) {
            InvestplanCommunication conversation = new InvestplanCommunication();
            conversation.setWms(wms);
            conversation.setRm(staffAccountSessionBean.getAccountByUsername("relationship_manager"));
            conversation.setIp(requestPlan);
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

    public String getRequestPlanID() {
        return requestPlanID;
    }

    public void setRequestPlanID(String requestPlanID) {
        this.requestPlanID = requestPlanID;
    }

    public InvestmentPlan getRequestPlan() {
        return requestPlan;
    }

    public void setRequestPlan(InvestmentPlan requestPlan) {
        this.requestPlan = requestPlan;
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

    public List<Double> getSuggestedPercentages() {
        return suggestedPercentages;
    }

    public void setSuggestedPercentages(List<Double> suggestedPercentages) {
        this.suggestedPercentages = suggestedPercentages;
    }

    public Integer getToleranceScore() {
        return toleranceScore;
    }

    public void setToleranceScore(Integer toleranceScore) {
        this.toleranceScore = toleranceScore;
    }

    public InvestmentRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(InvestmentRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
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
