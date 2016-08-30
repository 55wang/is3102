/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.Customer;
import entity.MainAccount;
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
    public void createCustomer(Customer customer, MainAccount mainAccount){
        customer.setMainAccount(mainAccount);
        mainAccount.setCustomer(customer);
        em.persist(customer);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
