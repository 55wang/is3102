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
import utils.EnumUtils.Permission;
import static utils.SessionUtils.getSession;

/**
 *
 * @author leiyang
 */
public class UserUtils {
    
    public static Boolean isUserInRole(Permission r) {
        HttpSession session = getSession();
        if (session != null) {
            StaffAccount sa = (StaffAccount) session.getAttribute("StaffAccount");
            Role role = sa.getRole();
            if (r.equals(Permission.SUPERUSER)) {
                return role.getSuperUserRight();
            } else if (r.equals(Permission.ANALYTICS)) {
                return role.getAnalyticsAccessRight();
            } else if (r.equals(Permission.BILL)) {
                return role.getBillAccessRight();
            } else if (r.equals(Permission.CARD)) {
                return role.getCardAccessRight();
            } else if (r.equals(Permission.CUSTOMER)) {
                return role.getCustomerAccessRight();
            } else if (r.equals(Permission.DEPOSIT)) {
                return role.getDepositAccessRight();
            } else if (r.equals(Permission.LOAN)) {
                return role.getLoanAccessRight();
            } else if (r.equals(Permission.PORTFOLIO)) {
                return role.getPortfolioAccessRight();
            } else if (r.equals(Permission.WEALTH)) {
                return role.getWealthAccessRight();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // Return true if this user has any of the following roles
    public static Boolean isUserInRoles(List<Permission> roles) {
        HttpSession session = getSession();
        if (session != null) {
            Boolean permitted = false;
            StaffAccount sa = (StaffAccount) session.getAttribute("StaffAccount");
            Role role = sa.getRole();

            for (Permission r : roles) {
                if (r.equals(Permission.SUPERUSER)) {
                    if (role.getSuperUserRight()) permitted = true;
                } else if (r.equals(Permission.ANALYTICS)) {
                    if (role.getAnalyticsAccessRight()) permitted = true;
                } else if (r.equals(Permission.BILL)) {
                    if (role.getBillAccessRight()) permitted = true;
                } else if (r.equals(Permission.CARD)) {
                    if (role.getCardAccessRight()) permitted = true;
                } else if (r.equals(Permission.CUSTOMER)) {
                    if (role.getCustomerAccessRight()) permitted = true;
                } else if (r.equals(Permission.DEPOSIT)) {
                    if (role.getDepositAccessRight()) permitted = true;
                } else if (r.equals(Permission.LOAN)) {
                    if (role.getLoanAccessRight()) permitted = true;
                } else if (r.equals(Permission.PORTFOLIO)) {
                    if (role.getPortfolioAccessRight()) permitted = true;
                } else if (r.equals(Permission.WEALTH)) {
                    if (role.getWealthAccessRight()) permitted = true;
                } else {
                    return false;
                }
            }
            return permitted;
        } else {
            return false;
        }
    }
}
