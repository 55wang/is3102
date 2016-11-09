/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.CustomerActivationSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import server.utilities.EnumUtils;
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;
import util.exception.dams.UpdateDepositAccountException;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerActivationManagedBean")
@RequestScoped
public class CustomerActivationManagedBean implements Serializable {
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private CustomerActivationSessionBeanLocal customerActivationSessionBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositAccountBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    @ManagedProperty(value = "#{param.email}")
    private String email;
    private Boolean valid;

    private MainAccount mainAccount;

    @PostConstruct
    public void init() {
        email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("email");
        String randomPwd = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
        System.out.println(email);
        System.out.println(randomPwd);
        try{
            mainAccount = mainAccountSessionBean.getMainAccountByEmail(email);
        }catch(MainAccountNotExistException ex){
            System.out.println("CustomerActivationManagedBean.init.customerActivationSessionBean.getMainAccountByEmail:"+ex.toString());
        }
        if (mainAccount.getStatus().equals(EnumUtils.StatusType.PENDING) && randomPwd.equals(mainAccount.getPassword())) {
            valid = true; // And auto-login if valid?
            try {
                mainAccount.setStatus(EnumUtils.StatusType.ACTIVE);
                customerActivationSessionBean.updateMainAccount(mainAccount);
                SessionUtils.setUserId(mainAccount.getId());
                SessionUtils.setUserName(mainAccount.getUserID());
                SessionUtils.setTokenAuthentication(false);
                AuditLog a = new AuditLog();
                a.setActivityLog("Account Activated at: " + new Date());
                a.setFunctionName("activateCustomer()");
                a.setMainAccount(mainAccount);
                utilsBean.persist(a);
                for (DepositAccount da : mainAccount.getBankAcounts()) {
                    da.setStatus(EnumUtils.StatusType.ACTIVE);
                    depositAccountBean.updateAccount(da);
                }
                // TODO: Activate all the deposit account
            } catch (UpdateMainAccountException ex1) {
                System.out.println("UpdateMainAccountException");
            }catch(UpdateDepositAccountException ex2){
                System.out.println("UpdateDepositAccountException");
            }
        } else {
            valid = false;
        }
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
