/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.setting;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import server.utilities.HashPwdUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffLoginResetPwdManagedBean")
@ViewScoped
public class StaffLoginResetPwdManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private String oldPwd;
    private String newPwd;
    private StaffAccount sa = SessionUtils.getStaff();
    
    /**
     * Creates a new instance of StaffLoginResetPwdManagedBean
     */
    public StaffLoginResetPwdManagedBean() {
    }

    
    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter StaffLoginResetPwdManagedBean");
        a.setFunctionName("StaffLoginResetPwdManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all StaffLoginResetPwdManagedBean");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    public Boolean changePwd(){
        if (!HashPwdUtils.hashPwd(oldPwd).equals(sa.getPassword())) {
            String msg = ConstantUtils.OLD_PASSWORD_NOTMTACH;
            MessageUtils.displayError(msg);
            return false;
        }
        
        try{
            sa.setPassword(HashPwdUtils.hashPwd(newPwd));
            StaffAccount result = staffBean.updateAccount(sa);
            if (result != null) {
                SessionUtils.setStaffAccount(result);
                String msg = ConstantUtils.PASSWORD_CHANGE_SUCCESS;
                MessageUtils.displayInfo(msg);
                return true;
            } else {
                return false;
            }
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

    /**
     * @return the oldPwd
     */
    public String getOldPwd() {
        return oldPwd;
    }

    /**
     * @param oldPwd the oldPwd to set
     */
    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }
    
}
