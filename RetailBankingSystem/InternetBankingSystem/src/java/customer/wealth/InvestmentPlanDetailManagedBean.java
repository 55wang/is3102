/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.wealth.DesignInvestmentPlanSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestmentPlan;
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
import server.utilities.EnumUtils;
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
    private DesignInvestmentPlanSessionBeanLocal designInvestmentPlanSessionBean;
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    
    
    @ManagedProperty(value="#{param.plan}")
    private String planID;
    private InvestmentPlan investmentPlan;
    private BarChartModel animatedModel;
    private WealthManagementSubscriber wms;
    private List<FinancialInstrumentAndWeight> suggestedFinancialInstruments;

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
        wms = investmentPlan.getWealthManagementSubscriber();   
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
}
