/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.CustomerGroup;
import entity.customer.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CustomerSegmentationSessionBean implements CustomerSegmentationSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<String> getListCustomersHashTag() {
        Query q = em.createQuery("SELECT DISTINCT(c.hashTag) FROM Customer c");
        return q.getResultList();
    }
    
    @Override
    public List<Customer> getCustomersByOptions(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, Double actualIncome) {
        Query q = em.createQuery("Select c from Customer c WHERE c.depositRecency >=:inDepositRecency AND c.depositFrequency >=:inDepositFrequency AND c.depositMonetary >=:inDepositMonetary AND c.cardRecency >=:inCardRecency AND c.cardFrequency >=:inCardFrequency AND c.cardMonetary >=:inCardMonetary AND c.actualIncome >=:inIncome");
        q.setParameter("inDepositRecency", depositRecency);
        q.setParameter("inDepositFrequency", depositFrequency);
        q.setParameter("inDepositMonetary", depositMonetary);
        q.setParameter("inCardRecency", cardRecency);
        q.setParameter("inCardFrequency", cardFrequency);
        q.setParameter("inCardMonetary", cardMonetary);
        q.setParameter("inIncome", actualIncome);
        return q.getResultList();
    }

    @Override
    public List<Customer> getListFilterCustomersByRFM(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, Double actualIncome) {
        Query q = em.createQuery("Select c from Customer c WHERE c.depositRecency >=:inDepositRecency AND c.depositFrequency >=:inDepositFrequency AND c.depositMonetary >=:inDepositMonetary AND c.cardRecency >=:inCardRecency AND c.cardFrequency >=:inCardFrequency AND c.cardMonetary >=:inCardMonetary AND c.actualIncome >=:inIncome");
        q.setParameter("inDepositRecency", depositRecency);
        q.setParameter("inDepositFrequency", depositFrequency);
        q.setParameter("inDepositMonetary", depositMonetary);
        q.setParameter("inCardRecency", cardRecency);
        q.setParameter("inCardFrequency", cardFrequency);
        q.setParameter("inCardMonetary", cardMonetary);
        q.setParameter("inIncome", actualIncome);
        return q.getResultList();
    }

    @Override
    public List<Customer> getListFilterCustomersByRFMAndHashTag(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, String hashTag, Double actualIncome) {
        Query q = em.createQuery("Select c from Customer c WHERE c.depositRecency >=:inDepositRecency AND c.depositFrequency >=:inDepositFrequency AND c.depositMonetary >=:inDepositMonetary AND c.cardRecency >=:inCardRecency AND c.cardFrequency >=:inCardFrequency AND c.cardMonetary >=:inCardMonetary AND c.hashTag like :inHashTag AND c.actualIncome >=:inIncome");
        q.setParameter("inDepositRecency", depositRecency);
        q.setParameter("inDepositFrequency", depositFrequency);
        q.setParameter("inDepositMonetary", depositMonetary);
        q.setParameter("inCardRecency", cardRecency);
        q.setParameter("inCardFrequency", cardFrequency);
        q.setParameter("inCardMonetary", cardMonetary);
        q.setParameter("inHashTag", "%" + hashTag + "%");
        q.setParameter("inIncome", actualIncome);
        return q.getResultList();
    }

    @Override
    public List<CustomerGroup> getListCustomerGroup() {
        Query q = em.createQuery("Select m from CustomerGroup m");
        return q.getResultList();
    }

    @Override
    public CustomerGroup getCustomerGroup(Long Id) {
        Query q = em.createQuery("Select m from CustomerGroup m Where m.id =:inId");
        q.setParameter("inId", Id);
        return (CustomerGroup) q.getSingleResult();
    }

    @Override
    public CustomerGroup createCustomerGroup(CustomerGroup customerGroup) {
        em.persist(customerGroup);
        return customerGroup;
    }

    @Override
    public CustomerGroup updateCustomerGroup(CustomerGroup customerGroup) {
        em.merge(customerGroup);
        return customerGroup;
    }

}
