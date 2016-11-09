/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.Customer;
import entity.customer.MainAccount;
import javax.ejb.Local;
import util.exception.cms.CustomerNotExistException;
import util.exception.common.LoginFailException;
import util.exception.common.MainAccountNotExistException;

/**
 *
 * @author wang
 */
@Local
public interface LoginSessionBeanLocal {
    public MainAccount loginAccount(String username, String password) throws LoginFailException; 
    public Customer getCustomerByUserID(String userID) throws CustomerNotExistException;
    public MainAccount getMainAccountByUserIC(String userIC) throws MainAccountNotExistException;
}
