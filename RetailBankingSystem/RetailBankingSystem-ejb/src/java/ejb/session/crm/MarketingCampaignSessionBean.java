/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.MarketingCampaign;
import java.util.List;
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
