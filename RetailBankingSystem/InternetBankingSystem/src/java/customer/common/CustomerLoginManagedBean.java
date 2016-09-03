/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.LoginSessionBeanLocal;
import entity.MainAccount;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.RedirectUtils;
import utils.SessionUtils;


/**
 *
 * @author VIN-S
 */
@Named(value = "customerLoginManagedBean")
@ViewScoped
public class CustomerLoginManagedBean implements Serializable{
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    
    private MainAccount loginAccount = new MainAccount();
    

    /**
     * Creates a new instance of CustomerLoginManagedBean
     */
    public CustomerLoginManagedBean() {
    }
    
    public MainAccount getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(MainAccount loginAccount) {
        this.loginAccount = loginAccount;
    }
    
    public void loginCustomer() {
        try {
            Long userID = loginSessionBean.loginAccount(loginAccount.getUserID(), loginAccount.getPassword()).getId();
            String userName = loginSessionBean.loginAccount(loginAccount.getUserID(), loginAccount.getPassword()).getUserID();
            SessionUtils.setUserId(userID);
            SessionUtils.setUserName(userName);
            RedirectUtils.redirect("customer_home.xhtml");
        } catch (NullPointerException e) {
            RedirectUtils.redirect("fail.xhtml");
        }

    }
    
}
