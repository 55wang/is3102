/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.UserRole;
import server.utilities.HashPwdUtils;
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
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
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
        AuditLog a = new AuditLog();
        a.setActivityLog("System StaffLoginManagedBean.xhtml");
        a.setFunctionName("StaffLoginManagedBean @PostConstruct init()");
        a.setInput("Getting all customer information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
//        StaffAccount sa = staffBean.loginAccount("adminadmin", HashPwdUtils.hashPwd("password"));
//        SessionUtils.setStaffAccount(sa);
//        RedirectUtils.redirect(SessionUtils.getContextPath() + "/admin/create_interest.xhtml");
    }

    public void loginStaff(ActionEvent event) {
        StaffAccount sa = staffBean.loginAccount(username, HashPwdUtils.hashPwd(password));
        if (sa == null) {
            MessageUtils.displayError("Either username or password is wrong");
        } else {
            SessionUtils.setStaffAccount(sa);
            if (UserUtils.isUserInRole(UserRole.SUPER_ADMIN.toString())) {
                RedirectUtils.redirect(SessionUtils.getContextPath() + "/admin/create_interest.xhtml");
            } else {
                RedirectUtils.redirect(SessionUtils.getContextPath() + "/message/notification.xhtml");
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
        StaffAccount forgotAccount = staffBean.getAccountByEmail(findUsernameEmail);
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
