/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.MainAccount;
import javax.ejb.Remote;
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author VIN-S
 */
@Remote
public interface CustomerActivationSessionBeanRemote {
    public MainAccount getMainAccountByEmail(String email) throws MainAccountNotExistException;
    public MainAccount updateMainAccount(MainAccount ma) throws UpdateMainAccountException;
}
