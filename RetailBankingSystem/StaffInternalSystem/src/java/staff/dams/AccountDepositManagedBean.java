/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.BankAccountSessionBeanLocal;
import entity.BankAccount;
import entity.CurrentAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.LoggingUtil;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "accountDepositManagedBean")
@ViewScoped
public class AccountDepositManagedBean implements Serializable {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBean;
    
    private String accountType;
    private String ACCOUNT_TYPE_CURRENT = BankAccount.AccountType.CURRENT.toString();
    private String ACCOUNT_TYPE_FIXED = BankAccount.AccountType.FIXED.toString();
    private String ACCOUNT_TYPE_SAVING = BankAccount.AccountType.SAVING.toString();
    private String ACCOUNT_TYPE_LOAN = BankAccount.AccountType.LOAN.toString();
    private List<CurrentAccount> accounts;
    private Long accountNumber;
    private BigDecimal depositAmount;
    
    public AccountDepositManagedBean() {}
    
    public void depositIntoAccount(ActionEvent event) {
        String message = bankAccountSessionBean.depositIntoAccount(getAccountNumber(), getDepositAmount());
        MessageUtils.displayInfo(message);
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
     * @return the depositAmount
     */
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    /**
     * @param depositAmount the depositAmount to set
     */
    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the ACCOUNT_TYPE_CURRENT
     */
    public String getACCOUNT_TYPE_CURRENT() {
        return ACCOUNT_TYPE_CURRENT;
    }

    /**
     * @param ACCOUNT_TYPE_CURRENT the ACCOUNT_TYPE_CURRENT to set
     */
    public void setACCOUNT_TYPE_CURRENT(String ACCOUNT_TYPE_CURRENT) {
        this.ACCOUNT_TYPE_CURRENT = ACCOUNT_TYPE_CURRENT;
    }

    /**
     * @return the ACCOUNT_TYPE_FIXED
     */
    public String getACCOUNT_TYPE_FIXED() {
        return ACCOUNT_TYPE_FIXED;
    }

    /**
     * @param ACCOUNT_TYPE_FIXED the ACCOUNT_TYPE_FIXED to set
     */
    public void setACCOUNT_TYPE_FIXED(String ACCOUNT_TYPE_FIXED) {
        this.ACCOUNT_TYPE_FIXED = ACCOUNT_TYPE_FIXED;
    }

    /**
     * @return the ACCOUNT_TYPE_SAVING
     */
    public String getACCOUNT_TYPE_SAVING() {
        return ACCOUNT_TYPE_SAVING;
    }

    /**
     * @param ACCOUNT_TYPE_SAVING the ACCOUNT_TYPE_SAVING to set
     */
    public void setACCOUNT_TYPE_SAVING(String ACCOUNT_TYPE_SAVING) {
        this.ACCOUNT_TYPE_SAVING = ACCOUNT_TYPE_SAVING;
    }

    /**
     * @return the ACCOUNT_TYPE_LOAN
     */
    public String getACCOUNT_TYPE_LOAN() {
        return ACCOUNT_TYPE_LOAN;
    }

    /**
     * @param ACCOUNT_TYPE_LOAN the ACCOUNT_TYPE_LOAN to set
     */
    public void setACCOUNT_TYPE_LOAN(String ACCOUNT_TYPE_LOAN) {
        this.ACCOUNT_TYPE_LOAN = ACCOUNT_TYPE_LOAN;
    }

}
