/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.CurrentAccountSessionBeanLocal;
import entity.CurrentAccount;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "openCurrentAccountManagedBean")
@ViewScoped
public class OpenCurrentAccountManagedBean implements Serializable {
    @EJB
    private CurrentAccountSessionBeanLocal currentAccountSessionBean;

    private List<CurrentAccount> accounts;
    private CurrentAccount newAccount;
    /**
     * Creates a new instance of AccountManagedBean
     */
    public OpenCurrentAccountManagedBean() {
        newAccount = new CurrentAccount();
    }
    
    @PostConstruct
    public void init() {
        accounts = currentAccountSessionBean.showAllAccounts();
    }
    
    public void createAccount(ActionEvent event) {
        currentAccountSessionBean.createAccount(newAccount);
        accounts.add(newAccount);
        newAccount = new CurrentAccount();
        // current context
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Account Created!", ""));
    }

    /**
     * @return the accounts
     */
    public List<CurrentAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<CurrentAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the newAccount
     */
    public CurrentAccount getNewAccount() {
        return newAccount;
    }

    /**
     * @param newAccount the newAccount to set
     */
    public void setNewAccount(CurrentAccount newAccount) {
        this.newAccount = newAccount;
    }
    
}
