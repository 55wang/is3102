/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.common;


import entity.staff.Role;
import entity.staff.StaffAccount;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import utils.SessionUtils;
import utils.UserUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffUserCounterManagedBean")
@SessionScoped
public class StaffUserCounterManagedBean implements Serializable {

    private String GENERAL_TELLER = EnumUtils.UserRole.GENERAL_TELLER.toString();
    private final String IP_ADDRESS = ConstantUtils.ipAddress;
    
    /**
     * Creates a new instance of StaffUserManagedBean
     */
    public StaffUserCounterManagedBean() {}
    
    
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
     * @return the GENERAL_TELLER
     */
    public String getGENERAL_TELLER() {
        return GENERAL_TELLER;
    }

    /**
     * @param GENERAL_TELLER the GENERAL_TELLER to set
     */
    public void setGENERAL_TELLER(String GENERAL_TELLER) {
        this.GENERAL_TELLER = GENERAL_TELLER;
    }

    /**
     * @return the IP_ADDRESS
     */
    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }
}
