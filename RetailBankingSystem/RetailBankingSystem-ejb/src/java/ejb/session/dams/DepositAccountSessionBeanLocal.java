/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface DepositAccountSessionBeanLocal {
    public long showNumberOfAccounts();
    public DepositAccount getAccountFromId(Long accountNumber);
    public DepositAccount createAccount(DepositAccount account);
    public DepositAccount updateAccount(DepositAccount account);
    public List<DepositAccount> showAllAccounts();
    public String depositIntoAccount(Long accountNumber, BigDecimal depositAmount);
    public String withdrawFromAccount(Long accountNumber, BigDecimal depositAmount);
    public DepositAccount depositIntoAccount(DepositAccount account, BigDecimal depositAmount);
    public DepositAccount withdrawFromAccount(DepositAccount account, BigDecimal withdrawAmount);
    public DepositAccount creditSalaryIntoAccount(DepositAccount account, BigDecimal depositAmount);
    public DepositAccount payBillFromAccount(DepositAccount account, BigDecimal payAmount);
    public DepositAccount ccSpendingFromAccount(DepositAccount account, BigDecimal spendAmount);
    public DepositAccount investFromAccount(DepositAccount account, BigDecimal investAmount);
}
