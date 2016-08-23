/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.BankAccountSessionBeanLocal;
import entity.DepositAccount;
import entity.BankAccount;
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
@Named(value = "accountManagedBean")
@ViewScoped
public class DepositAccountManagedBean implements Serializable {
    @EJB
    private BankAccountSessionBeanLocal accountSessionBean;

    private List<BankAccount> accounts;
    private DepositAccount newAccount;
    /**
     * Creates a new instance of AccountManagedBean
     */
    public DepositAccountManagedBean() {
        newAccount = new DepositAccount();
    }
    
    @PostConstruct
    public void init() {
        accounts = accountSessionBean.showAllAccounts();
    }
    
    public void createAccount(ActionEvent event) {
        accountSessionBean.createAccount(newAccount);
        accounts.add(newAccount);
        newAccount = new DepositAccount();
        // current context
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Account Created!", ""));
    }

    /**
     * @return the accounts
     */
    public List<BankAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the newAccount
     */
    public BankAccount getNewAccount() {
        return newAccount;
    }

    /**
     * @param newAccount the newAccount to set
     */
    public void setNewAccount(DepositAccount newAccount) {
        this.newAccount = newAccount;
    }
    
}
