/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author qiuxiaqing
 */
@Remote
public interface CustomerProfileSessionBeanRemote {

    public Customer getCustomerByUserID(String userID);
    public Customer saveProfile(Customer customer);
    public List<Customer> retrieveActivatedCustomers();   
    public Customer searchCustomerByIdentityNumber(String id);     
    public Customer getCustomerByID(Long ID);
}

