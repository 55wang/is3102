/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import interceptor.audit.Audit;
import ejb.session.common.ChangePasswordSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.crm.MarketingCampaignSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.crm.AdsBannerCampaign;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.HashPwdUtils;
import util.exception.common.UpdateMainAccountException;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerHomeManagedBean")
@ViewScoped
public class CustomerHomeManagedBean implements Serializable {

    @EJB
    private ChangePasswordSessionBeanLocal changePasswordSessionBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    MarketingCampaignSessionBeanLocal marketingCampaignSessionBean;

    private AdsBannerCampaign adsBannerCampaign;
    private Customer customer;
    private String newPwd;
    private Date currentDate = new Date();

    /**
     * Creates a new instance of CustomerHomeManagedBean
     */
    public CustomerHomeManagedBean() {
    }

    @Audit(activtyLog = "home managed")
    public Boolean changePwd() {
        try {
            AuditLog a = new AuditLog();
            a.setActivityLog("Log off at: " + new Date());
            a.setFunctionName("CustomerLogoutManagedBean logoutCustomer()");
            a.setMainAccount((MainAccount) utilsBean.find(MainAccount.class, SessionUtils.getUserId()));
            utilsBean.persist(a);
            customer.getMainAccount().setPassword(HashPwdUtils.hashPwd(newPwd));
            changePasswordSessionBean.changeMainAccountPwd(customer.getMainAccount());
            String msg = "Successful! You have reset your password. ";
            MessageUtils.displayInfo(msg);
            return true;
        } catch (UpdateMainAccountException umax) {
            String msg = "Something went wrong.";
            System.out.print(umax);
            MessageUtils.displayError(msg);
            return false;
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    @PostConstruct
    public void setCustomer() {
        System.out.println("CustomerHomeManagedBean().setCustomer()");
        try{
            String userID = SessionUtils.getUserName();
            if(userID == null)
                adsBannerCampaign = getDefaultAdsBannerCampaign();
            else{
                this.customer = loginSessionBean.getCustomerByUserID(SessionUtils.getUserName());
                adsBannerCampaign = selectAdsBannerCampaign();

                System.out.println("adsBannerCampaign.getAdsTitle(): "+adsBannerCampaign.getAdsTitle());
                System.out.println("adsBannerCampaign.adstype: "+adsBannerCampaign.getAdsType());
                System.out.println("adsBannerCampaign.adsinfo: "+adsBannerCampaign.getAdsInfo());
                System.out.println("adsBannerCampaign.extrainfo: "+adsBannerCampaign.getAdsExtraInfo());
                System.out.println("adsBannerCampaign.landing: "+adsBannerCampaign.getLandingPageName());
            }
        }catch(Exception ex){
            adsBannerCampaign = getDefaultAdsBannerCampaign();
        }
    }

    public AdsBannerCampaign selectAdsBannerCampaign() {
        System.out.println("selectAdsBannerCampaign()");
        if (getTargetedAdsBannerCampaign() != null) {
            System.out.println("getTargetedAdsBannerCampaign()");
            return getTargetedAdsBannerCampaign();
        } else {
            System.out.println("getDefaultAdsBannerCampaign()");
            return getDefaultAdsBannerCampaign();
        }
    }

    public AdsBannerCampaign getDefaultAdsBannerCampaign() {
        return marketingCampaignSessionBean.getDefaultMarketingCampaign();
    }

    public AdsBannerCampaign getTargetedAdsBannerCampaign() {
        return marketingCampaignSessionBean.getMarketingCampaignByCustomer(customer);
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public AdsBannerCampaign getAdsBannerCampaign() {
        return adsBannerCampaign;
    }

    public void setAdsBannerCampaign(AdsBannerCampaign adsBannerCampaign) {
        this.adsBannerCampaign = adsBannerCampaign;
    }

}
