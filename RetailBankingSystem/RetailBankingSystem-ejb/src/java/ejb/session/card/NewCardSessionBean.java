/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardOrder;
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
public class NewCardSessionBean implements NewCardSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    public List<CreditCardOrder> showAllCreditCardOrder() {
        Query q = em.createQuery("SELECT cco FROM CreditCardOrder cco");
        return q.getResultList();
    }

    public CreditCardOrder getAccountFromId(Long orderNumber) {
        return em.find(CreditCardOrder.class, orderNumber);
    }

    public void addAccount(CreditCardOrder order) {
        em.persist(order);
    }
}
