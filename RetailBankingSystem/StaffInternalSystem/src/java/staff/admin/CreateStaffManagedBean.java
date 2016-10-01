/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Role;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.StatusType;
import server.utilities.CommonUtils;
import server.utilities.HashPwdUtils;
import utils.MessageUtils;
import utils.SessionUtils;

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
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
   
    
    private StaffAccount newStaff = new StaffAccount();
    private List<StaffAccount> staffs = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private Map<String, String> selectRoles = new HashMap<>();
    private List<String> selectStatuses = CommonUtils.getEnumList(StatusType.class);
    private String selectedRoleName;
    private String cellSelectedRoleName;
    private String cellSelectedStatus;
    
    public CreateStaffManagedBean() {}
    
    @PostConstruct
    public void init() {
        setStaffs(staffAccountSessionBean.getAllStaffs());
        setRoles(staffRoleSessionBean.getAllRoles());
        for (Role r : roles) {
            getSelectRoles().put(r.getRoleName(), r.getRoleName());
        }
        
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter create_role.xhtml");
        a.setFunctionName("CreateRoleManagedBean @PostConstruct init()");
        a.setInput("Getting all roles");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
    public void addStaff(ActionEvent event) {
        Boolean emailSuccessFlag = true;
        String randomPwd = HashPwdUtils.hashPwd(generatePwd());
        
        try{
            emailServiceSessionBean.sendActivationGmailForStaff(newStaff.getEmail(), randomPwd);
        }catch(Exception ex){
            emailSuccessFlag = false;
        } finally {
            if (emailSuccessFlag) {
                Role r = staffRoleSessionBean.findRoleByName(getSelectedRoleName());
                newStaff.setRole(r);
                newStaff.setPassword(randomPwd);
                createAccount();
            } else {
                MessageUtils.displayInfo("Add Staff Failed");
            }
        }
    }
    
    // private function helper
    private void createAccount() {
        StaffAccount result = staffAccountSessionBean.createAccount(newStaff);
        if (result != null) {
            staffs.add(newStaff);
            newStaff = new StaffAccount();
            MessageUtils.displayInfo("New Role Added");
        } else {
            MessageUtils.displayInfo("Staff already Added");
        }
    }
    
    public void onCellEdit(StaffAccount sa) {
        if (cellSelectedRoleName != null) {
            sa.setRole(staffRoleSessionBean.findRoleByName(getCellSelectedRoleName()));
        } else if (cellSelectedStatus != null) {
            sa.setStatus(StatusType.getEnum(cellSelectedStatus));
        } else {
            return;
        }
        StaffAccount result = staffAccountSessionBean.updateAccount(sa);
        if (result != null) {
            cellSelectedRoleName = null;
            cellSelectedStatus = null;
            MessageUtils.displayInfo(sa.getFullName() + "'s Role Edited");
        } else {
            MessageUtils.displayInfo(sa.getFullName() + "'s Role Not Edited");
        }
    }
    
    private String generatePwd(){
        int pwdLen = 10;
        SecureRandom rnd = new SecureRandom();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(pwdLen);
        for( int i = 0; i < pwdLen; i++ ) 
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    
    // Getter and Setter

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

    /**
     * @return the cellSelectedRoleName
     */
    public String getCellSelectedRoleName() {
        return cellSelectedRoleName;
    }

    /**
     * @param cellSelectedRoleName the cellSelectedRoleName to set
     */
    public void setCellSelectedRoleName(String cellSelectedRoleName) {
        this.cellSelectedRoleName = cellSelectedRoleName;
    }

    /**
     * @return the selectStatuses
     */
    public List<String> getSelectStatuses() {
        return selectStatuses;
    }

    /**
     * @param selectStatuses the selectStatuses to set
     */
    public void setSelectStatuses(List<String> selectStatuses) {
        this.selectStatuses = selectStatuses;
    }

    /**
     * @return the cellSelectedStatus
     */
    public String getCellSelectedStatus() {
        return cellSelectedStatus;
    }

    /**
     * @param cellSelectedStatus the cellSelectedStatus to set
     */
    public void setCellSelectedStatus(String cellSelectedStatus) {
        this.cellSelectedStatus = cellSelectedStatus;
    }
}
