/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.CustomerActivationSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.MainAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerActivationManagedBean")
@RequestScoped
public class CustomerActivationManagedBean implements Serializable {
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private CustomerActivationSessionBeanLocal customerActivationSessionBean;
    
    @ManagedProperty(value="#{param.email}")
    private String email;
    private Boolean valid;
    
    private MainAccount mainAccount;
   
    @PostConstruct
    public void init() {
        email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("email");
        String randomPwd = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
        mainAccount = customerActivationSessionBean.getMainAccountByEmail(email);
        if(mainAccount.getStatus().equals(MainAccount.StatusType.PENDING) && randomPwd.equals(mainAccount.getPassword())){
            valid = true; // And auto-login if valid?
            try{
                customerActivationSessionBean.updateAccountStatus(mainAccount);
                loginSessionBean.loginAccount(mainAccount.getUserID(), mainAccount.getPassword());
                SessionUtils.setUserId(mainAccount.getId());
                SessionUtils.setUserName(mainAccount.getUserID());
            }
            catch(Exception ex){
                
            }
        }
        else
            valid = false;
    }

    /**
     * Creates a new instance of CustomerActivationManagedBean
     */
    public CustomerActivationManagedBean() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }
}
