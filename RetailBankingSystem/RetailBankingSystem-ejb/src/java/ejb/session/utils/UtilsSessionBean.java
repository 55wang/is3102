/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.utils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leiyang
 */
@Stateless
public class UtilsSessionBean implements UtilsSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Object find(Class type, Long id) {
        return em.find(type, id);
    }
    
    @Override
    public Object persist(Object object) {
        em.persist(object);
        return object;
    }
    
    @Override
    public Object merge(Object object) {
        em.merge(object);
        return object;
    }
}
