/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import util.exception.dams.DepositAccountNotFoundException;
import util.exception.dams.DuplicateDepositAccountException;
import util.exception.dams.UpdateDepositAccountException;

/**
 *
 * @author leiyang
 */
@Remote
public interface CustomerDepositSessionBeanRemote {
    
    public DepositAccount getAccountFromId(String accountNumber) throws DepositAccountNotFoundException;
    public DepositAccount createAccount(DepositAccount account)  throws DuplicateDepositAccountException ;
    public DepositAccount updateAccount(DepositAccount account) throws UpdateDepositAccountException ;
    public List<DepositAccount> showAllAccounts();
    public List<DepositAccount> showAllActiveAccounts();
    public List<DepositAccount> getAllCustomerAccounts(String mainAccountId);
    public CustomerDepositAccount getDaytoDayAccountByMainAccount(MainAccount ma);
    public List<CustomerDepositAccount> getAllNonFixedCustomerAccounts(String mainAccountId);
    public List<CustomerFixedDepositAccount> getAllFixedCustomerAccounts(String mainAccountId);
    public String depositIntoAccount(String accountNumber, BigDecimal depositAmount);
    public String withdrawFromAccount(String accountNumber, BigDecimal depositAmount);
    public DepositAccount transferFromAccount(DepositAccount account, BigDecimal amount);
    public DepositAccount transferToAccount(DepositAccount account, BigDecimal amount);
    public DepositAccount depositIntoAccount(DepositAccount account, BigDecimal depositAmount);
    public DepositAccount demoDepositIntoAccount(DepositAccount account, BigDecimal depositAmount);
    public DepositAccount withdrawFromAccount(DepositAccount account, BigDecimal withdrawAmount);
    public DepositAccount creditSalaryIntoAccount(DepositAccount account, BigDecimal depositAmount);
    public DepositAccount payBillFromAccount(DepositAccount account, BigDecimal payAmount);
    public String payCCBillFromAccount(String accountNumber, String ccNumber, BigDecimal amount);
    public DepositAccount ccSpendingFromAccount(DepositAccount account, BigDecimal spendAmount);
    public DepositAccount investFromAccount(DepositAccount account, BigDecimal investAmount);
    public DepositAccount creditInterestAccount(DepositAccount account);
    public List<TransactionRecord> transactionRecordFromAccountNumber(String accountNumber);
    public TransactionRecord latestTransactionFromAccountNumber(String accountNumber);
    public CustomerDepositAccount updateCustomerDepositAccount(CustomerDepositAccount account);
    
}
