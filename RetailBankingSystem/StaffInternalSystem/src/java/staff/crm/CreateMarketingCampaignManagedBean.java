/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.crm.CustomerSegmentationSessionBeanLocal;
import ejb.session.crm.MarketingCampaignSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.crm.AdsBannerCampaign;
import entity.crm.CustomerGroup;
import entity.crm.EmailCampaign;
import entity.crm.MarketingCampaign;
import entity.customer.Customer;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author wang
 */
@Named(value = "createMarketingCampaignManagedBean")
@ViewScoped
public class CreateMarketingCampaignManagedBean implements Serializable {

    @EJB
    MarketingCampaignSessionBeanLocal marketingCampaignSessionBean;
    @EJB
    StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    CustomerSegmentationSessionBeanLocal customerSegmentationSessionBean;
    @EJB
    EmailServiceSessionBeanLocal emailServiceSessionBean;

    private EmailCampaign emailMarketingCampaign;
    private AdsBannerCampaign adsMarketingCampaign;
    private StaffAccount sa = utils.SessionUtils.getStaff();

    private List<CustomerGroup> customerGroupOptions;
    private Long selectedCustomerGroupEmail;
    private Long selectedCustomerGroupAdsBanner;

    public CreateMarketingCampaignManagedBean() {
    }

    @PostConstruct
    public void init() {
        emailMarketingCampaign = new EmailCampaign();
        adsMarketingCampaign = new AdsBannerCampaign();

        customerGroupOptions = customerSegmentationSessionBean.getListCustomerGroup();
    }

    public void addNewAdsBannerMarketingCampaign() {
        try {
            adsMarketingCampaign.setTypeCampaign(EnumUtils.TypeMarketingCampaign.ADSBANNER);
            adsMarketingCampaign.setStaffAccount(sa);

            adsMarketingCampaign.setCustomerGroup(customerSegmentationSessionBean.getCustomerGroup(selectedCustomerGroupAdsBanner));
            adsMarketingCampaign.setNumOfTargetResponse((long)customerSegmentationSessionBean.getCustomerGroup(selectedCustomerGroupAdsBanner).getCustomers().size());
            marketingCampaignSessionBean.createMarketingCampaign(adsMarketingCampaign);
            sa.getMarketingCampaign().add(adsMarketingCampaign);
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
            emailMarketingCampaign.setLandingPageName(emailMarketingCampaign.getLandingPageName());
            emailMarketingCampaign.setStaffAccount(sa);

            emailMarketingCampaign.setCustomerGroup(customerSegmentationSessionBean.getCustomerGroup(selectedCustomerGroupEmail));

            marketingCampaignSessionBean.createMarketingCampaign(emailMarketingCampaign);
            sa.getMarketingCampaign().add(emailMarketingCampaign);
            staffAccountSessionBean.updateAccount(sa);
            
            //send email
            sendMassEmail();
            
            RedirectUtils.redirect(ConstantUtils.STAFF_MC_VIEW_MARKETING_CAMPAIGNS);

        } catch (Exception ex) {
            System.out.println("createMarketingCampaignManagedBean.addNewEmailMarketingCampaign Error");
            System.out.println(ex);
            MessageUtils.displayError("Email Campaign Created Fail!");
        }
    }
    
    public void sendMassEmail() {
        for (Customer c : emailMarketingCampaign.getCustomerGroup().getCustomers()){
            emailServiceSessionBean.sendEmailMarketingCampaign(c.getEmail(), emailMarketingCampaign.getSubjectEmail(), emailMarketingCampaign.getContentEmail(), emailMarketingCampaign.getLandingPageName());
        }
    }

    public MarketingCampaign getEmailMarketingCampaign() {
        return emailMarketingCampaign;
    }

    public void setEmailMarketingCampaign(EmailCampaign emailMarketingCampaign) {
        this.emailMarketingCampaign = emailMarketingCampaign;
    }

    public StaffAccount getSa() {
        return sa;
    }

    public void setSa(StaffAccount sa) {
        this.sa = sa;
    }


    public List<CustomerGroup> getCustomerGroupOptions() {
        return customerGroupOptions;
    }

    public void setCustomerGroupOptions(List<CustomerGroup> customerGroupOptions) {
        this.customerGroupOptions = customerGroupOptions;
    }

    public Long getSelectedCustomerGroupEmail() {
        return selectedCustomerGroupEmail;
    }

    public void setSelectedCustomerGroupEmail(Long selectedCustomerGroupEmail) {
        this.selectedCustomerGroupEmail = selectedCustomerGroupEmail;
    }

    public Long getSelectedCustomerGroupAdsBanner() {
        return selectedCustomerGroupAdsBanner;
    }

    public void setSelectedCustomerGroupAdsBanner(Long selectedCustomerGroupAdsBanner) {
        this.selectedCustomerGroupAdsBanner = selectedCustomerGroupAdsBanner;
    }

    public AdsBannerCampaign getAdsMarketingCampaign() {
        return adsMarketingCampaign;
    }

    public void setAdsMarketingCampaign(AdsBannerCampaign adsMarketingCampaign) {
        this.adsMarketingCampaign = adsMarketingCampaign;
    }

}
