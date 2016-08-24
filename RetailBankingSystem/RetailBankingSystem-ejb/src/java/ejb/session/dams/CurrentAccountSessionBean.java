/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.CurrentAccount;
import entity.Interest;
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
public class CurrentAccountSessionBean implements CurrentAccountSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createAccount(CurrentAccount account) {
        account.addInterestRules(new Interest());
        em.persist(account);
    }
    
    public CurrentAccount getAccountFromId(Long id) {
        return em.find(CurrentAccount.class, id);
    }
    
    public long showNumberOfAccounts() {
        Query q = em.createQuery("SELECT COUNT(a) FROM CurrentAccount a");
        return ((Long)q.getSingleResult()).longValue();
    }
    
    // TODO: For internal testing only, not for demo
    public List<CurrentAccount> showAllAccounts() {
        Query q = em.createQuery("SELECT a FROM CurrentAccount a");
        return q.getResultList();
    }
    
    public String depositIntoAccount(Long accountNumber, BigDecimal depositAmount) {
        if (accountNumber == null) {
            return "Account Number Cannot be Empty!";
        }
        if (depositAmount == null) {
            return "Your Deposit Amount is Empty!";
        }
        CurrentAccount ca = em.find(CurrentAccount.class, accountNumber);
        if (ca == null) {
            return "Account Not Found";
        } else {
            ca.addBalance(depositAmount);
            em.persist(ca);
            return "Deposit Success!";
        }
    }
}
