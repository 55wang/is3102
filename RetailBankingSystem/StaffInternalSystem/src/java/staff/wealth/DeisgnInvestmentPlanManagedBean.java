/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.FinancialInstrument;
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
import server.utilities.EnumUtils.RiskToleranceLevel;

/**
 *
 * @author VIN-S
 */
@Named(value = "deisgnInvestmentPlanManagedBean")
@ViewScoped
public class DeisgnInvestmentPlanManagedBean implements Serializable{
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
    private RiskToleranceLevel toleranceLevel;

    /**
     * Creates a new instance of DeisgnInvestmentPlanManagedBean
     */
    public DeisgnInvestmentPlanManagedBean() {
    }
    @PostConstruct
    public void init() {
        requestPlanID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("plan");
        requestPlan = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(requestPlanID));
        requestPlan = investmentPlanSessionBean.generateSuggestedInvestmentPlan(requestPlan);
        suggestedFinancialInstruments = requestPlan.getSuggestedFinancialInstruments();
        wms = requestPlan.getWealthManagementSubscriber();   
        toleranceScore = wms.getRiskToleranceScore();
        toleranceLevel = wms.getRiskToleranceLevel();
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
    
        for(int i=0;i<suggestedFinancialInstruments.size();i++){
            if(!suggestedFinancialInstruments.get(i).getWeight().equals(0.0)){
                ChartSeries suggestedinstrument = new ChartSeries();
                suggestedinstrument.setLabel(suggestedFinancialInstruments.get(i).getFi().getName().toString());
                suggestedinstrument.set("Suggested Investment Plan", suggestedFinancialInstruments.get(i).getWeight()*100);
                model.addSeries(suggestedinstrument);
            }
        } 
 
        return model;
    }
    
    public void updateToleranceLevel(){
        if(toleranceScore<18)
            setToleranceLevel(RiskToleranceLevel.LOW_RISK_TOLERANCE);
        else if(toleranceScore<22)
            setToleranceLevel(RiskToleranceLevel.BELOW_AVERAGE_RISK_TOLERANCE);
        else if(toleranceScore<28)
            setToleranceLevel(RiskToleranceLevel.AVERAGE_RISK_TOLERANCE);
        else if(toleranceScore<32)
            setToleranceLevel(RiskToleranceLevel.ABOVE_AVERAGE_RISK_TOLERANCE);
        else
            setToleranceLevel(RiskToleranceLevel.HIGH_RISK_ROLERANCE);
    }
    
    public void reset(){
        requestPlan = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(requestPlanID));
        requestPlan = investmentPlanSessionBean.generateSuggestedInvestmentPlan(requestPlan);
        suggestedFinancialInstruments = requestPlan.getSuggestedFinancialInstruments();
        wms = requestPlan.getWealthManagementSubscriber();   
        toleranceScore = wms.getRiskToleranceScore();
        toleranceLevel = wms.getRiskToleranceLevel();
    }
    
    public void update(){
        
    }
    
    public void submit(){
    
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

    public RiskToleranceLevel getToleranceLevel() {
        return toleranceLevel;
    }

    public void setToleranceLevel(RiskToleranceLevel toleranceLevel) {
        this.toleranceLevel = toleranceLevel;
    }
}
