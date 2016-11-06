/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.counter.TellerCounterSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.counter.TellerCounter;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "accountWithdrawCounterManagedBean")
@ViewScoped
public class AccountWithdrawCounterManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal bankAccountSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private TellerCounterSessionBeanLocal counterBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    
    private String accountNumber;
    private BigDecimal withdrawAmount;
    private MainAccount mainAccount;
    private String customerIC;
    private List<DepositAccount> depositAccounts;
    
    /**
     * Creates a new instance of WithdrawCurrentAccountManagedBean
     */
    public AccountWithdrawCounterManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter AccountWithdrawManagedBean");
        a.setFunctionName("AccountWithdrawManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all AccountWithdrawManagedBean information");
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
    
    public void withdrawFromAccount() {
        
        if (!updateCounter(false, getWithdrawAmount())) {
            return;
        }
        
        String message = bankAccountSessionBean.withdrawFromAccount(getAccountNumber(), getWithdrawAmount());
        
        if (message.equals("SUCCESS")) {
            
            MessageUtils.displayInfo("Withdraw success!");
        } else {
            MessageUtils.displayError(message);
        }
        
    }
    
    public Boolean updateCounter(Boolean isAdd, BigDecimal amount) {
        TellerCounter tc = SessionUtils.getTellerCounter();
        tc = counterBean.getTellerCounterById(tc.getId());
        if (isAdd) {
            tc.addCash(amount);
        } else {
            if (tc.hasEnoughCash(amount)) {
                tc.removeCash(amount);
            } else {
                MessageUtils.displayError("Not Enough Cash in Counter!");
                return false;
            }
        }
        
        tc = counterBean.updateTellerCounter(tc);
        SessionUtils.setTellerCounter(tc);

        return true;
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
     * @return the withdrawAmount
     */
    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    /**
     * @param withdrawAmount the withdrawAmount to set
     */
    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
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
