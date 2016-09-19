/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.common;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import utils.EnumUtils;
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
    
    @ManagedProperty(value="#{param.email}")
    private String email;
    private Boolean valid;

    /**
     * Creates a new instance of StaffActivationFManagedBean
     */
    public StaffActivationManagedBean() {}
    
    @PostConstruct
    public void init() {
        email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("email");
        String randomPwd = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
        StaffAccount sa = staffBean.loginAccount(email, randomPwd);
        if(sa != null ){
            valid = true;
            sa.setStatus(EnumUtils.StatusType.ACTIVE);
            staffBean.updateAccount(sa);
            SessionUtils.setStaffAccount(sa);
        }
        else
            valid = false;
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
    
}
