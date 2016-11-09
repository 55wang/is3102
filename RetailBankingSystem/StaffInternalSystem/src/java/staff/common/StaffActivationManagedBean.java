/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.common;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import server.utilities.EnumUtils;
import util.exception.common.UpdateStaffAccountException;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffActivationManagedBean")
@RequestScoped
public class StaffActivationManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    @ManagedProperty(value = "#{param.email}")
    private String email;
    private Boolean valid;
    private StaffAccount staff;

    /**
     * Creates a new instance of StaffActivationFManagedBean
     */
    public StaffActivationManagedBean() {
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("Login from activation");
            email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("email");
            String randomPwd = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
            StaffAccount sa = staffBean.loginAccount(staffBean.getAccountByEmail(email).getUsername(), randomPwd);
            if (sa != null) {
                staff = sa;
                valid = true;
                System.out.println("Status updated");
                sa.setStatus(EnumUtils.StatusType.ACTIVE);
                staffBean.updateAccount(sa);
                SessionUtils.setStaffAccount(sa);
            } else {
                valid = false;
            }
            AuditLog a = new AuditLog();
            a.setActivityLog("System user enter create_customer_information.xhtml");
            a.setFunctionName("StaffActivitionCaseManagedBean @PostConstruct init()");
            a.setFunctionInput("Getting all activition");
            a.setStaffAccount(SessionUtils.getStaff());
            utilsBean.persist(a);
        } catch (UpdateStaffAccountException e) {
            System.out.println("UpdateStaffAccountException StaffActivationManagedBean init()");
        }

    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the valid
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public StaffAccount getStaff() {
        return staff;
    }

    public void setStaff(StaffAccount staff) {
        this.staff = staff;
    }
}
