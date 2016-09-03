/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.others;

import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createRoleManagedBean")
@ViewScoped
public class CreateRoleManagedBean implements Serializable {

    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    
    private String roleName;
    private List<Role> roles = new ArrayList<>();
    private Role newRole = new Role();
    
    public CreateRoleManagedBean() {}
    
    @PostConstruct
    public void init() {
        setRoles(staffRoleSessionBean.getAllRoles());
    }
    
    public void addRole(ActionEvent event) {
        if (staffRoleSessionBean.addRole(newRole)) {
            roles.add(newRole);
            newRole = new Role();
            MessageUtils.displayInfo("New Role Added");
        } else {
            MessageUtils.displayInfo("Role already Added");
        }
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
