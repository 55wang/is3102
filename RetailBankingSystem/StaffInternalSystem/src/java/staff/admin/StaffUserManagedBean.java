/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Role;
import entity.staff.StaffAccount;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import server.utilities.ConstantUtils;
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
    private String LOAN_OFFICER = EnumUtils.UserRole.LOAN_OFFICIER.toString();
    private final String IP_ADDRESS = ConstantUtils.ipAddress;
    
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
    
    
    public List<Role> getRoles() {
        return SessionUtils.getStaff().getRoles();
    }
    
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

    /**
     * @return the LOAN_OFFICER
     */
    public String getLOAN_OFFICER() {
        return LOAN_OFFICER;
    }

    /**
     * @param LOAN_OFFICER the LOAN_OFFICER to set
     */
    public void setLOAN_OFFICER(String LOAN_OFFICER) {
        this.LOAN_OFFICER = LOAN_OFFICER;
    }

    /**
     * @return the IP_ADDRESS
     */
    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }
}
