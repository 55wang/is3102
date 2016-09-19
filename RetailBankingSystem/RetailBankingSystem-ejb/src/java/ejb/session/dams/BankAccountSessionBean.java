/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.DepositAccount;
import entity.common.Transaction;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utils.EnumUtils;

/**
 *
 * @author leiyang
 */
@Stateless
public class BankAccountSessionBean implements BankAccountSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override 
    public long showNumberOfAccounts() {
        Query q = em.createQuery("SELECT COUNT(*) FROM BankAccount");
        return ((Long)q.getSingleResult()).longValue();
    }
    
    @Override 
    public DepositAccount getAccountFromId(Long accountNumber) {
        return em.find(DepositAccount.class, accountNumber);
    }

    @Override
    public Boolean createAccount(DepositAccount account) {
         try {
            Transaction t = new Transaction();
            t.setAmount(account.getBalance());
            t.setCredit(Boolean.TRUE);
            t.setActionType(EnumUtils.TransactionType.INITIAL);
            account.addTransaction(t);
            em.persist(account);
            t.setFromAccount(account);
            em.merge(t);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }
    
    @Override
    public Boolean updateAccount(DepositAccount account) {
         try {
            em.merge(account);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }

    @Override
    public List<DepositAccount> showAllAccounts() {
        Query q = em.createQuery("SELECT ba FROM BankAccount ba");
        return q.getResultList();
    }

    @Override
    public String depositIntoAccount(Long accountNumber, BigDecimal depositAmount) {
        if (accountNumber == null) {
            return "Account Number Cannot be Empty!";
        }
        if (depositAmount == null) {
            return "Your Deposit Amount is Empty!";
        }
        DepositAccount ba = em.find(DepositAccount.class, accountNumber);
        if (ba == null) {
            return "Account Not Found";
        } else {
            Transaction t = new Transaction();
            t.setActionType(EnumUtils.TransactionType.DEPOSIT);
            t.setAmount(depositAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(ba);
            ba.addTransaction(t);
            ba.addBalance(depositAmount);
            em.persist(ba);
            return "Deposit Success!";
        }
    }

    @Override
    public String withdrawFromAccount(Long accountNumber, BigDecimal withdrawAmount) {
        if (accountNumber == null) {
            return "Account Number Cannot be Empty!";
        }
        if (withdrawAmount == null) {
            return "Your Withdraw Amount is Empty!";
        }
        DepositAccount ba = em.find(DepositAccount.class, accountNumber);
        if (ba == null) {
            return "Account Not Found";
        } else {
            int res = ba.getBalance().compareTo(withdrawAmount);
            if (res == -1) {
                return "Withdraw Failed! Balance not enough!";
            } else {
                Transaction t = new Transaction();
                t.setActionType(EnumUtils.TransactionType.DEPOSIT);
                t.setAmount(withdrawAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(ba);
                ba.addTransaction(t);
                ba.removeBalance(withdrawAmount);
                em.persist(ba);
                return "Withdraw Success!";
            }
        }
    }
}
