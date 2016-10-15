/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import entity.customer.MainAccount;
import entity.wealth.InvestmentPlan;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "viewInvestmentPlanManagedBean")
@ViewScoped
public class ViewInvestmentPlanManagedBean implements Serializable{
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    
    private String searchText;
    private List<InvestmentPlan> investmentPlans = new ArrayList<InvestmentPlan>();
    private MainAccount mainAccount;

    /**
     * Creates a new instance of ViewInvestmentPlanManagedBean
     */
    public ViewInvestmentPlanManagedBean() {
    }
    
    // Followed by @PostConstruct
    @PostConstruct
    public void init() {
        setMainAccount(mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName()));
        setInvestmentPlans(investmentPlanSessionBean.getInvestmentPlanByMainAccount(mainAccount));
    }
    
    public void search() {
        if (searchText.isEmpty()){
            investmentPlans = investmentPlanSessionBean.getInvestmentPlanByMainAccount(mainAccount);
        }else{
            InvestmentPlan tempRequest = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(searchText));
            if(tempRequest == null){
                investmentPlans = new ArrayList<InvestmentPlan>(); 
                MessageUtils.displayInfo("No results found");
            }else{      
                investmentPlans = new ArrayList<InvestmentPlan>();
                investmentPlans.add(tempRequest);
            }
        }
    }
    
    public void cancel(InvestmentPlan ip){
        ip.setStatus(InvestmentPlanStatus.CANCELLED);
        investmentPlanSessionBean.updateInvestmentPlan(ip);
        investmentPlans.remove(ip);
    }
    
    public void viewDetail(InvestmentPlan ip){
        RedirectUtils.redirect("investment_plan_detail.xhtml?plan="+ip.getId());
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<InvestmentPlan> getInvestmentPlans() {
        return investmentPlans;
    }

    public void setInvestmentPlans(List<InvestmentPlan> investmentPlans) {
        this.investmentPlans = investmentPlans;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }
}
