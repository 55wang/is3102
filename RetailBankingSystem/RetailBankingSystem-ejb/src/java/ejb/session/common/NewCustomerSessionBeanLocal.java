/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.Customer;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface NewCustomerSessionBeanLocal {
    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
}
