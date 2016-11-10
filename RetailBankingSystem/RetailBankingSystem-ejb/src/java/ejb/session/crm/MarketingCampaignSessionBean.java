/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.AdsBannerCampaign;
import entity.crm.CustomerGroup;
import entity.crm.EmailCampaign;
import entity.crm.MarketingCampaign;
import entity.customer.Customer;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class MarketingCampaignSessionBean implements MarketingCampaignSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public AdsBannerCampaign getDefaultMarketingCampaign() {
        AdsBannerCampaign abc = new AdsBannerCampaign();
        abc.setAdsTitle("MBS Deposit!");
        abc.setAdsType("deposit");
        abc.setAdsInfo("Merlion Account, the more you save, the more you gain.");
        abc.setAdsExtraInfo("Provide interest rate as high as 2.5%.");
        abc.setLandingPageName("deposit_campaign.xhtml");

        return abc;
    }

    @Override
    public AdsBannerCampaign getMarketingCampaignByCustomer(Customer c) {
        System.out.println("getMarketingCampaignByCustomer()");

        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.id =:inId");
        q.setParameter("inId", c.getId());
        c = (Customer) q.getSingleResult();

        System.out.println("customerGroup size: " + c.getCustomerGroups().size());
        Set<AdsBannerCampaign> adsMcHashSet = new HashSet<>();
        Set<EmailCampaign> emailMcHashSet = new HashSet<>();

        for (CustomerGroup cg : c.getCustomerGroups()) {
            if (!cg.getMarketingCampaigns().isEmpty()) {
                System.out.println("!cg.getMarketingCampaigns() size: " + cg.getMarketingCampaigns().size());
                for (MarketingCampaign mc : cg.getMarketingCampaigns()) {
                    if (mc instanceof AdsBannerCampaign) {
                        adsMcHashSet.add((AdsBannerCampaign) mc);
                        System.out.println("mc added into adshashset");
                    } else if (mc instanceof EmailCampaign) {
                        emailMcHashSet.add((EmailCampaign) mc);
                        System.out.println("mc added into emailhashset");
                    }

                }
            }
        }
        System.out.println("ads marketing campaign size: " + adsMcHashSet.size());
        System.out.println("email marketing campaign size: " + emailMcHashSet.size());

        int size = adsMcHashSet.size();
        if (size > 0) {
            int item = new Random().nextInt(size);
            int i = 0;
            for (AdsBannerCampaign mc : adsMcHashSet) {
                if (i == item) {
                    System.out.println("return mc" + mc.getId());
                    return (AdsBannerCampaign) mc;
                }
                i = i + 1;
            }
        }

        return null;
    }

    @Override
    public Long addViewCount(MarketingCampaign mc) {

        MarketingCampaign tempMc = em.find(MarketingCampaign.class, mc.getId());
        Long newCount = tempMc.getViewCount() + 1L;
        tempMc.setViewCount(newCount);
        em.merge(tempMc);
        return tempMc.getViewCount();
    }

    @Override
    public Long addClickCount(MarketingCampaign mc) {

        MarketingCampaign tempMc = em.find(MarketingCampaign.class, mc.getId());
        Long newCount = tempMc.getClickCount() + 1L;
        tempMc.setClickCount(newCount);
        em.merge(tempMc);
        return tempMc.getClickCount();
    }

    @Override
    public Long addResponseCount(MarketingCampaign mc) {

        MarketingCampaign tempMc = em.find(MarketingCampaign.class, mc.getId());
        Long newCount = tempMc.getNumOfResponse() + 1L;
        tempMc.setNumOfResponse(newCount);
        em.merge(tempMc);
        return tempMc.getNumOfResponse();
    }

    @Override
    public List<MarketingCampaign> getListMarketingCampaigns() {
        Query q = em.createQuery("Select m from MarketingCampaign m");
        return q.getResultList();
    }

    @Override
    public MarketingCampaign getMarketingCampaign(Long Id) {
        Query q = em.createQuery("Select m from MarketingCampaign m Where m.id =:inId");
        q.setParameter("inId", Id);
        return (MarketingCampaign) q.getSingleResult();
    }

    @Override
    public MarketingCampaign createMarketingCampaign(MarketingCampaign mc) {
        em.persist(mc);
        return mc;
    }

    @Override
    public MarketingCampaign updateMarketingCampaign(MarketingCampaign mc) {
        em.merge(mc);
        return mc;
    }

}
