/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.RedirectUtils;

/**
 *
 * @author wang
 */
@Named(value = "viewPortfolioManagedBean")
@ViewScoped
public class ViewPortfolioManagedBean extends ViewPortfolioAbstractBean implements Serializable {

    @EJB
    PortfolioSessionBeanLocal portfolioSessionBean;

    private List<Portfolio> portfolios;
    private String searchText;
    private String portfolioID;

    public ViewPortfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
        portfolios = portfolioSessionBean.getListPortfolios();
    }
    
    public void retrievePortfolio() {
        System.out.println("retrievePortfolio()");
        System.out.println(portfolioID);
        portfolios = new ArrayList<Portfolio> ();
        if (portfolioID != null && portfolioID.length() > 0) {
            Portfolio p = portfolioSessionBean.getPortfolioById(Long.parseLong(portfolioID));
            portfolios.add(p);
        }
    }

    public void retrieveAllPortfolio(){
        portfolios = portfolioSessionBean.getListPortfolios();
    }
    
    public void viewPortfolioDetail(Portfolio p) {
        RedirectUtils.redirect("staff-view-executed-investment-detail.xhtml?port=" + p.getId());
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getPortfolioID() {
        return portfolioID;
    }

    public void setPortfolioID(String portfolioID) {
        this.portfolioID = portfolioID;
    }
}
