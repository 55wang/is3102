/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.counter;

import entity.counter.ServiceChargeTransaction;
import entity.counter.TellerCounter;
import entity.staff.ServiceCharge;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class TellerCounterSessionBean implements TellerCounterSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // teller counter
    @Override
    public TellerCounter createTellerCounter(TellerCounter object) {
        em.persist(object);
        return object;
    }
    
    @Override
    public TellerCounter updateTellerCounter(TellerCounter object) {
        em.merge(object);
        return object;
    }
    
    @Override
    public TellerCounter getTellerCounterById(Long id) {
        return em.find(TellerCounter.class, id);
    }
    
    // service charge transaction
    @Override
    public ServiceChargeTransaction createServiceChargeTransaction(ServiceChargeTransaction object) {
        em.persist(object);
        return object;
    }
    
    @Override
    public ServiceChargeTransaction updateServiceChargeTransaction(ServiceChargeTransaction object) {
        em.merge(object);
        return object;
    }
    
    // service charge
    @Override
    public ServiceCharge createServiceCharge(ServiceCharge object) {
        em.persist(object);
        return object;
    }
    
    @Override
    public ServiceCharge updateServiceCharge(ServiceCharge object) {
        em.merge(object);
        return object;
    }
    
    @Override
    public ServiceCharge getServiceChargeByName(String name) {
        return em.find(ServiceCharge.class, name);
    }
    
    @Override
    public List<ServiceCharge> getAllServiceCharges() {
        Query q = em.createQuery("SELECT sc FROM ServiceCharge sc");
        return q.getResultList();
    }
}
