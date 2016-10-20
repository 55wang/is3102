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

    public void updateBuyingPortfolio() {
        //when execute btn is pressed from the viewinvestmentplan, it should already create the portfolio
        //here is just merge and update value instead of persist.

        //set current amount and value to buying 
        portfolioSessionBean.updatePortfolio(p);
        MessageUtils.displayInfo("Update Successful");
    }

    public void updateCurrentPortfolio() {
        //when execute btn is pressed from the viewinvestmentplan, it should already create the portfolio
        //here is just merge and update value instead of persist.
        portfolioSessionBean.updatePortfolio(p);
        MessageUtils.displayInfo("Update Successful");
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
