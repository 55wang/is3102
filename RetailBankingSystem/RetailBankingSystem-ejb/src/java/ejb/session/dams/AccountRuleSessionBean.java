/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.rules.DepositRule;
import entity.dams.rules.Interest;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class AccountRuleSessionBean implements AccountRuleSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Boolean addInterest(Interest interest) {
        try {
            em.persist(interest);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }
    
    @Override
    public Boolean updateInterest(Interest interest) {
        try {
            em.merge(interest);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }
    
    @Override
    public Boolean addDepositRule(DepositRule depositRule) {
        try {
            em.persist(depositRule);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }
    
    @Override
    public Boolean updateDepositRule(DepositRule depositRule) {
        try {
            em.merge(depositRule);
            return true;
        } catch (EntityExistsException e) {
            return false;
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
    
    @Override
    public DepositRule getDepositRuleByAccountName(String accountName) {
        
        Query q = em.createQuery("SELECT dr FROM DepositRule dr WHERE "
                + "dr.defaultCustomizedAccountName = :accountName AND "
                + "dr.isHistory = false "
                + "ORDER BY dr.createDate"
        );
        q.setParameter("accountName", accountName);
        
        DepositRule dr = null;
          
        try {
            List<DepositRule> rules = q.getResultList();
            if (rules != null && !rules.isEmpty() && rules.size() == 1) {
                return rules.get(0);
            } else {
                return null;
            }
        } catch (NoResultException ex) {
            return null;
        }
    }
}
