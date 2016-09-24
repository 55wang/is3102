/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.CustomerDepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "applyDebitCardAccountManagedBean")
@ViewScoped
public class ApplyDebitCardAccountManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    private List<CustomerDepositAccount> depositAccounts = new ArrayList<>();
    private List<String> accountOptions = new ArrayList<>();
    private String selectedAccountNumber;
    /**
     * Creates a new instance of ApplyDebitCardAccountManagedBean
     */
    public ApplyDebitCardAccountManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        depositAccounts = depositBean.getAllNonFixedCustomerAccounts(Long.parseLong(SessionUtils.getUserId()));
        for (CustomerDepositAccount a : depositAccounts) {
            accountOptions.add(a.getAccountNumber());
        }
    }
    
    public void applyDebitCard() {
        CustomerDepositAccount selectedAccount = getSelectedAccount();
        if (selectedAccount != null) {
            // TODO: create debit card and link with account
        }
    }
    
    public CustomerDepositAccount getSelectedAccount() {
        for (CustomerDepositAccount a : depositAccounts) {
            if (a.getAccountNumber().equals(selectedAccountNumber)) {
                return a;
            }
        } 
        return null;
    }

    /**
     * @return the accountOptions
     */
    public List<String> getAccountOptions() {
        return accountOptions;
    }

    /**
     * @param accountOptions the accountOptions to set
     */
    public void setAccountOptions(List<String> accountOptions) {
        this.accountOptions = accountOptions;
    }

    /**
     * @return the selectedAccountNumber
     */
    public String getSelectedAccountNumber() {
        return selectedAccountNumber;
    }

    /**
     * @param selectedAccountNumber the selectedAccountNumber to set
     */
    public void setSelectedAccountNumber(String selectedAccountNumber) {
        this.selectedAccountNumber = selectedAccountNumber;
    }
    
}
