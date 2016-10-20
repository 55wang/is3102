/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.AdsBannerCampaign;
import entity.crm.EmailCampaign;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wang
 */
@Stateless
public class OperationalCrmSessionBean implements OperationalCrmSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public EmailCampaign createEmailCampaign() {
        return null;
    }

    @Override
    public AdsBannerCampaign createAdsBannerCampaign() {
        return null;
    }
    
    @Override
    public EmailCampaign getEmailCampaignById(Long id) {
        return null;
    }

    @Override
    public AdsBannerCampaign getAdsBannerCampaignId(Long id) {
        return null;
    }
    
    @Override
    public EmailCampaign updateEmailCampaign(EmailCampaign ec) {
        return null;
    }

    @Override
    public AdsBannerCampaign updateAdsBannerCampaign(EmailCampaign bc) {
        return null;
    }
    
    
}
