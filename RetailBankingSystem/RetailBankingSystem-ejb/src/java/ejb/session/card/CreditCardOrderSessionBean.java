/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardAccount;
import entity.card.order.CreditCardOrder;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.ApplicationStatus;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CreditCardOrderSessionBean implements CreditCardOrderSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    //get all creditcardorders that is not cancelled.
    @Override
    public List<CreditCardOrder> getListCreditCardOrders() {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco");
        return q.getResultList();
    }

    @Override
    public List<CreditCardOrder> getListCreditCardOrdersByNotCancelStatus() {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco WHERE cco.applicationStatus != :cancelStatus");

        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);

        try {
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("CreditCardOrderSessionBean.getAllCreditCardOrders: " + ex.toString());
            return null;
        }
    }

    //get 
    @Override
    public List<CreditCardOrder> getListCreditCardOrdersByMainIdAndNotCancelStatus(Long mainAccountId) {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco WHERE cco.applicationStatus != :cancelStatus "
                + "AND cco.mainAccount.id = :id");

        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);
        q.setParameter("id", mainAccountId);

        try {
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("CreditCardOrderSessionBean.getMainAccountAllCreditCardOrders: " + ex.toString());
            return null;
        }
    }

    @Override
    public CreditCardOrder getCreditCardOrderById(Long ccoId) {
        Query q = em.createQuery("SELECT a FROM CreditCardOrder a WHERE "
                + "a.id = :id AND "
                + "a.applicationStatus != :cancelStatus");

        q.setParameter("id", ccoId);
        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);

        return (CreditCardOrder) q.getSingleResult();
    }

    @Override
    public CreditCardOrder getCreditCardOrderByIdMainId(Long ccoId, Long mainId) {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco WHERE "
                + "cco.id = :ccoId AND cco.mainAccount.id = :mainId"
                + " AND cco.applicationStatus != :cancelStatus");

        q.setParameter("ccoId", ccoId);
        q.setParameter("mainId", mainId);
        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);

        return (CreditCardOrder) q.getSingleResult();
    }

    @Override
    public CreditCardOrder updateCreditCardOrderStatus(CreditCardOrder cco, ApplicationStatus status) {
        cco.setApplicationStatus(status);
        em.merge(cco);
        return cco;
    }

    @Override
    public List<CreditCardAccount> getListCreditCardOrdersByPendingStatus() {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus = :status");
        q.setParameter("status", EnumUtils.CardAccountStatus.PENDING);
        return q.getResultList();
    }

    @Override
    public List<CreditCardOrder> getListCreditCardOrdersByApplicationStatus(EnumUtils.ApplicationStatus applicationStatus) {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco WHERE cco.applicationStatus = :applicationStatus");
        q.setParameter("applicationStatus", applicationStatus);
        return q.getResultList();
    }

    //try not to use void, always return something or null. and catch it at the caller side.
    @Override
    public CreditCardOrder createCardOrder(CreditCardOrder order) {
        em.persist(order);
        return order;
    }

    @Override
    public CreditCardOrder updateCreditCardOrder(CreditCardOrder cco) {
        em.merge(cco);
        return cco;
    }
}
