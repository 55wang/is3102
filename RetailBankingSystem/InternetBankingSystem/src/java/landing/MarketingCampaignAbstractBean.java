/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package landing;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.crm.MarketingCampaignSessionBeanLocal;
import entity.crm.MarketingCampaign;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
public abstract class MarketingCampaignAbstractBean implements Serializable {

    @EJB
    private MarketingCampaignSessionBeanLocal marketingCampaignSessionBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    public void customerViewPage(String landingURL) {
        System.out.println("customerViewPage");
        // get customer
        // if there is customer then view ++
        // increase view count here
        Customer existingCustomer;
        try {
            existingCustomer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
            System.out.println("existingCustomer: " + existingCustomer.getFullName());

//            for (CustomerGroup cg : existingCustomer.getCustomerGroups()) {
//                for (MarketingCampaign mc : cg.getMarketingCampaigns()) {
//                    if (landingURL.equals(mc.getLandingPageName())) {
//                        addViewCount(mc);
//                    }
//                }
//            }
            List<MarketingCampaign> mcs = marketingCampaignSessionBean.getListMarketingCampaigns();
            for (MarketingCampaign mc : mcs) {
                addViewCount(mc);
            }

        } catch (Exception ex) {
            existingCustomer = null;
            System.out.println("Not an existing customer");
        }
    }

    public Long addViewCount(MarketingCampaign mc) {
        return marketingCampaignSessionBean.addViewCount(mc);
    }

    public Long addClickCount(MarketingCampaign mc) {
        return marketingCampaignSessionBean.addClickCount(mc);
    }

}
