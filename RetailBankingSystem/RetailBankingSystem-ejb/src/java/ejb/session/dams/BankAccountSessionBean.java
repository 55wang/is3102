/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.BankAccount;
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
    public void createAccount(BankAccount account) {
        em.persist(account);
    }
    // TODO: All types of accounts
    public List<BankAccount> showAllAccounts() {
        Query q = em.createQuery("SELECT a FROM DepositAccount a");
        return q.getResultList();
    }
}
