/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.rules.Interest;
import entity.dams.rules.TimeRangeInterest;
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
    public List<TimeRangeInterest> addAllTimeRangeInterest(List<TimeRangeInterest> interest) {
        Integer newVersion = getCurrentVersion() + 1;
        for (TimeRangeInterest i : interest) {
            i.setVersion(newVersion);
            em.persist(i);
        }
        return interest;
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
    public List<Interest> showAllPresentInterests() {
        Query q = em.createQuery("SELECT i FROM Interest i WHERE i.isHistory = false");
        return q.getResultList();
    }
    
    @Override
    public List<TimeRangeInterest> getFixedDepositAccountDefaultInterests() {
        Query q = em.createQuery("SELECT i FROM TimeRangeInterest i WHERE "
                + "i.isHistory = false AND i.version = :version"
        );
        q.setParameter("version", getCurrentVersion());
        return q.getResultList();
    }
    
    private Integer getCurrentVersion() {
        Query q = em.createQuery("SELECT MAX(i.version) FROM TimeRangeInterest i");
        return (Integer) q.getSingleResult();
    }
}
