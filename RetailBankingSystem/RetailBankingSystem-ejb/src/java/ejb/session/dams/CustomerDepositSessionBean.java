/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.CustomerDepositAccount;
import entity.common.TransactionRecord;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author leiyang
 */
@Stateless
public class CustomerDepositSessionBean implements CustomerDepositSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override 
    public long showNumberOfAccounts() {
        Query q = em.createQuery("SELECT COUNT(*) FROM DepositAccount");
        return ((Long)q.getSingleResult()).longValue();
    }
    
    @Override 
    public DepositAccount getAccountFromId(String accountNumber) {
        return em.find(DepositAccount.class, accountNumber);
    }

    @Override
    public DepositAccount createAccount(DepositAccount account) {
         try {
            TransactionRecord t = new TransactionRecord();
            t.setAmount(account.getBalance());
            t.setCredit(Boolean.TRUE);
            t.setActionType(EnumUtils.TransactionType.INITIAL);
            account.addTransaction(t);
            if (account instanceof CustomerDepositAccount) {
                ((CustomerDepositAccount)account).setPreviousBalance(account.getBalance());
            }
            
            account.setAccountNumber(generateAccountNumber());
            em.persist(account);
            t.setFromAccount(account);
            em.merge(t);
            return account;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    private String generateAccountNumber() {
        String accountNumber = "";
        for(;;) {
             accountNumber = GenerateAccountAndCCNumber.generateAccountNumber();
             DepositAccount a = em.find(DepositAccount.class, accountNumber);
             if (a == null) {
                 return accountNumber;
             }
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
        Query q = em.createQuery("SELECT ba FROM DepositAccount ba");
        return q.getResultList();
    }
    
    @Override
    public List<DepositAccount> getAllCustomerAccounts(Long mainAccountId) {
        Query q = em.createQuery("SELECT ba FROM DepositAccount ba WHERE ba.mainAccount.id =:mainAccountId");
        q.setParameter("mainAccountId", mainAccountId);
        return q.getResultList();
    }

    @Override
    public String depositIntoAccount(String accountNumber, BigDecimal depositAmount) {
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
            TransactionRecord t = new TransactionRecord();
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
    public String withdrawFromAccount(String accountNumber, BigDecimal withdrawAmount) {
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
                TransactionRecord t = new TransactionRecord();
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
            TransactionRecord t = new TransactionRecord();
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
                TransactionRecord t = new TransactionRecord();
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
            TransactionRecord t = new TransactionRecord();
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
                TransactionRecord t = new TransactionRecord();
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
                TransactionRecord t = new TransactionRecord();
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
                TransactionRecord t = new TransactionRecord();
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
    
    @Override
    public DepositAccount creditInterestAccount(DepositAccount account) {
        BigDecimal interestAmount = account.getCumulatedInterest().getCummulativeAmount();
        if (account == null || interestAmount == null || interestAmount.compareTo(new BigDecimal(0.001)) >= 0) {
            return null;
        } else {
            TransactionRecord t = new TransactionRecord();
            t.setActionType(EnumUtils.TransactionType.INTEREST);
            t.setAmount(interestAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(account);
            account.addTransaction(t);
            account.addBalance(interestAmount);
            account.getCumulatedInterest().reset();
            em.merge(account);
            return account;
        }
    }
}
