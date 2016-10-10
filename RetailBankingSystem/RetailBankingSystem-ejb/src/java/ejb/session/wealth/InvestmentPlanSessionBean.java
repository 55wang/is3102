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
import server.utilities.EnumUtils.FinancialInstrumentClass;
import entity.wealth.FinancialInstrumentAndWeight;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.InvestmentRiskLevel;

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
        List<FinancialInstrumentAndWeight> suggestedFinancialInstruments = new ArrayList<FinancialInstrumentAndWeight>();
        
        FinancialInstrumentAndWeight fiaw1 = new FinancialInstrumentAndWeight();
        fiaw1.setFi(allFinancialInstruments.get(0));
        fiaw1.setWeight(0.12);
        suggestedFinancialInstruments.add(fiaw1);
        
        FinancialInstrumentAndWeight fiaw2 = new FinancialInstrumentAndWeight();
        fiaw2.setFi(allFinancialInstruments.get(2));
        fiaw2.setWeight(0.32);
        suggestedFinancialInstruments.add(fiaw2);
        
        FinancialInstrumentAndWeight fiaw3 = new FinancialInstrumentAndWeight();
        fiaw3.setFi(allFinancialInstruments.get(3));
        fiaw3.setWeight(0.06);
        suggestedFinancialInstruments.add(fiaw3);
        
        FinancialInstrumentAndWeight fiaw4 = new FinancialInstrumentAndWeight();
        fiaw4.setFi(allFinancialInstruments.get(6));
        fiaw4.setWeight(0.14);
        suggestedFinancialInstruments.add(fiaw4);
        
        FinancialInstrumentAndWeight fiaw5 = new FinancialInstrumentAndWeight();
        fiaw5.setFi(allFinancialInstruments.get(8));
        fiaw5.setWeight(0.22);
        suggestedFinancialInstruments.add(fiaw5);
        
        FinancialInstrumentAndWeight fiaw6 = new FinancialInstrumentAndWeight();
        fiaw6.setFi(allFinancialInstruments.get(10));
        fiaw6.setWeight(0.14);
        suggestedFinancialInstruments.add(fiaw6);
        
        FinancialInstrumentAndWeight fiaw7 = new FinancialInstrumentAndWeight();
        fiaw7.setFi(allFinancialInstruments.get(1));
        fiaw7.setWeight(0.0);
        suggestedFinancialInstruments.add(fiaw7);
        
        FinancialInstrumentAndWeight fiaw8 = new FinancialInstrumentAndWeight();
        fiaw8.setFi(allFinancialInstruments.get(4));
        fiaw8.setWeight(0.0);
        suggestedFinancialInstruments.add(fiaw8);
        
        FinancialInstrumentAndWeight fiaw9 = new FinancialInstrumentAndWeight();
        fiaw9.setFi(allFinancialInstruments.get(5));
        fiaw9.setWeight(0.0);
        suggestedFinancialInstruments.add(fiaw9);
        
        FinancialInstrumentAndWeight fiaw10 = new FinancialInstrumentAndWeight();
        fiaw10.setFi(allFinancialInstruments.get(7));
        fiaw10.setWeight(0.0);
        suggestedFinancialInstruments.add(fiaw10);
        
        FinancialInstrumentAndWeight fiaw11 = new FinancialInstrumentAndWeight();
        fiaw11.setFi(allFinancialInstruments.get(9));
        fiaw11.setWeight(0.0);
        suggestedFinancialInstruments.add(fiaw11);
        
        Double predictReturn = 0.11;
        ip.setSystemPredictReturn(predictReturn);
        ip.setSystemPredictRisk(30.0);
        ip.setRiskLevel(InvestmentRiskLevel.HIGH_RISK);

        ip.setSuggestedFinancialInstruments(suggestedFinancialInstruments);
        
        em.merge(ip);
        return ip;
    }
}
