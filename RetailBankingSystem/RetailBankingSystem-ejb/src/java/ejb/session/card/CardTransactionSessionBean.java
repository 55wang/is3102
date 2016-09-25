/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CardTransactionSessionBean implements CardTransactionSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Boolean createCardTransaction(CardTransaction ct){
        try{
            em.merge(ct);
            em.flush();
            return true;
        }
        catch(Exception ex){
            System.out.println("CardTransactionSessionBean.createCardTransaction: "+ex.toString());
            return false;
        }
    }
}
