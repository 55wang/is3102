/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBean;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.staff.StaffAccount;
import entity.wealth.PortfolioModel;
import entity.wealth.InvestmentPlan;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "viewInvestmentRequestManagedBean")
@ViewScoped
public class ViewInvestmentRequestManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    @EJB
    private PortfolioSessionBeanLocal portfolioSessionBean;

    private String searchText;
    private List<InvestmentPlan> requestInvestmentPlans = new ArrayList<InvestmentPlan>();
    private StaffAccount staffAccount;

    /**
     * Creates a new instance of ViewInvestmentRequestManagedBean
     */
    public ViewInvestmentRequestManagedBean() {
    }

    // Followed by @PostConstruct
    @PostConstruct
    public void init() {
        setStaffAccount(staffAccountSessionBean.getAccountByUsername(SessionUtils.getStaffUsername()));
        setRequestInvestmentPlans(investmentPlanSessionBean.getListInvestmentPlansByRM(staffAccount));
    }

    public void search() {
        if (searchText.isEmpty()) {
            requestInvestmentPlans = investmentPlanSessionBean.getListInvestmentPlansByRM(staffAccount);
        } else {
            InvestmentPlan tempRequest = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(searchText));
            if (tempRequest == null) {
                requestInvestmentPlans = new ArrayList<InvestmentPlan>();
                MessageUtils.displayInfo("No results found");
            } else {
                requestInvestmentPlans = new ArrayList<InvestmentPlan>();
                requestInvestmentPlans.add(tempRequest);
            }
        }
    }

    public void start(InvestmentPlan ip) {
        ip.setStatus(EnumUtils.InvestmentPlanStatus.ONGOING);
        investmentPlanSessionBean.updateInvestmentPlan(ip);
        RedirectUtils.redirect("staff-design-investment-plan.xhtml?plan=" + ip.getId());
    }

    public void cancel(InvestmentPlan ip) {

    }

    public void execute(InvestmentPlan ip) {
        //change to execute status
        //create portfolio 
        Portfolio p = new Portfolio();
        p.setExecutedInvestmentPlan(ip);
        p.setWealthManagementSubscriber(ip.getWealthManagementSubscriber());
        portfolioSessionBean.createPortfolio(p);

        ip.setStatus(EnumUtils.InvestmentPlanStatus.EXECUTED);
        ip.setPortfolio(p);
        investmentPlanSessionBean.updateInvestmentPlan(ip);

        RedirectUtils.redirect("staff-update-executed-investment-plan.xhtml?port=" + p.getId());
    }

    public void view(InvestmentPlan ip) {
        RedirectUtils.redirect("staff-view-executed-investment-detail.xhtml?port=" + ip.getPortfolio().getId());
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<InvestmentPlan> getRequestInvestmentPlans() {
        return requestInvestmentPlans;
    }

    public void setRequestInvestmentPlans(List<InvestmentPlan> requestInvestmentPlans) {
        this.requestInvestmentPlans = requestInvestmentPlans;
    }

    public StaffAccount getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
    }

}
