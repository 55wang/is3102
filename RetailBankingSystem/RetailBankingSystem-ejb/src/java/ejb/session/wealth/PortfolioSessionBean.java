/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.InvestmentPlan;
import entity.wealth.Portfolio;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.InvestmentPlanStatus;

/**
 *
 * @author wang
 */
@Stateless
public class PortfolioSessionBean implements PortfolioSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Portfolio> getListPortfolios() {
        Query q = em.createQuery("SELECT p FROM Portfolio p");
        return q.getResultList();
    }

    @Override
    public Portfolio getPortfolioById(Long Id) {
        return em.find(Portfolio.class, Id);
    }

    @Override
    public Portfolio createPortfolio(Portfolio p) {
        em.persist(p);
        return p;
    }

    @Override
    public Portfolio updatePortfolio(Portfolio p) {
        em.merge(p);
        return p;
    } 

}
