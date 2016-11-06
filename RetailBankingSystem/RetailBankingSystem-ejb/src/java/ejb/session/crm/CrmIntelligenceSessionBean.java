/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Stateless
public class CrmIntelligenceSessionBean implements CrmIntelligenceSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long getCustomerChurnCustomer(Date startDate, Date endDate) {
        return 0L;
    }

    @Override
    public Double getCustomerAvgDepositSavingAmount(Date startDate, Date endDate) {
        return 0.0;
    }

    @Override
    public Double getCustomerAvgLoanAmount(Date startDate, Date endDate) {
        return 0.0;
    }

    @Override
    public Long getTotalCustomerAgeGroup(Integer startAge, Integer endAge) {
        Query q = em.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.age BETWEEN :inStartAge AND :inEndAge");
        q.setParameter("inStartAge", startAge);
        q.setParameter("inEndAge", endAge);
        return (Long) q.getSingleResult();
    }

    @Override
    public Long getNewCustomerAgeGroup(Integer startAge, Integer endAge) {
        
        return 0L;
    }

}
