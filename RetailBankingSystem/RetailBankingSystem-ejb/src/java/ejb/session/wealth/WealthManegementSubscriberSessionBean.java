/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.customer.WealthManagementSubscriber;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author VIN-S
 */
@Stateless
public class WealthManegementSubscriberSessionBean implements WealthManegementSubscriberSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public WealthManagementSubscriber createWealthManagementSubscriber(WealthManagementSubscriber wms){
        em.persist(wms);
        return wms;
    }
    
    @Override
    public WealthManagementSubscriber getWealthManagementSubscriberById(Long id){
        return em.find(WealthManagementSubscriber.class, id);
    }
    
    @Override
    public WealthManagementSubscriber updateWealthManagementSubscriber(WealthManagementSubscriber wms){
        em.merge(wms);
        return wms;
    }
}
