/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import entity.staff.StaffAccount;
import entity.wealth.InvestmentPlan;
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
public class ViewInvestmentRequestManagedBean implements Serializable{
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    

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
        setRequestInvestmentPlans(investmentPlanSessionBean.getInvestmentPlanByRM(staffAccount));
    }
    
    public void search() {
        if (searchText.isEmpty()){
            requestInvestmentPlans = investmentPlanSessionBean.getInvestmentPlanByRM(staffAccount);
        }else{
            InvestmentPlan tempRequest = investmentPlanSessionBean.getInvestmentPlanById(Long.parseLong(searchText));
            if(tempRequest == null){
                requestInvestmentPlans = new ArrayList<InvestmentPlan>(); 
                MessageUtils.displayInfo("No results found");
            }else{      
                requestInvestmentPlans = new ArrayList<InvestmentPlan>();
                requestInvestmentPlans.add(tempRequest);
            }
        }
    }
    
    public void start(InvestmentPlan ip){
        ip.setStatus(EnumUtils.InvestmentPlanStatus.ONGOING);
        investmentPlanSessionBean.updateInvestmentPlan(ip);
        RedirectUtils.redirect("staff-design-investment-plan.xhtml?plan="+ip.getId());
    }
    
    public void cancel(InvestmentPlan ip){
    
    }
    
    public void execute(InvestmentPlan ip){
    
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
