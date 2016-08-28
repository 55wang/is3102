/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.Customer;
import entity.MainAccount;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface LoginSessionBeanLocal {
    public MainAccount loginAccount(String username, String password); 
    public Customer getCustomerByUserID(String userID);
}
