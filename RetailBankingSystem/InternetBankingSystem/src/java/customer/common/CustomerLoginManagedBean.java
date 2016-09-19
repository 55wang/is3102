/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.AuditUtils;
import utils.EnumUtils;
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

    @EJB
    private AuditSessionBeanLocal auditSessionBean;

    private MainAccount loginAccount = new MainAccount();

    private String findUsernameEmail;
    private String findPasswordEmail;

    /**
     * Creates a new instance of CustomerLoginManagedBean
     */
    public CustomerLoginManagedBean() {
    }

    public MainAccount getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(MainAccount loginAccount) {
        this.loginAccount = loginAccount;
    }

    public void loginCustomer() {
        String activityLog = "Login Customer Account";
        String functionName = new Object() {
        }.getClass().getEnclosingMethod().toString();
        String input = loginAccount.getUserID() + ", " + AuditUtils.hiddenFullString(loginAccount.getPassword());
        String output = "INITIATING";
        MainAccount ma = null;
        StaffAccount sa = null;

        AuditLog al = AuditUtils.createAuditLog(activityLog, functionName, input, output, ma, sa);
        auditSessionBean.insertAuditLog(al);

        MessageUtils.displayDetailedInfo("Correct", "Correct");

        try {
            MainAccount attemptLogin = loginSessionBean.getCustomerByUserID(loginAccount.getUserID()).getMainAccount();
            if (attemptLogin.getStatus().equals(EnumUtils.StatusType.PENDING)) {
                output = "FAIL";
                String msg = "Check your email and activate the account";
                MessageUtils.displayInfo(msg);
            } else if (attemptLogin.getStatus().equals(EnumUtils.StatusType.FREEZE)) {
                output = "FAIL";
                String msg = "Your account has been freezed.";
                MessageUtils.displayInfo(msg);
            } else if (attemptLogin.getStatus().equals(EnumUtils.StatusType.ACTIVE)) {
                ma = loginSessionBean.loginAccount(loginAccount.getUserID(), HashPwdUtils.hashPwd(loginAccount.getPassword()));
                Long userID = ma.getId();
                String userName = ma.getUserID();
                SessionUtils.setUserId(userID);
                SessionUtils.setUserName(userName);

                output = "SUCCESS";
                RedirectUtils.redirect("../customer_cms/customer_home.xhtml");
            }
        } catch (NullPointerException e) {
            output = "FAIL";
            String msg = "Account not exists or password incorrect.";
            MessageUtils.displayError(msg);
        } finally {
            al = AuditUtils.createAuditLog(activityLog, functionName, input, output, ma, sa);
            auditSessionBean.insertAuditLog(al);
        }

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
