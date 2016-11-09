/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.customer.Customer;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.StatusType;
import server.utilities.GenerateAccountAndCCNumber;
import util.exception.cms.CustomerNotExistException;
import util.exception.cms.DuplicateCustomerExistException;
import util.exception.cms.UpdateCustomerException;

/**
 *
 * @author qiuxiaqing
 */
@Stateless
public class CustomerProfileSessionBean implements CustomerProfileSessionBeanLocal, CustomerProfileSessionBeanRemote {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    private String generateReferenceNumber() {
        String referenceNumber = "";
        for (;;) {
            referenceNumber = GenerateAccountAndCCNumber.generateReferenceNumber();
            Customer a = em.find(Customer.class, referenceNumber);
            if (a == null) {
                return referenceNumber;
            }
        }
    }

    @Override
    public Customer getCustomerByUserID(String userID) throws CustomerNotExistException {
        try {
            Query q = em.createQuery("SELECT c FROM Customer c WHERE c.mainAccount.userID = :inUserID");
            q.setParameter("inUserID", userID);
            return (Customer) q.getSingleResult();
        } catch (NoResultException ex) {
            throw new CustomerNotExistException(ex.getMessage());
        }
    }

    @Override
    public Customer createCustomer(Customer customer) throws DuplicateCustomerExistException {

        try {
            if (customer.getId() == null) {
                customer.setId(generateReferenceNumber());
            } else {
                Customer c = em.find(Customer.class, customer.getId());
                if (c != null) {
                    throw new DuplicateCustomerExistException("Duplicate Customer Case!");
                }
            }

            em.persist(customer);
            return customer;
        } catch (EntityExistsException e) {
            throw new DuplicateCustomerExistException("Catch Duplicate Customer Case!");
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) throws UpdateCustomerException {

        try {

            if (customer.getId() == null) {
                throw new UpdateCustomerException("Not an entity!");
            }

            em.merge(customer);
            return customer;
        } catch (IllegalArgumentException e) {
            throw new UpdateCustomerException("Not an entity!");
        }

    }

    @Override
    public List<Customer> retrieveActivatedCustomers() {

        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.mainAccount.status = :inStatus");
        q.setParameter("inStatus", StatusType.ACTIVE);

        return q.getResultList();

    }

    @Override
    public Customer searchCustomerByIdentityNumber(String id) throws CustomerNotExistException {

        try {
            Query q = em.createQuery(
                    "SELECT c FROM Customer c WHERE "
                    + "c.mainAccount.status = :inStatus AND "
                    + "c.identityNumber = :identityNumber"
            );
            q.setParameter("inStatus", StatusType.ACTIVE);
            q.setParameter("identityNumber", id);
            System.out.print("DB search ---------------" + q.getResultList().size());

            return (Customer) q.getSingleResult();
        } catch (NoResultException ex) {
            throw new CustomerNotExistException("Customer not found!");
        }
    }

    @Override
    public Customer getCustomerByID(String id) throws CustomerNotExistException {
        
        if (id == null) {
            return null;
        }

        try {
            
            Customer c = em.find(Customer.class, id);
            if (c == null) {
                throw new CustomerNotExistException("Customer Case Not Found!");
            }
            return c;
        } catch (IllegalArgumentException e) {
            throw new CustomerNotExistException("Customer Case Not Found!");
        }
    }

    @Override
    public List<Customer> getListCustomers() {
        Query q = em.createQuery("SELECT c from Customer c");
        return q.getResultList();
    }

}
