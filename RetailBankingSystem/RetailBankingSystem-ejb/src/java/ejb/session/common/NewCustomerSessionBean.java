/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author VIN-S
 */
@Stateless
public class NewCustomerSessionBean implements NewCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setFullName(customer.getFirstname() + " " + customer.getLastname());
        customer.setAge(customer.calcAge().intValue());
        em.persist(customer);
        return customer;
    }
    
    @Override
    public Customer updateCustomer(Customer customer) {
        customer.setFullName(customer.getFirstname() + " " + customer.getLastname());
        customer.setAge(customer.calcAge().intValue());
        em.merge(customer);
        return customer;
    }
}
