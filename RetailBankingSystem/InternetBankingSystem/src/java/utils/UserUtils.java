/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entity.staff.Role;
import entity.staff.StaffAccount;
import javax.servlet.http.HttpSession;
import static utils.SessionUtils.getSession;

/**
 *
 * @author leiyang
 */
public class UserUtils {
    
    // TODO: Method permissions can be used as annotation.
    // https://www.mkyong.com/java/java-custom-annotations-example/
    // http://www.journaldev.com/1789/java-reflection-example-tutorial
    public static Boolean isUserInRole(String role) {
        HttpSession session = getSession();
        if (session != null) {
            StaffAccount sa = SessionUtils.getStaff();
            Role r = sa.getRole();
            return r.getRoleName().equals(role);
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
            Role role = sa.getRole();

            for (String r : roles) {
                if (r.equals(role.getRoleName())) {
                    return true;
                }
            }
            return permitted;
        } else {
            return false;
        }
    }
}
