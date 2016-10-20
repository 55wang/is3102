/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.StaffAccount;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import server.utilities.EnumUtils;
import server.utilities.LoggingUtils;
import utils.SessionUtils;
import utils.UserUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffUserManagedBean")
@SessionScoped
public class StaffUserManagedBean implements Serializable {
    
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private String SUPER_ADMIN = EnumUtils.UserRole.SUPER_ADMIN.toString();
    
    @PostConstruct
    public void init() {
        LoggingUtils.StaffMessageLog(StaffUserManagedBean.class, "StaffUserManagedBean @PostConstruct init");
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter create_staffUser.xhtml");
        a.setFunctionName("StaffUserManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all staff users");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    @PreDestroy
    public void deinit() {
        LoggingUtils.StaffMessageLog(StaffUserManagedBean.class, "StaffUserManagedBean @PostConstruct deinit");
    }
    /**
     * Creates a new instance of StaffUserManagedBean
     */
    public StaffUserManagedBean() {}
    
    
//    public String getRoleName() {
//        return SessionUtils.getStaff().getRole().getRoleName();
//    }
    
    public String getUserName() {
        return SessionUtils.getStaffUsername();
    }
    
    public StaffAccount getStaff() {
        return SessionUtils.getStaff();
    }
    
    public Boolean isUserInRole(String role) {
        return UserUtils.isUserInRole(role);
    }
    
    public Boolean isUserInRoles(String[] roles) {
        return UserUtils.isUserInRoles(roles);
    }

    /**
     * @return the SUPER_ADMIN
     */
    public String getSUPER_ADMIN() {
        return SUPER_ADMIN;
    }

    /**
     * @param SUPER_ADMIN the SUPER_ADMIN to set
     */
    public void setSUPER_ADMIN(String SUPER_ADMIN) {
        this.SUPER_ADMIN = SUPER_ADMIN;
    }
}
