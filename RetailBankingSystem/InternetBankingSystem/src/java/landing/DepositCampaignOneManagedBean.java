/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package landing;

import customer.dams.ExistingCustomerApplicationManagedBean;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.crm.CustomerGroup;
import entity.crm.MarketingCampaign;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "depositCampaignOneManagedBean")
@ViewScoped
public class DepositCampaignOneManagedBean extends MarketingCampaignAbstractBean implements Serializable {

    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    private Customer existingCustomer;
    private final String landingURL = "deposit_campaign.xhtml";
    private String selectedMC;
    private String temp = "";

    public DepositCampaignOneManagedBean() {
    }

    @PostConstruct
    public void init() {
        super.customerViewPage(landingURL);
    }

    public void clickApplyButton() {
        try {
            existingCustomer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
            System.out.println("existingCustomer: " + existingCustomer.getFullName());

            for (CustomerGroup cg : existingCustomer.getCustomerGroups()) {
                for (MarketingCampaign mc : cg.getMarketingCampaigns()) {
                    if (landingURL.equals(mc.getLandingPageName())) {
                        super.addClickCount(mc);
                        selectedMC = mc.getId().toString();
                    }
                }
            }

            Map<String, String> map = new HashMap<>();
            map.put("selectedMC", selectedMC);
            String params = RedirectUtils.generateParameters(map);
            RedirectUtils.redirect("/InternetBankingSystem/personal_apply/existing_apply_deposit.xhtml" + params);
        } catch (Exception ex) {
            existingCustomer = null;
            System.out.println("Not an existing customer");
            RedirectUtils.redirect("/InternetBankingSystem/main_deposit/default.xhtml");
        }

    }

    public Customer getExistingCustomer() {
        return existingCustomer;
    }

    public void setExistingCustomer(Customer existingCustomer) {
        this.existingCustomer = existingCustomer;
    }

    public String getLandingURL() {
        return landingURL;
    }

    public String getSelectedMC() {
        return selectedMC;
    }

    public void setSelectedMC(String selectedMC) {
        this.selectedMC = selectedMC;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

}
