/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.fact.FactSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.Portfolio;
import java.io.Serializable;
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
@Named(value = "viewExecutedInvestDetailManagedBean")
@ViewScoped
public class ViewExecutedInvestDetailManagedBean extends ViewPortfolioAbstractBean implements Serializable {

    @EJB
    FactSessionBeanLocal factSessionBean;
    @EJB
    PortfolioSessionBeanLocal portfolioSessionBean;
    @EJB
    CustomerProfileSessionBeanLocal customerProfileSessionBean;

    private SinglePortfolioFactTable portfolioFt;
    private List<SinglePortfolioFactTable> portfolioFtLineGraph;
    private String portfolioID;
    private Portfolio p;
    
    public ViewExecutedInvestDetailManagedBean() {

    }

    @PostConstruct
    public void init() {
        portfolioID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("port");
        if (portfolioID != null && portfolioID.length() > 0) {
            p = portfolioSessionBean.getPortfolioById(Long.parseLong(portfolioID));
        }
        
        //portfolioFt = factSessionBean.getLatestPortfolioFtByCustomerIdPortfolioId(1L, 2L);
        //portfolioFtLineGraph = factSessionBean.getListPortfoliosFtByCustomerIdPortfolioId(1L, 2L);
    }
    
    public void view(){
        RedirectUtils.redirect("staff-update-executed-investment-plan.xhtml?port=" + portfolioID);
    }

    public SinglePortfolioFactTable getPortfolioFt() {
        return portfolioFt;
    }

    public void setPortfolioFt(SinglePortfolioFactTable portfolioFt) {
        this.portfolioFt = portfolioFt;
    }

    public List<SinglePortfolioFactTable> getPortfolioFtLineGraph() {
        return portfolioFtLineGraph;
    }

    public void setPortfolioFtLineGraph(List<SinglePortfolioFactTable> portfolioFtLineGraph) {
        this.portfolioFtLineGraph = portfolioFtLineGraph;
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
