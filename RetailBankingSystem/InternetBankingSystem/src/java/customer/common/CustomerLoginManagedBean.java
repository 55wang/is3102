/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.customer.MainAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.HashPwdUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerLoginManagedBean")
@ViewScoped
public class CustomerLoginManagedBean implements Serializable {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;

//    @EJB
//    private AuditSessionBeanLocal auditSessionBean;

    private MainAccount loginAccount = new MainAccount();

    private String findUsernameEmail;
    private String findPasswordEmail;

    /**
     * Creates a new instance of CustomerLoginManagedBean
     */
    public CustomerLoginManagedBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("CustomerLoginManagedBean @PostConstruct");

//        EventBus eventBus = EventBusFactory.getDefault().eventBus();
//        FacesMessage m = new FacesMessage("Test Customer", "Content");
//        eventBus.publish(NOTIFY_CHANNEL, m);
        
//        MainAccount ma = loginSessionBean.getCustomerByUserID("c1234567").getMainAccount();
//        String userID = Long.toString(ma.getId());
//        String userName = ma.getUserID();
//        SessionUtils.setUserId(userID);
//        SessionUtils.setUserName(userName);
//        RedirectUtils.redirect(SessionUtils.getContextPath() + "/customer_cms/customer_home.xhtml");
        
        MainAccount ma = loginSessionBean.getCustomerByUserID("c1234567").getMainAccount();
        String userID = Long.toString(ma.getId());
        String userName = ma.getUserID();
        SessionUtils.setUserId(userID);
        SessionUtils.setUserName(userName);

        SessionUtils.setTokenAuthentication(Boolean.FALSE);
        RedirectUtils.redirect("/InternetBankingSystem/customer_deposit/deposit_account_summary.xhtml");

    }

    public MainAccount getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(MainAccount loginAccount) {
        this.loginAccount = loginAccount;
    }

//    @Audit(activtyLog = "login customer account")
    public String loginCustomer(String username, /*@FullHidden*/ String password) {
        MainAccount ma = null;

        try {
            MainAccount attemptLogin = loginSessionBean.getCustomerByUserID(username).getMainAccount();
            if (attemptLogin.getStatus().equals(EnumUtils.StatusType.PENDING)) {
                String msg = "Check your email and activate the account";
                MessageUtils.displayInfo(msg);
                return "FAIL";
            } else if (attemptLogin.getStatus().equals(EnumUtils.StatusType.FREEZE)) {
                String msg = "Your account has been freezed.";
                MessageUtils.displayInfo(msg);
                return "FAIL";
            } else if (attemptLogin.getStatus().equals(EnumUtils.StatusType.ACTIVE)) {
                ma = loginSessionBean.loginAccount(username, HashPwdUtils.hashPwd(password));
                String userID = Long.toString(ma.getId());
                String userName = ma.getUserID();
                SessionUtils.setUserId(userID);
                SessionUtils.setUserName(userName);
//                RedirectUtils.redirect("../customer_cms/customer_home.xhtml");
                return "SUCCESS";
            }
        } catch (NullPointerException e) {
            String msg = "Account not exists or password incorrect.";
            MessageUtils.displayError(msg);
        }
        return "FAIL";
    }

    public void forgotUserID() {
        MainAccount forgotAccount = null;
        forgotAccount = loginSessionBean.getMainAccountByEmail(findUsernameEmail);
        if (forgotAccount != null) {
            if (emailServiceSessionBean.sendUserIDforForgottenCustomer(findUsernameEmail, forgotAccount)) {
                String msg = "User ID has been sent to your email.";
                MessageUtils.displayInfo(msg);
            } else {
                String msg = "Email sent fail. Please try again";
                MessageUtils.displayError(msg);
            }
        } else {
            String msg = "The email is not registered.";
            MessageUtils.displayError(msg);
        }
    }

    public void forgotPassword() {
        MainAccount forgotAccount = null;
        forgotAccount = loginSessionBean.getMainAccountByEmail(findPasswordEmail);
        if (forgotAccount != null) {
            if (emailServiceSessionBean.sendResetPwdLinkforForgottenCustomer(findPasswordEmail, forgotAccount)) {
                String msg = "Check your email and reset password.";
                MessageUtils.displayInfo(msg);
            } else {
                String msg = "Email sent fail. Please try again";
                MessageUtils.displayError(msg);
            }
        } else {
            String msg = "The email is not registered.";
            MessageUtils.displayError(msg);
        }
    }

    public void backtoLogin() {
        RedirectUtils.redirect("customer_login.xhtml");
    }

    public String getFindUsernameEmail() {
        return findUsernameEmail;
    }

    public void setFindUsernameEmail(String findUsernameEmail) {
        this.findUsernameEmail = findUsernameEmail;
    }

    public String getFindPasswordEmail() {
        return findPasswordEmail;
    }

    public void setFindPasswordEmail(String findPasswordEmail) {
        this.findPasswordEmail = findPasswordEmail;
    }

}
