/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.Customer;
import java.util.List;
import javax.ejb.Remote;
import util.exception.cms.CustomerNotExistException;
import util.exception.cms.DuplicateCustomerExistException;
import util.exception.cms.UpdateCustomerException;

/**
 *
 * @author qiuxiaqing
 */

@Remote
public interface CustomerProfileSessionBeanRemote {

    public Customer getCustomerByUserID(String userID) throws CustomerNotExistException;
    public Customer createCustomer(Customer customer) throws DuplicateCustomerExistException;
    public Customer updateCustomer(Customer customer) throws UpdateCustomerException;
    public List<Customer> retrieveActivatedCustomers();   
    public Customer searchCustomerByIdentityNumber(String id) throws CustomerNotExistException;     
    public Customer getCustomerByID(String id) throws CustomerNotExistException;
}

