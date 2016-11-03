/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.wealth.DesignInvestmentPlanSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.InvestmentPlan;
import entity.wealth.FinancialInstrumentAndWeight;
import java.io.Serializable;
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
    
    
    @ManagedProperty(value="#{param.plan}")
    private String requestPlanID;
    private InvestmentPlan requestPlan;
    private BarChartModel animatedModel;
    private WealthManagementSubscriber wms;
    private List<FinancialInstrumentAndWeight> suggestedFinancialInstruments;
    private List<Double> suggestedPercentages;
    private Integer toleranceScore;
    private InvestmentRiskLevel riskLevel;

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
}
