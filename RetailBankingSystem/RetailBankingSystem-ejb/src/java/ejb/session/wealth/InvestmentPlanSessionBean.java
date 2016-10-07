/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.staff.StaffAccount;
import entity.wealth.FinancialInstrument;
import entity.wealth.InvestmentPlan;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.FinancialInstrumentClass;

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
    public InvestmentPlan createInvestmentPlan(InvestmentPlan ip){
        em.persist(ip);
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
    public InvestmentPlan generateSuggestedInvestmentPlan(InvestmentPlan ip){
        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();
        
        //Mathmatical Caculation
        
            //Input is wealthmanagementsubscriber's risk tolerance score, allFinancialInstruments' deviation, return and correlation
            //output is arraylist suggestedFinancialInstruments and according percentage
        
        //End of Mathmatical Caculation
        
        List<FinancialInstrument> suggestedFinancialInstruments = new ArrayList<FinancialInstrument>();
        suggestedFinancialInstruments.add(allFinancialInstruments.get(0));
        suggestedFinancialInstruments.add(allFinancialInstruments.get(2));
        suggestedFinancialInstruments.add(allFinancialInstruments.get(3));
        suggestedFinancialInstruments.add(allFinancialInstruments.get(6));
        suggestedFinancialInstruments.add(allFinancialInstruments.get(8));
        suggestedFinancialInstruments.add(allFinancialInstruments.get(10));
        
        List<Double> accordingPercentages = new ArrayList<Double>();
        accordingPercentages.add(0.12);
        accordingPercentages.add(0.32);
        accordingPercentages.add(0.06);
        accordingPercentages.add(0.14);
        accordingPercentages.add(0.22);
        accordingPercentages.add(0.14);
        
        ip.setFinancialInstruments(suggestedFinancialInstruments);
        ip.setFinancialInstrumentPecentage(accordingPercentages);
        
        em.merge(ip);
        return ip;
    }
}
