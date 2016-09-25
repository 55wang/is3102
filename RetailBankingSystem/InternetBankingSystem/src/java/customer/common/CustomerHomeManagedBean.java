/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import interceptor.audit.Audit;
import ejb.session.common.ChangePasswordSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.HashPwdUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerHomeManagedBean")
@ViewScoped
public class CustomerHomeManagedBean implements Serializable {

    @EJB
    private ChangePasswordSessionBeanLocal changePasswordSessionBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    private Customer customer;
    private String newPwd;
    private Date currentDate = new Date();

    /**
     * Creates a new instance of CustomerHomeManagedBean
     */
    public CustomerHomeManagedBean() {
    }

    @Audit(activtyLog = "home managed")
    public Boolean changePwd() {
        try {
            AuditLog a = new AuditLog();
            a.setActivityLog("Log off at: " + new Date());
            a.setFunctionName("CustomerLogoutManagedBean logoutCustomer()");
            a.setMainAccount((MainAccount) utilsBean.find(MainAccount.class, Long.parseLong(SessionUtils.getUserId())));
            utilsBean.persist(a);
            changePasswordSessionBean.changePwd(HashPwdUtils.hashPwd(newPwd), customer.getMainAccount());
            String msg = "Successful! You have reset your password. ";
            MessageUtils.displayInfo(msg);
            return true;
        } catch (Exception ex) {
            String msg = "Something went wrong.";
            System.out.print(ex);
            MessageUtils.displayError(msg);
            return false;
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    @PostConstruct
    public void setCustomer() {
        this.customer = loginSessionBean.getCustomerByUserID(SessionUtils.getUserName());
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

}
