/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.AdsBannerCampaign;
import entity.crm.MarketingCampaign;
import entity.customer.Customer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface MarketingCampaignSessionBeanLocal {

    public AdsBannerCampaign getDefaultMarketingCampaign();
    public AdsBannerCampaign getMarketingCampaignByCustomer(Customer c);
    public List<MarketingCampaign> getListMarketingCampaigns();
    public MarketingCampaign getMarketingCampaign(Long Id);
    public MarketingCampaign createMarketingCampaign(MarketingCampaign mc);
    public MarketingCampaign updateMarketingCampaign(MarketingCampaign mc);
}
