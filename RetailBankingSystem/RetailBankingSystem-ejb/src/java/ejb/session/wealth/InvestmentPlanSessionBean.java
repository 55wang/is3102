/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import entity.wealth.FinancialInstrumentAndWeight;
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

    @Override
    public InvestmentPlan createInvestmentPlan(InvestmentPlan ip) {
        em.merge(ip);
        return ip;
    }

    @Override
    public InvestmentPlan getInvestmentPlanById(Long id) {
        InvestmentPlanStatus cancelstatus = InvestmentPlanStatus.CANCELLED;
        Query q = em.createQuery("SELECT r FROM InvestmentPlan r WHERE r.id =:id AND r.status !=:cancel");
        q.setParameter("id", id);
        q.setParameter("cancel", cancelstatus);
        return (InvestmentPlan) q.getSingleResult();
    }

    @Override
    public InvestmentPlan updateInvestmentPlan(InvestmentPlan ip) {
        em.merge(ip);
        return ip;
    }

    @Override
    public List<InvestmentPlan> getListInvestmentPlansByRM(StaffAccount sa) {
        Query q = em.createQuery("SELECT r FROM InvestmentPlan r WHERE r.wealthManagementSubscriber.relationshipManager =:sa");
        q.setParameter("sa", sa);

        return q.getResultList();
    }

    @Override
    public List<InvestmentPlan> getListInvestmentPlansByMainAccount(MainAccount ma) {
        InvestmentPlanStatus cancelstatus = InvestmentPlanStatus.CANCELLED;
        Query q = em.createQuery("SELECT r FROM InvestmentPlan r WHERE r.wealthManagementSubscriber.mainAccount =:ma AND r.status !=:cancel");
        q.setParameter("ma", ma);
        q.setParameter("cancel", cancelstatus);

        return q.getResultList();
    }

    @Override
    public List<InvestmentPlan> getListInvestmentPlansByStatus(InvestmentPlanStatus status) {
        Query q = em.createQuery("Select i from InvestmentPlan i where i.status =:inStatus");
        q.setParameter("inStatus", status);
        return q.getResultList();
    }

}
