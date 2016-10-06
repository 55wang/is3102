/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.InvestmentPlan;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author VIN-S
 */
@Stateless
public class InvestmentPlanSessionBean implements InvestmentPlanSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public InvestmentPlan createInvestmentPlan(InvestmentPlan ip){
        em.persist(ip);
        return ip;
    }
    
    public InvestmentPlan getInvestmentPlanById(Long id){
        return em.find(InvestmentPlan.class, id);
    }
    
    public InvestmentPlan updateInvestmentPlan(InvestmentPlan ip){
        em.merge(ip);
        return ip;
    }
}
