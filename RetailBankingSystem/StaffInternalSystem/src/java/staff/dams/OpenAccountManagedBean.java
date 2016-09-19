/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.BankAccountSessionBeanLocal;
import ejb.session.dams.AccountRuleSessionBeanLocal;
import entity.dams.account.DepositAccount;
import entity.dams.account.CurrentAccount;
import entity.dams.account.FixedDepositAccount;
import entity.dams.account.SavingAccount;
import entity.common.Transaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.EnumUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "openAccountManagedBean")
@ViewScoped
public class OpenAccountManagedBean implements Serializable {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBean;
    @EJB
    private AccountRuleSessionBeanLocal interestSessionBean;

    private String accountType;
    private CurrentAccount newCurrentAccount = new CurrentAccount();
    private FixedDepositAccount newFixedDepositAccount = new FixedDepositAccount();
    private SavingAccount newSavingAccount = new SavingAccount();
    //LoanAccount and MobileAccount will be opened by other means
    private List<CurrentAccount> currentAccounts = new ArrayList<>();
    private List<FixedDepositAccount> fixedDepositAccounts = new ArrayList<>();
    private List<SavingAccount> savingAccounts = new ArrayList<>();
    private String ACCOUNT_TYPE_CURRENT = EnumUtils.DepositAccountType.CURRENT.toString();
    private String ACCOUNT_TYPE_FIXED = EnumUtils.DepositAccountType.FIXED.toString();
    private String ACCOUNT_TYPE_SAVING = EnumUtils.DepositAccountType.SAVING.toString();
 
    public OpenAccountManagedBean() {
        System.out.println("OpenAccountManagedBean() Created!!");
    }
    
    @PostConstruct
    public void init() {
        accountType = ACCOUNT_TYPE_CURRENT;
        List<DepositAccount> accounts = bankAccountSessionBean.showAllAccounts();
        for (DepositAccount ba : accounts) {
            if (ba instanceof CurrentAccount) {
                getCurrentAccounts().add((CurrentAccount) ba);
            } else if (ba instanceof FixedDepositAccount) {
                getFixedDepositAccounts().add((FixedDepositAccount) ba);
            } else if (ba instanceof SavingAccount) {
                getSavingAccounts().add((SavingAccount) ba);
            }
        }
    }
    
    public void addAccount(ActionEvent event) {
        addTransaction();
        addDefaultInterest();
        if (getAccountType().equals(getACCOUNT_TYPE_CURRENT())) {
            if (bankAccountSessionBean.createAccount(getNewCurrentAccount())) {
                getCurrentAccounts().add(getNewCurrentAccount());
                MessageUtils.displayInfo("Current Account Created");
            } else {
                MessageUtils.displayError("There's some error when creating account");
            }
        } else if (getAccountType().equals(getACCOUNT_TYPE_FIXED())) {
            if (bankAccountSessionBean.createAccount(getNewFixedDepositAccount())) {
                getFixedDepositAccounts().add(getNewFixedDepositAccount());
                MessageUtils.displayInfo("Fixed Deposit Account Created");
            } else {
                MessageUtils.displayError("There's some error when creating account");
            }
        } else if (getAccountType().equals(getACCOUNT_TYPE_SAVING())) {
            if (bankAccountSessionBean.createAccount(getNewSavingAccount())) {
                getSavingAccounts().add(getNewSavingAccount());
                MessageUtils.displayInfo("Savings Account Created");
            } else {
                MessageUtils.displayError("There's some error when creating account");
            }
        } else {
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
        Transaction t = new Transaction();
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
            getNewFixedDepositAccount().addInterestsRules(interestSessionBean.getFixedDepositAccountDefaultInterests());
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
    public CurrentAccount getNewCurrentAccount() {
        return newCurrentAccount;
    }

    /**
     * @param newCurrentAccount the newCurrentAccount to set
     */
    public void setNewCurrentAccount(CurrentAccount newCurrentAccount) {
        this.newCurrentAccount = newCurrentAccount;
    }

    /**
     * @return the newFixedDepositAccount
     */
    public FixedDepositAccount getNewFixedDepositAccount() {
        return newFixedDepositAccount;
    }

    /**
     * @param newFixedDepositAccount the newFixedDepositAccount to set
     */
    public void setNewFixedDepositAccount(FixedDepositAccount newFixedDepositAccount) {
        this.newFixedDepositAccount = newFixedDepositAccount;
    }

    /**
     * @return the newSavingAccount
     */
    public SavingAccount getNewSavingAccount() {
        return newSavingAccount;
    }

    /**
     * @param newSavingAccount the newSavingAccount to set
     */
    public void setNewSavingAccount(SavingAccount newSavingAccount) {
        this.newSavingAccount = newSavingAccount;
    }

    /**
     * @return the currentAccounts
     */
    public List<CurrentAccount> getCurrentAccounts() {
        return currentAccounts;
    }

    /**
     * @param currentAccounts the currentAccounts to set
     */
    public void setCurrentAccounts(List<CurrentAccount> currentAccounts) {
        this.currentAccounts = currentAccounts;
    }

    /**
     * @return the fixedDepositAccounts
     */
    public List<FixedDepositAccount> getFixedDepositAccounts() {
        return fixedDepositAccounts;
    }

    /**
     * @param fixedDepositAccounts the fixedDepositAccounts to set
     */
    public void setFixedDepositAccounts(List<FixedDepositAccount> fixedDepositAccounts) {
        this.fixedDepositAccounts = fixedDepositAccounts;
    }

    /**
     * @return the savingAccounts
     */
    public List<SavingAccount> getSavingAccounts() {
        return savingAccounts;
    }

    /**
     * @param savingAccounts the savingAccounts to set
     */
    public void setSavingAccounts(List<SavingAccount> savingAccounts) {
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
