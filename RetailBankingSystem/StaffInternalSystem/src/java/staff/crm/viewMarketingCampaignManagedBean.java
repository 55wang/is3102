/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.crm.MarketingCampaignSessionBeanLocal;
import entity.crm.MarketingCampaign;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "viewMarketingCampaignManagedBean")
@ViewScoped
public class viewMarketingCampaignManagedBean implements Serializable {

    @EJB
    MarketingCampaignSessionBeanLocal marketingCampaignSessionBean;

    private List<MarketingCampaign> marketingCampaigns;
    
    public viewMarketingCampaignManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        marketingCampaigns = marketingCampaignSessionBean.getListMarketingCampaigns();
    }

    public List<MarketingCampaign> getMarketingCampaigns() {
        return marketingCampaigns;
    }

    public void setMarketingCampaigns(List<MarketingCampaign> marketingCampaigns) {
        this.marketingCampaigns = marketingCampaigns;
    }
}
