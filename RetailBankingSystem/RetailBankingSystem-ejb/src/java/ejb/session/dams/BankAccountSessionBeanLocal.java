/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.BankAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface BankAccountSessionBeanLocal {
    public long showNumberOfAccounts();
    public BankAccount getAccountFromId(Long accountNumber);
    public void addAccount(BankAccount account);
    public List<BankAccount> showAllAccounts();
    public String depositIntoAccount(Long accountNumber, BigDecimal depositAmount);
    public String withdrawFromAccount(Long accountNumber, BigDecimal depositAmount);
}
