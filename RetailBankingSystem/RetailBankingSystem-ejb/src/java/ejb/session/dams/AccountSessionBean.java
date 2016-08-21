/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.Account;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author VIN-S
 */
@Stateful
public class AccountSessionBean implements AccountSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createAccount(Account account) {
        em.persist(account);
    }
    
    public List<Account> showAllAccounts() {
        Query q = em.createQuery("SELECT a FROM Account a");
        return q.getResultList();
    }
}
