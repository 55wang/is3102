/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.HashPwdUtils;
import util.exception.common.MainAccountNotExistException;
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
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

//    @EJB
//    private AuditSessionBeanLocal auditSessionBean;
    private MainAccount loginAccount = new MainAccount();

    private String findUsernameEmail;
    private String findPasswordEmail;

    /**
     * Creates a new instance of CustomerLoginManagedBean
     */
    @PostConstruct
    public void init() {
//        loginCustomer("c1234567", "password");
    }

    public CustomerLoginManagedBean() {
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
        System.out.print(username);
        System.out.print(password);

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
                String userID = ma.getId();
                String userName = ma.getUserID();
                SessionUtils.setUserId(userID);
                SessionUtils.setUserName(userName);

                SessionUtils.setTokenAuthentication(Boolean.FALSE);
                System.out.println("Successed");
                AuditLog a = new AuditLog();
                a.setActivityLog("Login in at: " + new Date());
                a.setFunctionName("CustomerLoginManagedBean loginCustomer()");
                a.setMainAccount(ma);
                utilsBean.persist(a);
                RedirectUtils.redirect("../personal_home/customer_home.xhtml");
                return "SUCCESS";
            }
        } catch (NullPointerException e) {
            String msg = "Account not exists or password incorrect.";
            MessageUtils.displayError(msg);
        } catch (Exception e) {
            String msg = "Account not exists or password incorrect.";
            MessageUtils.displayError(msg);
        }
        return "FAIL";
    }

    public void forgotUserID() {
        MainAccount forgotAccount = null;
        try{
            forgotAccount = mainAccountSessionBean.getMainAccountByEmail(findUsernameEmail);
        }catch(MainAccountNotExistException ex){
            System.out.println("forgotUserID.MainAccountNotExistException");
        }
        if (forgotAccount != null) {
            emailServiceSessionBean.sendUserIDforForgottenCustomer(findUsernameEmail, forgotAccount);
            String msg = "User ID has been sent to your email.";
            MessageUtils.displayInfo(msg);
        } else {
            String msg = "The email is not registered.";
            MessageUtils.displayError(msg);
        }
    }

    public void forgotPassword() {
        MainAccount forgotAccount = null;
        try{
            forgotAccount = mainAccountSessionBean.getMainAccountByEmail(findPasswordEmail);
        }catch(MainAccountNotExistException ex){
            System.out.println("forgotUserID.MainAccountNotExistException");
        }
        if (forgotAccount != null) {
            emailServiceSessionBean.sendResetPwdLinkforForgottenCustomer(findPasswordEmail, forgotAccount);
            String msg = "Check your email and reset password.";
            MessageUtils.displayInfo(msg);
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
