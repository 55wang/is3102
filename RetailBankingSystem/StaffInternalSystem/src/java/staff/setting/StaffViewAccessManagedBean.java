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
import java.util.List;
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

    private List<Role> roles = SessionUtils.getStaff().getRoles();
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
        a.setFunctionInput("Getting all StaffViewAccessManagedBean");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
}
