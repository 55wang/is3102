/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.card.account.CreditCardAccount;
import entity.customer.Customer;
import entity.dams.account.DepositAccount;
import entity.wealth.Portfolio;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotExistException;

/**
 *
 * @author qiuxiaqing
 */
@Local
public interface CustomerProfileSessionBeanLocal {

//    public Customer getCustomerByUserID (String userID) throws CustomerNotExistException;
    public Customer getCustomerByUserID (String userID);
    public Customer saveProfile(Customer customer);
    public List<Customer> retrieveActivatedCustomers();   
    public Customer searchCustomerByIdentityNumber(String id);     
    public Customer getCustomerByID(Long ID);
    public List<Customer> getListCustomers();
}

