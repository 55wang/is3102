/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.rules.Interest;
import entity.dams.rules.TimeRangeInterest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;

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
    
    @Override
    public List<TimeRangeInterest> getFixedDepositAccountInterestsByAccount(CustomerFixedDepositAccount account) {
        Date startDate = account.getCreationDate();
        Date now = new Date();
        Integer monthDiff = DateUtils.monthDifference(startDate, now);
        Query q = em.createQuery("SELECT i FROM TimeRangeInterest i WHERE "
                + "i.isHistory = false AND i.version = :version AND "
                + "i.startMonth <= :monthDiff AND i.endMonth >= :monthDiff"
        );
        q.setParameter("version", getCurrentVersion());
        q.setParameter("monthDiff", monthDiff);
        
        return q.getResultList();
    }
    
    @Override
    public TimeRangeInterest getTimeRangeInterestByAmountAndMonth(Double amount, Integer month) {
        Query q = em.createQuery("SELECT i FROM TimeRangeInterest i WHERE "
                + "i.isHistory = false AND i.version = :version AND "
                + "i.startMonth <= :monthDiff AND i.endMonth >= :monthDiff AND "
                + "i.minimum <= :amount AND i.maximum >= :amount"
        );
        q.setParameter("version", getCurrentVersion());
        q.setParameter("monthDiff", month);
        q.setParameter("amount", new BigDecimal(amount));
        
        List<TimeRangeInterest> result = q.getResultList();
                
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
    
    private Integer getCurrentVersion() {
        Query q = em.createQuery("SELECT MAX(i.version) FROM TimeRangeInterest i");
        return (Integer) q.getSingleResult();
    }
}
