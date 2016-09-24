/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import interceptor.audit.Audit;
import ejb.session.common.ChangePasswordSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.customer.Customer;
import java.io.Serializable;
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
public class CustomerHomeManagedBean implements Serializable{
    @EJB
    private ChangePasswordSessionBeanLocal changePasswordSessionBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    private Customer customer;
    private String newPwd;
    
    
    /**
     * Creates a new instance of CustomerHomeManagedBean
     */
    public CustomerHomeManagedBean() {
    }
    
    @Audit(activtyLog = "home manged")
    public Boolean changePwd(){
        try{
            changePasswordSessionBean.changePwd(HashPwdUtils.hashPwd(newPwd), customer.getMainAccount());
            String msg = "Successful! You have reset your password. ";
            MessageUtils.displayInfo(msg);
            return true;
        }
        catch(Exception ex){
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
}
