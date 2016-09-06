/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entity.StaffAccount;
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
    
    public static String getStaffUsername() {
        HttpSession session = getSession();
        StaffAccount sa = (StaffAccount)session.getAttribute("StaffAccount");
        return sa.getUsername();
    }
    
    public static String getContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }
}