/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.staff.StaffRoleSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.common.DuplicateStaffRoleException;
import util.exception.common.NoRolesExistException;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createRoleManagedBean")
@ViewScoped
public class CreateRoleManagedBean implements Serializable {

    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;

    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private String roleName;
    private List<Role> roles = new ArrayList<>();
    private Role newRole = new Role();

    public CreateRoleManagedBean() {
    }

    @PostConstruct
    public void init() {
        try {
            roles = staffRoleSessionBean.getAllRoles();
        } catch (NoRolesExistException e) {
            System.out.println("NoRolesExistException CreateRoleManagedBean");
            roles = new ArrayList<>();
        }
        

        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter create_role.xhtml");
        a.setFunctionName("CreateRoleManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all roles");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void addRole(ActionEvent event) {

        AuditLog a = new AuditLog();
        a.setActivityLog("System user add role");
        a.setFunctionName("CreateRoleManagedBean addRole()");

        try {
            Role temp = staffRoleSessionBean.createRole(newRole);

            roles.add(temp);
            newRole = new Role();
            a.setFunctionOutput("SUCCESS");
            MessageUtils.displayInfo("New Role Added");

        } catch (DuplicateStaffRoleException e) {
            a.setFunctionOutput("FAIL");
            MessageUtils.displayInfo("Role already Added");
        }
        utilsBean.persist(a);
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    /**
     * @return the newRole
     */
    public Role getNewRole() {
        return newRole;
    }

    /**
     * @param newRole the newRole to set
     */
    public void setNewRole(Role newRole) {
        this.newRole = newRole;
    }
}
