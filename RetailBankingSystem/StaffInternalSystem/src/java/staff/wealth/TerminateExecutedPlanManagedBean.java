/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import ejb.session.wealth.WealthManegementSubscriberSessionBeanLocal;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestmentPlan;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import server.utilities.ColorUtils;
import static server.utilities.CommonUtils.getDateDiff;
import server.utilities.EnumUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "terminateExecutedPlanManagedBean")
@ViewScoped
public class TerminateExecutedPlanManagedBean implements Serializable{
    @EJB
    private WealthManegementSubscriberSessionBeanLocal wealthManegementSubscriberSessionBean;
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    @EJB
    private PortfolioSessionBeanLocal portfolioSessionBean;
    
    
    private String portfolioID;
    private Portfolio p;
    private InvestmentPlan ip;
    private WealthManagementSubscriber wms;
    private Integer dayDuration;
    private HorizontalBarChartModel horizontalBarModel;
    /**
     * Creates a new instance of TerminateExecutedPlanManagedBean
     */
    public TerminateExecutedPlanManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        portfolioID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("plan");
        System.out.println("TerminateExecutedPlanManagedBeanInit: "+portfolioID);
        if (portfolioID != null && portfolioID.length() > 0) {
            p = portfolioSessionBean.getPortfolioById(Long.parseLong(portfolioID));
        }
        ip = p.getExecutedInvestmentPlan();
        wms = ip.getWealthManagementSubscriber();
        ip.setSoldDate(new Date());
        ip.setTotalETFfee(calculateTotalETF(ip));
        createBarModels();
    }
    
    public void confirmTerminate(){
        ip.setStatus(EnumUtils.InvestmentPlanStatus.TERMINATED);
        for(int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++){
            FinancialInstrumentAndWeight fiaw = ip.getSuggestedFinancialInstruments().get(i);
            fiaw.setSoldValuePerShare(fiaw.getCurrentValuePerShare());
        }
        p.setStatus(EnumUtils.PortfolioStatus.SOLD);
        portfolioSessionBean.updatePortfolio(p);
        ip = investmentPlanSessionBean.updateInvestmentPlan(ip);
        
        Double accumulatedCharge = wms.getAccumulatedAdvisoryFee();
        accumulatedCharge += wms.getMonthlyAdvisoryFee() * getDateDiff(wms.getAdvisoryFeeClearDate(), new Date(), TimeUnit.DAYS)/30;
        System.out.println("SoldCurrentPortfolio.accumulatedCharge: " + accumulatedCharge);
        wms.setAccumulatedAdvisoryFee(accumulatedCharge);
        wms.setAdvisoryFeeClearDate(new Date());
        
        System.out.println("SoldCurrentPortfolio.newAdvisoryFee: " + calculateNewAdvisoryCharge(wms));
        wms.setMonthlyAdvisoryFee(calculateNewAdvisoryCharge(wms));
        
        wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(wms);
        RedirectUtils.redirect("staff-view-investment-request.xhtml");
    }
    
    public Double calculateNewAdvisoryCharge(WealthManagementSubscriber wms){
        List<InvestmentPlan> ips = wms.getInvestmentPlans();
        Integer totalInvest = 0;
        for(int i = 0; i < ips.size(); i++){
            if(ips.get(i).getStatus() == EnumUtils.InvestmentPlanStatus.EXECUTED){
                totalInvest += ips.get(i).getAmountOfInvestment();
            }
        }
        if(totalInvest < 10000) 
            return 0.0;
        else
            return (totalInvest - 10000)*0.0025*30/365;
    }
    
    public Double calculateTotalETF(InvestmentPlan ip){
        Long diffInDays = getDateDiff(ip.getExecutionDate(), ip.getSoldDate(),  TimeUnit.DAYS);
        System.out.println("Day difference: "+diffInDays);
        dayDuration = diffInDays.intValue();
        return ip.getPortfolio().getTotalBuyingValue() * 0.0012*diffInDays/365;
    }
    

    
    private void createBarModels() {
        createHorizontalBarModel();
    }
    
    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();
 
        ChartSeries buyingvalue = new ChartSeries();
        buyingvalue.setLabel("Total Value");
        buyingvalue.set(new SimpleDateFormat("yyyy-MM-dd").format(ip.getSoldDate()), ip.getPortfolio().getTotalCurrentValue());
         buyingvalue.set(new SimpleDateFormat("yyyy-MM-dd").format(ip.getExecutionDate()), ip.getPortfolio().getTotalBuyingValue());
 
        horizontalBarModel.addSeries(buyingvalue);
         
        horizontalBarModel.setTitle("Investment Plan");
        horizontalBarModel.setLegendPosition("e");
         
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Value");
        xAxis.setMin(0);
        xAxis.setMax(ip.getAmountOfInvestment()*1.5);
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Date");   
        
        horizontalBarModel.setSeriesColors(ColorUtils.getFlatUIColors(7)+","+ColorUtils.getFlatUIColors(8));
        horizontalBarModel.setExtender("chartExtender");
    }

    public String getPortfolioID() {
        return portfolioID;
    }

    public void setPortfolioID(String portfolioID) {
        this.portfolioID = portfolioID;
    }

    public Portfolio getP() {
        return p;
    }

    public void setP(Portfolio p) {
        this.p = p;
    }

    public InvestmentPlan getIp() {
        return ip;
    }

    public void setIp(InvestmentPlan ip) {
        this.ip = ip;
    }

    public WealthManagementSubscriber getWms() {
        return wms;
    }

    public void setWms(WealthManagementSubscriber wms) {
        this.wms = wms;
    }

    public Integer getDayDuration() {
        return dayDuration;
    }

    public void setDayDuration(Integer dayDuration) {
        this.dayDuration = dayDuration;
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
        this.horizontalBarModel = horizontalBarModel;
    }
}
