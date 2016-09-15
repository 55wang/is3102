/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.MainAccount;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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

        FacesMessage msgCaptcha = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Correct");
        FacesContext.getCurrentInstance().addMessage(null, msgCaptcha);

        try {
            MainAccount attemptLogin = loginSessionBean.getCustomerByUserID(loginAccount.getUserID()).getMainAccount();
            if (attemptLogin.getStatus().equals(MainAccount.StatusType.PENDING)) {
                String msg = "Check your email and activate the account";
                MessageUtils.displayInfo(msg);
            } else if (attemptLogin.getStatus().equals(MainAccount.StatusType.FREEZE)) {
                String msg = "Your account has been freezed.";
                MessageUtils.displayInfo(msg);
            } else if (attemptLogin.getStatus().equals(MainAccount.StatusType.ACTIVE)) {
                Long userID = loginSessionBean.loginAccount(loginAccount.getUserID(), loginAccount.getPassword()).getId();
                String userName = loginSessionBean.loginAccount(loginAccount.getUserID(), loginAccount.getPassword()).getUserID();
                SessionUtils.setUserId(userID);
                SessionUtils.setUserName(userName);
                RedirectUtils.redirect("customer_home.xhtml");
            }
        } catch (NullPointerException e) {
            String msg = "Account not exists or password incorrect.";
            MessageUtils.displayError(msg);
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
