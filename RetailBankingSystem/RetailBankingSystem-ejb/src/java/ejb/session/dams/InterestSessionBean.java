/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

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
public class InterestSessionBean implements InterestSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void addInterest(Interest interest) {
        em.persist(interest);
    }
    
    @Override
    public List<Interest> showAllInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i");
        return q.getResultList();
    }
    
    @Override
    public List<Interest> getCurrentAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE i.defaultCurrentAccount = true");
        return q.getResultList();
    }
    @Override
    public List<Interest> getFixedDepositAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE i.defaultFixedDepositAccount = true");
        return q.getResultList();
    }
    @Override
    public List<Interest> getSavingccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE i.defaultSavingAccount = true");
        return q.getResultList();
    }
    @Override
    public List<Interest> getLoanAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE i.defaultLoanAccount = true");
        return q.getResultList();
    }
    @Override
    public List<Interest> getMobileAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE i.defaultMobileAccount = true");
        return q.getResultList();
    }
}
