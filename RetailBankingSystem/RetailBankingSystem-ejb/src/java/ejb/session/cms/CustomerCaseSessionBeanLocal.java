/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.CustomerCase;
import entity.customer.MainAccount;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface CustomerCaseSessionBeanLocal {
    public MainAccount getMainAccountByUserID(String userID);
    public Boolean saveCase(CustomerCase customerCase);
}
