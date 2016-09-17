/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.others;

import annotation.Audit;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.Role.Permission;
import entity.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
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
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    
    public StaffLoginManagedBean() {
        System.out.println("StaffLoginManagedBean() Created!!");
    }
    
    @PostConstruct
    public void init() {
        // Set a default super account
        System.out.println("StaffLoginManagedBean @PostConstruct");
    }
    
    private String username;
    private String password;

    @Audit( activtyLog = "Login Staff Account" )
    public void loginStaff(ActionEvent event) {
        StaffAccount sa = staffAccountSessionBean.loginAccount(username, HashPwdUtils.hashPwd(password));
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
    
}
