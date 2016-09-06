/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.others;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.Role;
import entity.Role.Permission;
import entity.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
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
    
    public StaffLoginManagedBean() {}
    
    @PostConstruct
    public void init() {
        // Set a default super account
        String u = "adminadmin";
        String p = "password";
        StaffAccount sa = staffAccountSessionBean.loginAccount(u, p);
        if (sa == null) {
            StaffAccount superAccount = new StaffAccount();
            superAccount.setUsername(u);
            superAccount.setPassword(p);
            Role r = staffRoleSessionBean.getSuperAdminRole();
            superAccount.setRole(r);
            staffAccountSessionBean.createAccount(superAccount);
        }
    }
    
    private String username;
    private String password;

    public void loginStaff(ActionEvent event) {
        StaffAccount sa = staffAccountSessionBean.loginAccount(username, password);
        if (sa == null) {
            MessageUtils.displayError("Either username or password is wrong");
        } else {
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