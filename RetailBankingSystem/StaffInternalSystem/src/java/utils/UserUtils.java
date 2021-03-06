/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entity.staff.Role;
import entity.staff.StaffAccount;
import java.util.List;
import javax.servlet.http.HttpSession;
import static utils.SessionUtils.getSession;

/**
 *
 * @author leiyang
 */
public class UserUtils {

    // Return true if this user has any of the following roles

    public static Boolean isUserInRole(String role) {
        HttpSession session = getSession();
        if (session != null) {
            StaffAccount sa = SessionUtils.getStaff();
            List<Role> roles = sa.getRoles();
            for (Role r : roles) {
                if (r.getRoleName().equals(role)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    // Return true if this user has any of the following roles
    public static Boolean isUserInRoles(String[] roles) {
        HttpSession session = getSession();
        if (session != null) {
            Boolean permitted = false;
            StaffAccount sa = SessionUtils.getStaff();
            List<Role> staffRoles = sa.getRoles();

            for (Role role : staffRoles) {
                for (String r : roles) {
                    if (r.equals(role.getRoleName())) {
                        permitted = true;
                        break;
                    }
                }
            }

            return permitted;
        } else {
            return false;
        }
    }
}
