/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    @Override
    public List<CardTransaction> retrieveTransactionByDate(Date a, Date b) {
        
        java.sql.Date startDate = new java.sql.Date(a.getTime());
        java.sql.Date endDate = new java.sql.Date(b.getTime());
        
        System.out.println("SELECT ct FROM CardTransaction ct WHERE ct.updateDate BETWEEN "+startDate+" AND "+endDate+"");
        Query q = em.createQuery("SELECT ct FROM CardTransaction ct WHERE "
                + "ct.updateDate BETWEEN :startDate AND :endDate"
        );
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        
        return q.getResultList();
    }
}
