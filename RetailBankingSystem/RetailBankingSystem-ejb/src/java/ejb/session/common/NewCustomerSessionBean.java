/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import SMSMessaging.SendTextMessage;
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
    public void createCustomer(Customer customer, MainAccount mainAccount) {
        customer.setMainAccount(mainAccount);
        em.persist(customer);
        SendTextMessage.sendText("6581567758", "testing2");
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
