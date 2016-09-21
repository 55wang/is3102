/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utils.EnumUtils.StatusType;

/**
 *
 * @author qiuxiaqing
 */
@Stateless
public class CustomerProfileSessionBean implements CustomerProfileSessionBeanLocal {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Customer getCustomerByUserID(String userID) {
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :inUserID");

        q.setParameter("inUserID", userID);

        MainAccount mainAccount = null;

        try {
            mainAccount = (MainAccount) q.getSingleResult();
            return mainAccount.getCustomer();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Customer saveProfile(Customer customer) {

        try {

            em.merge(customer);
            em.flush();

            emailServiceSessionBean.sendUpdatedProfile(customer.getEmail());
            return customer;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Customer> retrieveActivatedCustomers() {

        
        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.mainAccount.status = :inStatus");
        q.setParameter("inStatus", StatusType.ACTIVE);

        return q.getResultList();
        
 
    }

    @Override
    public List<Customer> searchCustomerByIdentityNumber(String id) {

        Query q = em.createQuery(
                "SELECT c FROM Customer c WHERE "
                + "c.mainAccount.status = :inStatus AND "
                + "c.identityNumber = :identityNumber"
        );
        q.setParameter("inStatus", StatusType.ACTIVE);
        q.setParameter("identityNumber", id);
        System.out.print("DB search ---------------" + q.getResultList().size());

        return q.getResultList();
    }

}
