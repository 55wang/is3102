/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.CurrentAccount;
import entity.Interest;
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
        Query q = em.createQuery("SELECT a FROM CurrentAccount a WHERE a.id = :id");
        q.setParameter("id", id);
        if (q.getResultList().isEmpty()) {
            // no account found
            return null;
        } else {
            return (CurrentAccount) q.getResultList().remove(0);
        }
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
}
