/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.MileCardProduct;
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
public class NewCardProductSessionBean implements NewCardProductSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    public List<MileCardProduct> showAllMileProducts() {
        Query q = em.createQuery("SELECT * FROM CreditCardProduct cc inner join MileCardProduct mile on cc.id=mile.id");
        return q.getResultList();
    }

    @Override
    public MileCardProduct getMileProductFromId(Long orderNumber) {
        return em.find(MileCardProduct.class, orderNumber);
    }

    @Override
    public void createMileProduct(MileCardProduct mcp) {
        em.persist(mcp);
    }
    
    //repeat for the rest of card

}
