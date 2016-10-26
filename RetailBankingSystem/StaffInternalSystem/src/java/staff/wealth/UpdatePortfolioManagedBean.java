/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import sentiment.Analyze;
import com.google.api.services.language.v1beta1.model.Sentiment;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.wealth.Portfolio;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author wang
 */
@Named(value = "updatePortfolioManagedBean")
@ViewScoped
public class UpdatePortfolioManagedBean implements Serializable {

    @EJB
    PortfolioSessionBeanLocal portfolioSessionBean;
    @EJB
    MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    InvestmentPlanSessionBeanLocal investmentPlanSessionBean;

    private String portfolioID;

    private Portfolio p;

    public UpdatePortfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
        portfolioID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("port");
        System.out.println(portfolioID);
        if (portfolioID != null && portfolioID.length() > 0) {
            p = portfolioSessionBean.getPortfolioById(Long.parseLong(portfolioID));
        }

    }

    public void retrievePortfolio() {
        System.out.println("retrievePortfolio()");
        System.out.println(portfolioID);
        if (portfolioID != null && portfolioID.length() > 0) {
            p = portfolioSessionBean.getPortfolioById(Long.parseLong(portfolioID));
        }
    }

    public void viewPortfolioDetail() {
        System.out.println("viewPortfolioDetail()");
        System.out.println(portfolioID);
        if (portfolioID != null && portfolioID.length() > 0) {
            RedirectUtils.redirect("staff-view-portfolio-detail.xhtml?port=" + portfolioID);
        }
    }

    public void submitUpdateBuyingPortfolio() {
        //when execute btn is pressed from the viewinvestmentplan, it should already create the portfolio
        //here is just merge and update value instead of persist.

        //set current amount and value to buying 
        if(validator()){
            portfolioSessionBean.updatePortfolio(p);
            MessageUtils.displayInfo("Update Successful");
        }
        else{
            MessageUtils.displayError("Please enter all buying values!");
        }   
    }

    public void updateCurrentPortfolio() {
        //when execute btn is pressed from the viewinvestmentplan, it should already create the portfolio
        //here is just merge and update value instead of persist.
        portfolioSessionBean.updatePortfolio(p);
        MessageUtils.displayInfo("Update Successful");
    }
    
    public void reset(){
        for(int i = 0; i < p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().size(); i++)
            p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).setBuyingValuePerShare(0.0);           
        calculate();
    }
    
    public void calculate(){
        for(int i = 0; i < p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().size(); i++){
            if(p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare() != 0.0){
                System.out.println("Name:" + p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getFi().getName());
                System.out.println("Price" + p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare());
                Double newNumberOfShare = 0.0;
                newNumberOfShare = (p.getExecutedInvestmentPlan().getAmountOfInvestment() * p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getWeight())/p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare();
                p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).setBuyingNumberOfShare(newNumberOfShare.intValue());
            }else if(p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare() == 0.0)
                p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).setBuyingNumberOfShare(0);
        }
    }
    
    public Boolean validator(){
        Boolean flag = true;
        
        for(int i = 0; i < p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().size(); i++){
            if(p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare() == 0.0 && p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getWeight() != 0.0)
                flag = false;
        }
        
        return flag;
    }

    public Portfolio getP() {
        return p;
    }

    public void setP(Portfolio p) {
        this.p = p;
    }

    public String getPortfolioID() {
        return portfolioID;
    }

    public void setPortfolioID(String portfolioID) {
        this.portfolioID = portfolioID;
    }

}
