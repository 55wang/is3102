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
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import server.utilities.ConstantUtils;
import server.utilities.HashPwdUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffResetPwdManagedBean")
@ViewScoped
public class StaffResetPwdManagedBean implements Serializable{
    
    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private String newPwd;
    private StaffAccount sa;
    /**
     * Creates a new instance of StaffResetPwdManagedBean
     */
    public StaffResetPwdManagedBean() {}
    
    @PostConstruct
    public void init() {
        this.sa = staffBean.getAccountByUsername(SessionUtils.getStaffUsername());
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter resetpassword.xhtml");
        a.setFunctionName("StaffResetPwdManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all customer information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
    public Boolean changePwd(){
        try{
            sa.setPassword(HashPwdUtils.hashPwd(newPwd));
            staffBean.updateAccount(sa);
            String msg = ConstantUtils.PASSWORD_CHANGE_SUCCESS;
            MessageUtils.displayInfo(msg);
            return true;
        }
        catch(Exception ex){
            String msg = ConstantUtils.SOMETHING_WENT_WRONG;
            MessageUtils.displayError(msg);
            return false;
        }
    }

    /**
     * @return the newPwd
     */
    public String getNewPwd() {
        return newPwd;
    }

    /**
     * @param newPwd the newPwd to set
     */
    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    /**
     * @return the sa
     */
    public StaffAccount getSa() {
        return sa;
    }

    /**
     * @param sa the sa to set
     */
    public void setSa(StaffAccount sa) {
        this.sa = sa;
    }
    
}
