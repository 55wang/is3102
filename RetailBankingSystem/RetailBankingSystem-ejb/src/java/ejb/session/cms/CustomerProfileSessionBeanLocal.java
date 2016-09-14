/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuxiaqing
 */
@Local
public interface CustomerProfileSessionBeanLocal {

    Customer getCustomerByUserID(String userID);
    Boolean saveProfile(Customer customer);
    
}

