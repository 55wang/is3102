/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.AdsBannerCampaign;
import entity.crm.EmailCampaign;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface OperationalCrmSessionBeanLocal {

    public EmailCampaign createEmailCampaign();
    public AdsBannerCampaign createAdsBannerCampaign();
    public EmailCampaign getEmailCampaignById(Long id);
    public AdsBannerCampaign getAdsBannerCampaignId(Long id);
    public EmailCampaign updateEmailCampaign(EmailCampaign ec);
    public AdsBannerCampaign updateAdsBannerCampaign(EmailCampaign bc);
}
