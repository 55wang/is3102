/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.dams.DepositAccountNotFoundException;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "checkAccountBalanceCounterManagedBean")
@ViewScoped
public class CheckAccountBalanceCounterManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal bankAccountSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    
    /**
     * Creates a new instance of CheckBalanceCurrentAccountManagedBean
     */
    public CheckAccountBalanceCounterManagedBean() {
    }
    
    private String accountNumber;
    private DepositAccount account;
    private MainAccount mainAccount;
    private String customerIC;
    private List<DepositAccount> depositAccounts;
    
    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("Counter user enter checkAccountBalanceCounterManagedBean");
        a.setFunctionName("checkAccountBalanceCounterManagedBean @PostConstruct init()");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
    public void retrieveMainAccount() {
        
        try {
            mainAccount = loginSessionBean.getMainAccountByUserIC(getCustomerIC());
            depositAccounts = mainAccount.getBankAcounts();
        } catch (Exception e) {
            mainAccount = null;
            MessageUtils.displayError("Customer Main Account Not Found!");
        }
        
    }
    
    public void checkBalance(ActionEvent event) {
        
        try {
            DepositAccount newAccount = bankAccountSessionBean.getAccountFromId(getAccountNumber());
            account = newAccount;
            MessageUtils.displayInfo("Account Retrieved!");
        } catch (DepositAccountNotFoundException e) {
            MessageUtils.displayError("Account Not Found!");
        }
        
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    /**
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    /**
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
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
