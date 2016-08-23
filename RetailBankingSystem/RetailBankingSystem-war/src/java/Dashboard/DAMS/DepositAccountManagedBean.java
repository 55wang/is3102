/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dashboard.DAMS;

import ejb.session.dams.BankAccountSessionBeanLocal;
import entity.BankAccount;
import entity.DepositAccount;
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
    private BankAccountSessionBeanLocal bankAccountSessionBean;

    private List<BankAccount> accounts;
    private BankAccount newAccount;
    /**
     * Creates a new instance of AccountManagedBean
     */
    public DepositAccountManagedBean() {
        // admin need to select the type of accounts here
        newAccount = new DepositAccount();
    }
    
    @PostConstruct
    public void init() {
        accounts = bankAccountSessionBean.showAllAccounts();
    }
    
    public void createAccount(ActionEvent event) {
        bankAccountSessionBean.createAccount(newAccount);
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
    public void setNewAccount(BankAccount newAccount) {
        this.newAccount = newAccount;
    }
    
}
