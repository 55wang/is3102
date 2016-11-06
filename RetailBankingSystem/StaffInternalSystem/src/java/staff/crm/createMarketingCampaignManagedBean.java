/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.crm.MarketingCampaignSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.crm.AdsBannerCampaign;
import entity.crm.EmailCampaign;
import entity.crm.MarketingCampaign;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import utils.RedirectUtils;

/**
 *
 * @author wang
 */
@Named(value = "createMarketingCampaignManagedBean")
@ViewScoped
public class createMarketingCampaignManagedBean implements Serializable {

    @EJB
    MarketingCampaignSessionBeanLocal marketingCampaignSessionBean;
    @EJB
    StaffAccountSessionBeanLocal staffAccountSessionBean;

    private MarketingCampaign emailMarketingCampaign;
    private MarketingCampaign AdsMarketingCampaign;
    private StaffAccount sa = utils.SessionUtils.getStaff();

    public createMarketingCampaignManagedBean() {
    }

    @PostConstruct
    public void init() {
        emailMarketingCampaign = new EmailCampaign();
        AdsMarketingCampaign = new AdsBannerCampaign();
    }

    public void addNewAdsBannerMarketingCampaign() {
        try {
            AdsMarketingCampaign.setNameCampaign(AdsMarketingCampaign.getNameCampaign());
            AdsMarketingCampaign.setTypeCampaign(EnumUtils.TypeMarketingCampaign.ADSBANNER);
            AdsMarketingCampaign.setDescription(AdsMarketingCampaign.getDescription());
            AdsMarketingCampaign.setLandingPageUrl(AdsMarketingCampaign.getLandingPageUrl());
            AdsMarketingCampaign.setStaffAccount(sa);
            
            AdsMarketingCampaign.setCardFrequencyLevel(AdsMarketingCampaign.getCardFrequencyLevel());

            marketingCampaignSessionBean.createMarketingCampaign(AdsMarketingCampaign);
            sa.getMarketingCampaign().add(AdsMarketingCampaign);
            staffAccountSessionBean.updateAccount(sa);

            RedirectUtils.redirect(ConstantUtils.STAFF_MC_VIEW_MARKETING_CAMPAIGNS);

        } catch (Exception ex) {
            System.out.println("createMarketingCampaignManagedBean.addNewAdsMarketingCampaign Error");
            System.out.println(ex);

            RedirectUtils.redirect(ConstantUtils.STAFF_MC_CREATE_MARKETING_CAMPAIGNS);
        }
    }

    public void addNewEmailMarketingCampaign() {
        try {
            emailMarketingCampaign.setNameCampaign(emailMarketingCampaign.getNameCampaign());
            emailMarketingCampaign.setTypeCampaign(EnumUtils.TypeMarketingCampaign.EMAILCAMPAIGN);
            emailMarketingCampaign.setDescription(emailMarketingCampaign.getDescription());
            emailMarketingCampaign.setLandingPageUrl(emailMarketingCampaign.getLandingPageUrl());
            emailMarketingCampaign.setStaffAccount(sa);
            
            AdsMarketingCampaign.setCardFrequencyLevel(AdsMarketingCampaign.getCardFrequencyLevel());

            marketingCampaignSessionBean.createMarketingCampaign(emailMarketingCampaign);
            sa.getMarketingCampaign().add(emailMarketingCampaign);
            staffAccountSessionBean.updateAccount(sa);

            RedirectUtils.redirect(ConstantUtils.STAFF_MC_VIEW_MARKETING_CAMPAIGNS);

        } catch (Exception ex) {
            System.out.println("createMarketingCampaignManagedBean.addNewEmailMarketingCampaign Error");
            System.out.println(ex);

            RedirectUtils.redirect(ConstantUtils.STAFF_MC_CREATE_MARKETING_CAMPAIGNS);
        }
    }

    public MarketingCampaign getEmailMarketingCampaign() {
        return emailMarketingCampaign;
    }

    public void setEmailMarketingCampaign(MarketingCampaign emailMarketingCampaign) {
        this.emailMarketingCampaign = emailMarketingCampaign;
    }

    public StaffAccount getSa() {
        return sa;
    }

    public void setSa(StaffAccount sa) {
        this.sa = sa;
    }

    public MarketingCampaign getAdsMarketingCampaign() {
        return AdsMarketingCampaign;
    }

    public void setAdsMarketingCampaign(MarketingCampaign AdsMarketingCampaign) {
        this.AdsMarketingCampaign = AdsMarketingCampaign;
    }

}
