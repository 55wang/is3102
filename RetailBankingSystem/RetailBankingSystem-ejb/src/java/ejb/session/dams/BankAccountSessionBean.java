/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.BankAccount;
import entity.Transaction;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public BankAccount getAccountFromId(Long accountNumber) {
        return em.find(BankAccount.class, accountNumber);
    }

    @Override
    public void addAccount(BankAccount account) {
        em.persist(account);
    }

    @Override
    public List<BankAccount> showAllAccounts() {
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
        BankAccount ba = em.find(BankAccount.class, accountNumber);
        if (ba == null) {
            return "Account Not Found";
        } else {
            Transaction t = new Transaction();
            t.setAction(Transaction.ActionType.DEPOSIT.toString());
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
        BankAccount ba = em.find(BankAccount.class, accountNumber);
        if (ba == null) {
            return "Account Not Found";
        } else {
            int res = ba.getBalance().compareTo(withdrawAmount);
            if (res == -1) {
                return "Withdraw Failed! Balance not enough!";
            } else {
                Transaction t = new Transaction();
                t.setAction(Transaction.ActionType.DEPOSIT.toString());
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
