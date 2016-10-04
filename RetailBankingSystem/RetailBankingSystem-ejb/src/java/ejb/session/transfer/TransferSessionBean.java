/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.transfer;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leiyang
 */
@Stateless
public class TransferSessionBean implements TransferSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    @Override
    public String transferFromAccountToAccount(String fromAcc, String toAcc, BigDecimal amount) {
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAcc);
        DepositAccount toAccount = depositBean.getAccountFromId(toAcc);
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            // not enough money
            return "FAIL";
        } else {
            depositBean.withdrawFromAccount(fromAccount, amount);
            depositBean.depositIntoAccount(toAccount, amount);
            return "SUCCESS";
        }
    }
}
