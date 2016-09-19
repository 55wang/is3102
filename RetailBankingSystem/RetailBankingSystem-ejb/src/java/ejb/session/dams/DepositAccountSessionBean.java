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
public class DepositAccountSessionBean implements DepositAccountSessionBeanLocal {

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
    public DepositAccount createAccount(DepositAccount account) {
         try {
            Transaction t = new Transaction();
            t.setAmount(account.getBalance());
            t.setCredit(Boolean.TRUE);
            t.setActionType(EnumUtils.TransactionType.INITIAL);
            account.addTransaction(t);
            account.setPreviousBalance(account.getBalance());
            em.persist(account);
            t.setFromAccount(account);
            em.merge(t);
            return account;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public DepositAccount updateAccount(DepositAccount account) {
         try {
            em.merge(account);
            return account;
        } catch (EntityExistsException e) {
            return null;
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
            em.merge(ba);
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
                t.setActionType(EnumUtils.TransactionType.WITHDRAW);
                t.setAmount(withdrawAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(ba);
                ba.addTransaction(t);
                ba.removeBalance(withdrawAmount);
                em.merge(ba);
                return "Withdraw Success!";
            }
        }
    }
    
    @Override
    public DepositAccount depositIntoAccount(DepositAccount account, BigDecimal depositAmount){
        if (account == null || depositAmount == null) {
            return null;
        } else {
            Transaction t = new Transaction();
            t.setActionType(EnumUtils.TransactionType.DEPOSIT);
            t.setAmount(depositAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(account);
            account.addTransaction(t);
            account.addBalance(depositAmount);
            em.merge(account);
            return account;
        }
    }
    
    @Override
    public DepositAccount withdrawFromAccount(DepositAccount account, BigDecimal withdrawAmount) {
        if (account == null || withdrawAmount == null) {
            return null;
        } else {
            int res = account.getBalance().compareTo(withdrawAmount);
            if (res == -1) {
                return null;
            } else {
                Transaction t = new Transaction();
                t.setActionType(EnumUtils.TransactionType.WITHDRAW);
                t.setAmount(withdrawAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                account.addTransaction(t);
                account.removeBalance(withdrawAmount);
                em.merge(account);
                return account;
            }
        }
    }
    
    @Override
    public DepositAccount creditSalaryIntoAccount(DepositAccount account, BigDecimal depositAmount) {
        if (account == null || depositAmount == null) {
            return null;
        } else {
            Transaction t = new Transaction();
            t.setActionType(EnumUtils.TransactionType.SALARY);
            t.setAmount(depositAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(account);
            account.addTransaction(t);
            account.addBalance(depositAmount);
            em.merge(account);
            return account;
        }
    }
    
    @Override
    public DepositAccount payBillFromAccount(DepositAccount account, BigDecimal payAmount) {
        if (account == null || payAmount == null) {
            return null;
        } else {
            int res = account.getBalance().compareTo(payAmount);
            if (res == -1) {
                return null;
            } else {
                Transaction t = new Transaction();
                t.setActionType(EnumUtils.TransactionType.BILL);
                t.setAmount(payAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                account.addTransaction(t);
                account.removeBalance(payAmount);
                em.merge(account);
                return account;
            }
        }
    }
    
    @Override
    public DepositAccount ccSpendingFromAccount(DepositAccount account, BigDecimal spendAmount) {
        if (account == null || spendAmount == null) {
            return null;
        } else {
            int res = account.getBalance().compareTo(spendAmount);
            if (res == -1) {
                return null;
            } else {
                Transaction t = new Transaction();
                t.setActionType(EnumUtils.TransactionType.CCSPENDING);
                t.setAmount(spendAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                account.addTransaction(t);
                account.removeBalance(spendAmount);
                em.merge(account);
                return account;
            }
        }
    }
    
    @Override
    public DepositAccount investFromAccount(DepositAccount account, BigDecimal investAmount) {
        if (account == null || investAmount == null) {
            return null;
        } else {
            int res = account.getBalance().compareTo(investAmount);
            if (res == -1) {
                return null;
            } else {
                Transaction t = new Transaction();
                t.setActionType(EnumUtils.TransactionType.INVEST);
                t.setAmount(investAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                account.addTransaction(t);
                account.removeBalance(investAmount);
                em.merge(account);
                return account;
            }
        }
    }
}
