/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.common;

import ejb.session.counter.TellerCounterSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.counter.TellerCounter;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "staffCounterLoginManagedBean")
@ViewScoped
public class StaffCounterLoginManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private TellerCounterSessionBeanLocal counterBean;

    private Long counterId = 9L;
    private BigDecimal currentCash = new BigDecimal(10000.0);
    private String username = "general_teller";
    private String password = "password";

    public StaffCounterLoginManagedBean() {
        System.out.println("StaffCounterLoginManagedBean() Created!!");
    }

    @PostConstruct
    public void init() {
        // Set a default super account
        System.out.println("StaffLoginManagedBean @PostConstruct");
        AuditLog a = new AuditLog();
        a.setActivityLog("Teller Counter StaffLoginManagedBean.xhtml");
        a.setFunctionName("StaffLoginManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all customer information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void loginStaff(ActionEvent event) {
        password = "password";    
        
        StaffAccount sa = staffBean.loginAccount(username, HashPwdUtils.hashPwd(password));
        if (sa == null) {
            MessageUtils.displayError("Either username or password is wrong");
        } else {
            SessionUtils.setStaffAccount(sa);
            if (UserUtils.isUserInRole(UserRole.GENERAL_TELLER.toString())) {

                TellerCounter tc = counterBean.getTellerCounterById(counterId);

                if (tc == null) {
                    MessageUtils.displayError("Counter not found");
                } else {

                    tc.setStaffAccount(sa);
                    tc.setCurrentCash(currentCash);
                    counterBean.updateTellerCounter(tc);

                    SessionUtils.setTellerCounter(tc);

                    RedirectUtils.redirect(SessionUtils.getContextPath() + "/counter/counter_summary.xhtml");
                }
            } else {
                RedirectUtils.redirect(SessionUtils.getContextPath() + "/common/permission_denied.xhtml");
            }
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
     * @return the currentCash
     */
    public BigDecimal getCurrentCash() {
        return currentCash;
    }

    /**
     * @param currentCash the currentCash to set
     */
    public void setCurrentCash(BigDecimal currentCash) {
        this.currentCash = currentCash;
    }

    /**
     * @return the counterId
     */
    public Long getCounterId() {
        return counterId;
    }

    /**
     * @param counterId the counterId to set
     */
    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

}
