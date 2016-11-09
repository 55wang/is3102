/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.transfer;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.counter.TellerCounterSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import util.exception.dams.DepositAccountNotFoundException;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "intraBankTransferCounterManagedBean")
@ViewScoped
public class IntraBankTransferCounterManagedBean implements Serializable {

    @EJB
    private TellerCounterSessionBeanLocal counterBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    private Boolean isOwn = false;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String customerIC;
    private BigDecimal amount;
    private MainAccount mainAccount;
    private List<DepositAccount> depositAccounts = new ArrayList<>();
    
    public IntraBankTransferCounterManagedBean() {
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
    
    public void transfer(ActionEvent event) {
        
        try {
            
            DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNumber);
        if (fromAccount != null && fromAccount.getBalance().compareTo(amount) < 0) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
            return;
        }

        String result = transferBean.transferFromAccountToAccount(fromAccountNumber, toAccountNumber, amount);
        if (result.equals("SUCCESS")) {
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        } else {
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
        
        } catch (DepositAccountNotFoundException e) {
            System.out.println("DepositAccountNotFoundException IntraBankTransferCounterManagedBean transfer()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        } 
        
    }

    /**
     * @return the fromAccountNumber
     */
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    /**
     * @param fromAccountNumber the fromAccountNumber to set
     */
    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    /**
     * @return the toAccountNumber
     */
    public String getToAccountNumber() {
        return toAccountNumber;
    }

    /**
     * @param toAccountNumber the toAccountNumber to set
     */
    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
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
     * @return the depositAmount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param depositAmount the depositAmount to set
     */
    public void setAmount(BigDecimal depositAmount) {
        this.amount = depositAmount;
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

    /**
     * @return the isOwn
     */
    public Boolean getIsOwn() {
        return isOwn;
    }

    /**
     * @param isOwn the isOwn to set
     */
    public void setIsOwn(Boolean isOwn) {
        this.isOwn = isOwn;
    }
    
}
