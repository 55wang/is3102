/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.setting;

import entity.staff.Role;
import java.io.Serializable;
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

    private Role role = SessionUtils.getStaff().getRole();
    /**
     * Creates a new instance of StaffViewAccessManagedBean
     */
    public StaffViewAccessManagedBean() {
    }

    /**
     * @return the role
     */
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
