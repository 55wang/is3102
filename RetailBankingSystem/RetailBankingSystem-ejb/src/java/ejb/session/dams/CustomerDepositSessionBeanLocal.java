/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface CustomerDepositSessionBeanLocal {
    public long showNumberOfAccounts();
    public DepositAccount getAccountFromId(String accountNumber);
    public DepositAccount createAccount(DepositAccount account);
    public DepositAccount updateAccount(DepositAccount account);
    public List<DepositAccount> showAllAccounts();
    public List<DepositAccount> getAllCustomerAccounts(Long mainAccountId);
    public CustomerDepositAccount getDaytoDayAccountByMainAccount(MainAccount ma);
    public List<CustomerDepositAccount> getAllNonFixedCustomerAccounts(Long mainAccountId);
    public String depositIntoAccount(String accountNumber, BigDecimal depositAmount);
    public String withdrawFromAccount(String accountNumber, BigDecimal depositAmount);
    public DepositAccount transferFromAccount(DepositAccount account, BigDecimal amount);
    public DepositAccount transferToAccount(DepositAccount account, BigDecimal amount);
    public DepositAccount depositIntoAccount(DepositAccount account, BigDecimal depositAmount);
    public DepositAccount withdrawFromAccount(DepositAccount account, BigDecimal withdrawAmount);
    public DepositAccount creditSalaryIntoAccount(DepositAccount account, BigDecimal depositAmount);
    public DepositAccount payBillFromAccount(DepositAccount account, BigDecimal payAmount);
    public DepositAccount ccSpendingFromAccount(DepositAccount account, BigDecimal spendAmount);
    public DepositAccount investFromAccount(DepositAccount account, BigDecimal investAmount);
    public DepositAccount creditInterestAccount(DepositAccount account);
}
