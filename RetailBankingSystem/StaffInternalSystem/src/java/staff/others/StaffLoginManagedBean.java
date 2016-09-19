/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.others;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import utils.EnumUtils.Permission;
import utils.HashPwdUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;
import utils.UserUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffLoginManagedBean")
@ViewScoped
public class StaffLoginManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private StaffRoleSessionBeanLocal roleBean;
    @EJB
    private EmailServiceSessionBeanLocal emailBean;
    
    private String username;
    private String password;
    private String findPasswordEmail;
    private String findUsernameEmail;
    
    public StaffLoginManagedBean() {
        System.out.println("StaffLoginManagedBean() Created!!");
    }
    
    @PostConstruct
    public void init() {
        // Set a default super account
        System.out.println("StaffLoginManagedBean @PostConstruct");
    }

    public void loginStaff(ActionEvent event) {
        StaffAccount sa = staffBean.loginAccount(username, HashPwdUtils.hashPwd(password));
        if (sa == null) {
//            AuditUtils.testAuditAnnotation();
            MessageUtils.displayError("Either username or password is wrong");
        } else {
//            AuditUtils.testAuditAnnotation();
            SessionUtils.setStaffAccount(sa);
            if (UserUtils.isUserInRole(Permission.SUPERUSER)) {
                RedirectUtils.redirect(SessionUtils.getContextPath() + "/others/create-interest.xhtml");
            } else if (UserUtils.isUserInRole(Permission.DEPOSIT)) {
                RedirectUtils.redirect(SessionUtils.getContextPath() + "/dams/open-account.xhtml");
            }
        }
    }
    
    public void forgotPassword() {
        StaffAccount forgotAccount = staffBean.getAccountByEmail(findPasswordEmail);
        if (forgotAccount != null) {
            if (emailBean.sendResetPwdLinkforForgottenStaff(findPasswordEmail)) {
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
    
    public void forgotUserID() {
        StaffAccount forgotAccount = staffBean.getAccountByEmail(findPasswordEmail);
        if (forgotAccount != null) {
            if (emailBean.sendUserNameforForgottenStaff(findUsernameEmail, forgotAccount.getUsername())) {
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
    
    public void backtoLogin() {
        RedirectUtils.redirect("../index.xhtml");
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the findPasswordEmail
     */
    public String getFindPasswordEmail() {
        return findPasswordEmail;
    }

    /**
     * @param findPasswordEmail the findPasswordEmail to set
     */
    public void setFindPasswordEmail(String findPasswordEmail) {
        this.findPasswordEmail = findPasswordEmail;
    }

    /**
     * @return the findUsernameEmail
     */
    public String getFindUsernameEmail() {
        return findUsernameEmail;
    }

    /**
     * @param findUsernameEmail the findUsernameEmail to set
     */
    public void setFindUsernameEmail(String findUsernameEmail) {
        this.findUsernameEmail = findUsernameEmail;
    }
    
}
