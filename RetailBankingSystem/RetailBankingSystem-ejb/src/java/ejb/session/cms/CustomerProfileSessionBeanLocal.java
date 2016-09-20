/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.Customer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuxiaqing
 */
@Local
public interface CustomerProfileSessionBeanLocal {

    public Customer getCustomerByUserID(String userID);
    public Boolean saveProfile(Customer customer);
    public List<Customer> retrieveActivatedCustomers();   
    public List<Customer> searchCustomerByIdentityNumber(String id);     
 
}

