/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.common.TransactionRecord;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.DepositAccountType;
import util.exception.dams.DuplicateDepositAccountException;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "openAccountManagedBean")
@ViewScoped
public class OpenAccountManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal customerDepositSessionBean;
    @EJB
    private InterestSessionBeanLocal interestSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private String accountType;
    private CustomerDepositAccount newCurrentAccount;
    private CustomerFixedDepositAccount newFixedDepositAccount;
    private CustomerDepositAccount newSavingAccount;
    //LoanAccount and MobileAccount will be opened by other means
    private List<CustomerDepositAccount> currentAccounts = new ArrayList<>();
    private List<CustomerFixedDepositAccount> fixedDepositAccounts = new ArrayList<>();
    private List<CustomerDepositAccount> savingAccounts = new ArrayList<>();
    private String ACCOUNT_TYPE_CURRENT = EnumUtils.DepositAccountType.CURRENT.toString();
    private String ACCOUNT_TYPE_FIXED = EnumUtils.DepositAccountType.FIXED.toString();
    private String ACCOUNT_TYPE_SAVING = EnumUtils.DepositAccountType.SAVING.toString();

    public OpenAccountManagedBean() {
        System.out.println("OpenAccountManagedBean() Created!!");
    }

    @PostConstruct
    public void init() {
        newCurrentAccount = new CustomerDepositAccount();
        newCurrentAccount.setType(DepositAccountType.CURRENT);
        newFixedDepositAccount = new CustomerFixedDepositAccount();
        newFixedDepositAccount.setType(DepositAccountType.FIXED);
        newSavingAccount = new CustomerDepositAccount();
        newSavingAccount.setType(DepositAccountType.SAVING);
        accountType = ACCOUNT_TYPE_CURRENT;
        List<DepositAccount> accounts = customerDepositSessionBean.showAllAccounts();
        for (DepositAccount ba : accounts) {
            if (ba instanceof CustomerDepositAccount) {
                if (ba.getType().equals(DepositAccountType.CURRENT)) {
                    getCurrentAccounts().add((CustomerDepositAccount) ba);
                } else if (ba.getType().equals(DepositAccountType.SAVING)) {
                    getSavingAccounts().add((CustomerDepositAccount) ba);
                }
            } else {
                getFixedDepositAccounts().add((CustomerFixedDepositAccount) ba);
            }

        }
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter OpenAccountManagedBean");
        a.setFunctionName("OpenAccountManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all OpenAccountManagedBean information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void addAccount(ActionEvent event) {
        try {

            addTransaction();
            addDefaultInterest();
            if (getAccountType().equals(getACCOUNT_TYPE_CURRENT())) {
                if (customerDepositSessionBean.createAccount(getNewCurrentAccount()) != null) {
                    getCurrentAccounts().add(getNewCurrentAccount());
                    MessageUtils.displayInfo("Current Account Created");
                } else {
                    MessageUtils.displayError("There's some error when creating account");
                }
            } else if (getAccountType().equals(getACCOUNT_TYPE_FIXED())) {
                if (customerDepositSessionBean.createAccount(getNewFixedDepositAccount()) != null) {
                    getFixedDepositAccounts().add(getNewFixedDepositAccount());
                    MessageUtils.displayInfo("Fixed Deposit Account Created");
                } else {
                    MessageUtils.displayError("There's some error when creating account");
                }
            } else if (getAccountType().equals(getACCOUNT_TYPE_SAVING())) {
                if (customerDepositSessionBean.createAccount(getNewSavingAccount()) != null) {
                    getSavingAccounts().add(getNewSavingAccount());
                    MessageUtils.displayInfo("Savings Account Created");
                } else {
                    MessageUtils.displayError("There's some error when creating account");
                }
            } else {
                MessageUtils.displayError("There's some error when creating account");
            }

        } catch (DuplicateDepositAccountException e) {
            MessageUtils.displayError("There's some error when creating account");
        }
    }

    private DepositAccount getAccount() {
        if (accountType.equals(getACCOUNT_TYPE_CURRENT())) {
            return getNewCurrentAccount();
        } else if (accountType.equals(getACCOUNT_TYPE_FIXED())) {
            return getNewFixedDepositAccount();
        } else if (accountType.equals(getACCOUNT_TYPE_SAVING())) {
            return getNewSavingAccount();
        } else {
            return null;
        }
    }

    private void addTransaction() {
        TransactionRecord t = new TransactionRecord();
        t.setAmount(getAccount().getBalance());
        t.setFromAccount(getAccount());
        t.setCredit(Boolean.TRUE);
        t.setActionType(EnumUtils.TransactionType.DEPOSIT);
        if (accountType.equals(getACCOUNT_TYPE_CURRENT())) {
            getNewCurrentAccount().addTransaction(t);
        } else if (accountType.equals(getACCOUNT_TYPE_FIXED())) {
            getNewFixedDepositAccount().addTransaction(t);
        } else if (accountType.equals(getACCOUNT_TYPE_SAVING())) {
            getNewSavingAccount().addTransaction(t);
        } else {
        }
    }

    private void addDefaultInterest() {
        if (accountType.equals(getACCOUNT_TYPE_FIXED())) {
            // Only fixed deposit account will save interest, save a new Interest object, with id account id + interest name
//            getNewFixedDepositAccount().addInterestsRules(interestSessionBean.getFixedDepositAccountDefaultInterests());
        } else {

        }
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
     * @return the newCurrentAccount
     */
    public CustomerDepositAccount getNewCurrentAccount() {
        return newCurrentAccount;
    }

    /**
     * @param newCurrentAccount the newCurrentAccount to set
     */
    public void setNewCurrentAccount(CustomerDepositAccount newCurrentAccount) {
        this.newCurrentAccount = newCurrentAccount;
    }

    /**
     * @return the newFixedDepositAccount
     */
    public CustomerFixedDepositAccount getNewFixedDepositAccount() {
        return newFixedDepositAccount;
    }

    /**
     * @param newFixedDepositAccount the newFixedDepositAccount to set
     */
    public void setNewFixedDepositAccount(CustomerFixedDepositAccount newFixedDepositAccount) {
        this.newFixedDepositAccount = newFixedDepositAccount;
    }

    /**
     * @return the newSavingAccount
     */
    public CustomerDepositAccount getNewSavingAccount() {
        return newSavingAccount;
    }

    /**
     * @param newSavingAccount the newSavingAccount to set
     */
    public void setNewSavingAccount(CustomerDepositAccount newSavingAccount) {
        this.newSavingAccount = newSavingAccount;
    }

    /**
     * @return the currentAccounts
     */
    public List<CustomerDepositAccount> getCurrentAccounts() {
        return currentAccounts;
    }

    /**
     * @param currentAccounts the currentAccounts to set
     */
    public void setCurrentAccounts(List<CustomerDepositAccount> currentAccounts) {
        this.currentAccounts = currentAccounts;
    }

    /**
     * @return the fixedDepositAccounts
     */
    public List<CustomerFixedDepositAccount> getFixedDepositAccounts() {
        return fixedDepositAccounts;
    }

    /**
     * @param fixedDepositAccounts the fixedDepositAccounts to set
     */
    public void setFixedDepositAccounts(List<CustomerFixedDepositAccount> fixedDepositAccounts) {
        this.fixedDepositAccounts = fixedDepositAccounts;
    }

    /**
     * @return the savingAccounts
     */
    public List<CustomerDepositAccount> getSavingAccounts() {
        return savingAccounts;
    }

    /**
     * @param savingAccounts the savingAccounts to set
     */
    public void setSavingAccounts(List<CustomerDepositAccount> savingAccounts) {
        this.savingAccounts = savingAccounts;
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

}
