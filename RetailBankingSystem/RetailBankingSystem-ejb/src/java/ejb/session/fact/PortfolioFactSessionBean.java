/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import entity.fact.customer.FinancialInstrumentFactTable;
import entity.fact.customer.SinglePortfolioFactTable;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Stateless
public class PortfolioFactSessionBean implements PortfolioFactSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Double getFinancialInstrumentValueChangeByCreationDateAndName(Date date, EnumUtils.FinancialInstrumentClass name) {
        Query q = em.createQuery("SELECT a.instrumentValueChange from FinancialInstrumentFactTable a WHERE a.fi.name =:inName AND a.creationDate=:inDate");
        q.setParameter("inName", name);
        q.setParameter("inDate", date);
        return (Double) q.getSingleResult();
    }
    
    @Override
    public List<Date> getDistinctDateFromFIFactTable() {
        Query q = em.createNativeQuery("Select Distinct creationDate from FinancialInstrumentFactTable order by creationDate desc;");
        return q.getResultList();
    }
    
    @Override
    public FinancialInstrumentFactTable createFinancialInstrumentFactTable(FinancialInstrumentFactTable fif) {
        em.persist(fif);
        return fif;
    }

    @Override
    public SinglePortfolioFactTable createSinglePortfolioFactTable(SinglePortfolioFactTable spf) {
        em.persist(spf);
        return spf;
    }

    @Override
    public SinglePortfolioFactTable getSinglePortfolioFactTable(Date date, Long custId, Long portId) {
        Query q = em.createQuery("SELECT p FROM SinglePortfolioFactTable p where p.creationDate=:inDate AND p.customer.id =:inCustomer AND p.portfolio.id =:inPort");
        q.setParameter("inDate", date, TemporalType.DATE);
        q.setParameter("inCustomer", custId);
        q.setParameter("inPort", portId);
        return (SinglePortfolioFactTable) q.getSingleResult();
    }

    @Override
    public SinglePortfolioFactTable updateSinglePortfolioFactTable(SinglePortfolioFactTable spf) {
        em.merge(spf);
        return spf;
    }

    @Override
    public List<SinglePortfolioFactTable> getListPortfoliosFtByCustomerIdPortfolioId(Long custId, Long portId) {
        Query q = em.createQuery("SELECT p FROM SinglePortfolioFactTable p where p.portfolio.id =:inPortId AND p.customer.id =:inCustId ORDER BY p.creationDate asc");
        q.setParameter("inCustId", custId);
        q.setParameter("inPortId", portId);
        return q.getResultList();
    }

    @Override
    public List<SinglePortfolioFactTable> getListPortfoliosFtByCustomerId(Long custId) {
        Query q = em.createQuery("SELECT p FROM SinglePortfolioFactTable p where p.customer.id =:inCustId");
        q.setParameter("inCustId", custId);
        return q.getResultList();
    }

    @Override
    public SinglePortfolioFactTable getLatestPortfolioFtByCustomerIdPortfolioId(Long custId, Long portId) {
        //to be continued
        return null;
    }
    
    @Override
    public List<FinancialInstrumentFactTable> getListFinancialInstrumentFactTableByETFName(String etf){
        Query q = em.createQuery("SELECT p FROM FinancialInstrumentFactTable p where p.fi.ETFName =:etf ORDER BY p.creationDate asc");
        q.setParameter("etf", etf);
        return q.getResultList();
    }
}
