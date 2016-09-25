/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.setting;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Role;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffViewAccessManagedBean")
@ViewScoped
public class StaffViewAccessManagedBean implements Serializable {
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private Role role = SessionUtils.getStaff().getRole();
    /**
     * Creates a new instance of StaffViewAccessManagedBean
     */
    public StaffViewAccessManagedBean() {
    }

    /**
     * @return the role
     */
    
    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter StaffViewAccessManagedBean");
        a.setFunctionName("StaffViewAccessManagedBean @PostConstruct init()");
        a.setInput("Getting all StaffViewAccessManagedBean");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
    public Role getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }
    
}
