/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.rules.Interest;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
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
    public Interest addInterest(Interest interest) {
        try {
            em.persist(interest);
            return interest;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public Interest updateInterest(Interest interest) {
        try {
            em.merge(interest);
            return interest;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public List<Interest> showAllInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i");
        return q.getResultList();
    }
    
    @Override
    public List<Interest> getCustomAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE "
                + "i.defaultCustomAccount = true AND "
                + "i.isHistory = false"
        );
        return q.getResultList();
    }
    
    @Override
    public List<Interest> getCurrentAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE "
                + "i.defaultCurrentAccount = true AND "
                + "i.isHistory = false"
        );
        return q.getResultList();
    }
    @Override
    public List<Interest> getFixedDepositAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE "
                + "i.defaultFixedDepositAccount = true AND "
                + "i.isHistory = false"
        );
        return q.getResultList();
    }
    @Override
    public List<Interest> getSavingccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE "
                + "i.defaultSavingAccount = true AND "
                + "i.isHistory = false"
        );
        return q.getResultList();
    }
    @Override
    public List<Interest> getLoanAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE "
                + "i.defaultLoanAccount = true AND "
                + "i.isHistory = false"
        );
        return q.getResultList();
    }
    @Override
    public List<Interest> getMobileAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE "
                + "i.defaultMobileAccount = true AND "
                + "i.isHistory = false"
        );
        return q.getResultList();
    }
    
    @Override// For Custom Account
    public List<Interest> getDefaultInterestsByAccountName(String accountName) {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE "
                + "i.defaultCustomizedAccountName = :accountName AND "
                + "i.isHistory = false"
        );
        q.setParameter("accountName", accountName);
        return q.getResultList();
    }
}
