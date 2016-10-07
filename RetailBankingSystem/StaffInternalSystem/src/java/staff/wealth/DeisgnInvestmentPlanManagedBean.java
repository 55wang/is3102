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
    private List<FinancialInstrument> suggestedCombination;
    private List<Double> suggestedPercentages;

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
        suggestedCombination = requestPlan.getFinancialInstruments();
        suggestedPercentages = requestPlan.getFinancialInstrumentPecentage();
        wms = requestPlan.getWealthManagementSubscriber();     
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
        yAxis.setLabel("Percentage(%)");
    }
     
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
    
        for(int i=0;i<requestPlan.getFinancialInstruments().size();i++){
            ChartSeries suggestedinstrument = new ChartSeries();
            suggestedinstrument.setLabel(requestPlan.getFinancialInstruments().get(i).getName().toString());
            suggestedinstrument.set("Suggested Investment Plan", requestPlan.getFinancialInstrumentPecentage().get(i)*100);
            model.addSeries(suggestedinstrument);
        } 
 
        return model;
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

    public List<FinancialInstrument> getSuggestedCombination() {
        return suggestedCombination;
    }

    public void setSuggestedCombination(List<FinancialInstrument> suggestedCombination) {
        this.suggestedCombination = suggestedCombination;
    }

    public List<Double> getSuggestedPercentages() {
        return suggestedPercentages;
    }

    public void setSuggestedPercentages(List<Double> suggestedPercentages) {
        this.suggestedPercentages = suggestedPercentages;
    }
}
