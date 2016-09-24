/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.utils;

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
public class UtilsSessionBean implements UtilsSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Object find(Class type, Long id) {
        return em.find(type, id);
    }

    @Override
    public List<Object> findAll(String entityName) {
        String s = "SELECT e FROM " + entityName + " e";
        Query q = em.createQuery(s);
        return q.getResultList();
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

    @Override
    public Boolean checkIdentityNumberIsUnique(String identityNumber) {
        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.identityNumber = :identityNumber");
        q.setParameter("identityNumber", identityNumber);
        return q.getResultList().isEmpty();
    }

    @Override
    public Boolean checkEmailIsUnique(String email) {
        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.email = :email");
        q.setParameter("email", email);
        return q.getResultList().isEmpty();
    }

    @Override
    public Boolean checkPhoneIsUnique(String phone) {
        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.phone = :phone");
        q.setParameter("phone", phone);
        return q.getResultList().isEmpty();
    }
}
