/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import entity.wealth.PortfolioModel;
import entity.wealth.InvestmentPlan;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.InvestmentPlanStatus;


/**
 *
 * @author VIN-S
 */
@Stateless
public class InvestmentPlanSessionBean implements InvestmentPlanSessionBeanLocal {
    @EJB
    private FinancialInstrumentSessionBeanLocal financialInstrumentSessionBean;
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public List<PortfolioModel> getTestR() {
        PortfolioModel cp = new PortfolioModel();
        Query q = em.createQuery("SELECT a FROM ConstructPortfolio a");
        return q.getResultList();
    }
    
    @Override
    public InvestmentPlan createInvestmentPlan(InvestmentPlan ip){
        em.merge(ip);
        return ip;
    }
    
    @Override
    public InvestmentPlan getInvestmentPlanById(Long id){
        return em.find(InvestmentPlan.class, id);
    }
    
    @Override
    public InvestmentPlan updateInvestmentPlan(InvestmentPlan ip){
        em.merge(ip);
        return ip;
    }

    @Override
    public List<InvestmentPlan> getInvestmentPlanByRM(StaffAccount sa){
        Query q = em.createQuery("SELECT r FROM InvestmentPlan r WHERE r.wealthManagementSubscriber.relationshipManager =:sa");
        q.setParameter("sa", sa);
        
        return q.getResultList();
    }
    
    @Override
    public List<InvestmentPlan> getInvestmentPlanByMainAccount(MainAccount ma){
        InvestmentPlanStatus cancelstatus = InvestmentPlanStatus.CANCELLED;
        Query q = em.createQuery("SELECT r FROM InvestmentPlan r WHERE r.wealthManagementSubscriber.mainAccount =:ma AND r.status !=:cancel");
        q.setParameter("ma", ma);
        q.setParameter("cancel", cancelstatus);
        
        return q.getResultList();
    }
}
