/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.dams;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "customerDepositAccountTransactionHistoryManagedBean")
@ViewScoped
public class CustomerDepositAccountTransactionHistoryManagedBean implements Serializable {

    @EJB 
    private CustomerDepositSessionBeanLocal depositAccountBean;
    
    private String accountId;
    private DepositAccount account;
    
    /**
     * Creates a new instance of
     * CustomerDepositAccountTransactionHistoryManagedBean
     */
    public CustomerDepositAccountTransactionHistoryManagedBean() {
    }
    
    public void init() {
        System.out.println("Account id is: " + getAccountId());
        setAccount(depositAccountBean.getAccountFromId(getAccountId()));
        System.out.println("Account retrieved is: " + account.getAccountNumber());
    }

    /**
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the account
     */
    public DepositAccount getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(DepositAccount account) {
        this.account = account;
    }
    
}
