/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.others;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.Role;
import entity.StaffAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Named(value = "createStaffManagedBean")
@ViewScoped
public class CreateStaffManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    
    private StaffAccount newStaff = new StaffAccount();
    private List<StaffAccount> staffs = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private Map<String, String> selectRoles = new HashMap<>();
    private String selectedRoleName;
    
    public CreateStaffManagedBean() {}
    
    @PostConstruct
    public void init() {
        setStaffs(staffAccountSessionBean.getAllStaffs());
        setRoles(staffRoleSessionBean.getAllRoles());
        for (Role r : roles) {
            getSelectRoles().put(r.getRoleName(), r.getRoleName());
        }
    }
    
    public void addStaff(ActionEvent event) {
        Role r = staffRoleSessionBean.findRoleByName(selectedRoleName);
//        r.addStaffAccount(newStaff);
//        if (staffRoleSessionBean.updateRole(r)) {
//            System.out.println("Staff added to Role");
//        }
        newStaff.setRole(r);
        if (staffAccountSessionBean.createAccount(newStaff)) {
            staffs.add(newStaff);
            newStaff = new StaffAccount();
            MessageUtils.displayInfo("New Role Added");
        } else {
            MessageUtils.displayInfo("Staff already Added");
        }
    }

    /**
     * @return the newStaff
     */
    public StaffAccount getNewStaff() {
        return newStaff;
    }

    /**
     * @param newStaff the newStaff to set
     */
    public void setNewStaff(StaffAccount newStaff) {
        this.newStaff = newStaff;
    }

    /**
     * @return the staffs
     */
    public List<StaffAccount> getStaffs() {
        return staffs;
    }

    /**
     * @param staffs the staffs to set
     */
    public void setStaffs(List<StaffAccount> staffs) {
        this.staffs = staffs;
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
     * @return the selectRoles
     */
    public Map<String, String> getSelectRoles() {
        return selectRoles;
    }

    /**
     * @param selectRoles the selectRoles to set
     */
    public void setSelectRoles(Map<String, String> selectRoles) {
        this.selectRoles = selectRoles;
    }

    /**
     * @return the selectedRoleName
     */
    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    /**
     * @param selectedRoleName the selectedRoleName to set
     */
    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }
}
