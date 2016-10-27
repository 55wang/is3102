/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import entity.fact.customer.SinglePortfolioFactTable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class FactSessionBean implements FactSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public SinglePortfolioFactTable createSinglePortfolioFactTable(SinglePortfolioFactTable spf) {
        em.persist(spf);
        return spf;
    }
    
    @Override
    public List<SinglePortfolioFactTable> getListPortfoliosFtByCustomerIdPortfolioId(Long custId, Long portId) {
        Query q = em.createQuery("SELECT p FROM portfoliofacttable p where p.portfolio.id =:inPortId AND p.customer.id =: inCustId");
        q.setParameter("inCustId", custId);
        q.setParameter("inPortId", portId);
        return q.getResultList();
    }

    @Override
    public SinglePortfolioFactTable getLatestPortfolioFtByCustomerIdPortfolioId(Long custId, Long portId) {
        //to be continued
        return null;
    }
}
