/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entity.Role;
import entity.StaffAccount;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author leiyang
 */
public class SessionUtils {

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
    }

    public static String getUserName() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        return session.getAttribute("username").toString();
    }

    public static void setUserName(String userName) {
        HttpSession session = getSession();
        session.setAttribute("username", userName);
    }

    public static String getUserId() {
        HttpSession session = getSession();
        if (session != null) {
            return (String) session.getAttribute("userid");
        } else {
            return null;
        }
    }

    public static void setUserId(Long userID) {
        HttpSession session = getSession();
        session.setAttribute("userid", userID);
    }

    public static void setStaffAccount(StaffAccount sa) {
        HttpSession session = getSession();
        session.setAttribute("StaffAccount", sa);
    }
    
    public static String getContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public static boolean isUserInRole(Role.Permission r) {
        HttpSession session = getSession();
        if (session != null) {
            StaffAccount sa = (StaffAccount) session.getAttribute("StaffAccount");
            Role role = sa.getRole();
            if (r.equals(Role.Permission.SUPERUSER)) {
                return role.getSuperUserRight();
            } else if (r.equals(Role.Permission.ANALYTICS)) {
                return role.getAnalyticsAccessRight();
            } else if (r.equals(Role.Permission.BILL)) {
                return role.getBillAccessRight();
            } else if (r.equals(Role.Permission.CARD)) {
                return role.getCardAccessRight();
            } else if (r.equals(Role.Permission.CUSTOMER)) {
                return role.getCustomerAccessRight();
            } else if (r.equals(Role.Permission.DEPOSIT)) {
                return role.getDepositAccessRight();
            } else if (r.equals(Role.Permission.LOAN)) {
                return role.getLoanAccessRight();
            } else if (r.equals(Role.Permission.PORTFOLIO)) {
                return role.getPortfolioAccessRight();
            } else if (r.equals(Role.Permission.WEALTH)) {
                return role.getWealthAccessRight();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // Return true if this user has any of the following roles
    public static boolean isUserInRoles(List<Role.Permission> roles) {
        HttpSession session = getSession();
        if (session != null) {
            boolean permitted = false;
            StaffAccount sa = (StaffAccount) session.getAttribute("StaffAccount");
            Role role = sa.getRole();

            for (Role.Permission r : roles) {
                if (r.equals(Role.Permission.SUPERUSER)) {
                    if (role.getSuperUserRight()) permitted = true;
                } else if (r.equals(Role.Permission.ANALYTICS)) {
                    if (role.getAnalyticsAccessRight()) permitted = true;
                } else if (r.equals(Role.Permission.BILL)) {
                    if (role.getBillAccessRight()) permitted = true;
                } else if (r.equals(Role.Permission.CARD)) {
                    if (role.getCardAccessRight()) permitted = true;
                } else if (r.equals(Role.Permission.CUSTOMER)) {
                    if (role.getCustomerAccessRight()) permitted = true;
                } else if (r.equals(Role.Permission.DEPOSIT)) {
                    if (role.getDepositAccessRight()) permitted = true;
                } else if (r.equals(Role.Permission.LOAN)) {
                    if (role.getLoanAccessRight()) permitted = true;
                } else if (r.equals(Role.Permission.PORTFOLIO)) {
                    if (role.getPortfolioAccessRight()) permitted = true;
                } else if (r.equals(Role.Permission.WEALTH)) {
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
