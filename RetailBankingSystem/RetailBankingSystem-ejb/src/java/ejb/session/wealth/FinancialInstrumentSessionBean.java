/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.FinancialInstrument;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestmentPlan;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.FinancialInstrumentClass;

/**
 *
 * @author VIN-S
 */
@Stateless
public class FinancialInstrumentSessionBean implements FinancialInstrumentSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public FinancialInstrument createFinancialInstrument(FinancialInstrument fi) {
        em.persist(fi);
        return fi;
    }

    @Override
    public FinancialInstrument getFinancialInstrumentByName(FinancialInstrumentClass name) {
        return em.find(FinancialInstrument.class, name);
    }

    @Override
    public FinancialInstrument updateFinancialInstrument(FinancialInstrument fi) {
        em.merge(fi);
        return fi;
    }

    @Override
    public List<FinancialInstrument> getAllFinancialInstruments() {
        Query q = em.createQuery("SELECT a from FinancialInstrument a");
        return q.getResultList();
    }

    
    @Override
    public FinancialInstrumentAndWeight updateFinancialInstrumentAndWeight(FinancialInstrumentAndWeight fiw) {
        em.merge(fiw);
        return fiw;
    }    
}
