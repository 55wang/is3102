/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "checkAccountBalanceManagedBean")
@ViewScoped
public class CheckAccountBalanceManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal bankAccountSessionBean;
    /**
     * Creates a new instance of CheckBalanceCurrentAccountManagedBean
     */
    public CheckAccountBalanceManagedBean() {
    }
    
    private Long accountNumber;
    private List<DepositAccount> depositAccounts = new ArrayList<>();
    
    public void checkBalance(ActionEvent event) {
        DepositAccount newAccount = bankAccountSessionBean.getAccountFromId(getAccountNumber());
        if (newAccount == null) {
            MessageUtils.displayError("Account Not Found!");
        } else {
            getDepositAccounts().add(newAccount);
            MessageUtils.displayInfo("Account Retrieved!");
        }
        
    }

    /**
     * @return the accountNumber
     */
    public Long getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the depositAccounts
     */
    public List<DepositAccount> getDepositAccounts() {
        return depositAccounts;
    }

    /**
     * @param depositAccounts the depositAccounts to set
     */
    public void setDepositAccounts(List<DepositAccount> depositAccounts) {
        this.depositAccounts = depositAccounts;
    }
}
