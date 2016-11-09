/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Role;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "editStaffManagedBean")
@ViewScoped
public class EditStaffManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private String staffId;
    private StaffAccount staff;
    private String[] selectedRoles;
    private List<Role> roles = new ArrayList<>();

    /**
     * Creates a new instance of EditStaffManagedBean
     */
    public EditStaffManagedBean() {
    }

    public void init() {
        System.out.println("Staff id is: " + staffId);
        roles = staffRoleSessionBean.getAllRoles();
        staff = staffAccountSessionBean.getAccountByUsername(staffId);
        selectedRoles = new String[roles.size()];

        int index = 0;
        for (Role r : staff.getRoles()) {
            System.out.println(r.getRoleName());
            selectedRoles[index] = r.getRoleName();
            index++;
        }

        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter edit_role.xhtml");
        a.setFunctionName("CreateRoleManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all roles");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void editStaff() {

        // remove all first
        for (Role r : staff.getRoles()) {
            r.getStaffAccounts().remove(staff);
            staffRoleSessionBean.updateRole(r);
        }
        staff.setRoles(new ArrayList<>());
        staffAccountSessionBean.updateAccount(staff);

        for (String s : selectedRoles) {
            Role r = staffRoleSessionBean.findRoleByName(s);
            r.getStaffAccounts().add(staff);
            staff.addRole(r);
            staffRoleSessionBean.updateRole(r);
        }

        staffAccountSessionBean.updateAccount(staff);
        MessageUtils.displayInfo("Updated!");
    }

    /**
     * @return the staffId
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * @param staffId the staffId to set
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
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
     * @return the selectedRoles
     */
    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    /**
     * @param selectedRoles the selectedRoles to set
     */
    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    /**
     * @return the staff
     */
    public StaffAccount getStaff() {
        return staff;
    }

    /**
     * @param staff the staff to set
     */
    public void setStaff(StaffAccount staff) {
        this.staff = staff;
    }

}
