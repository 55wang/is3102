/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.mainaccount;

import entity.customer.MainAccount;
import javax.ejb.Local;
import util.exception.common.DuplicateMainAccountExistException;
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author leiyang
 */
@Local
public interface MainAccountSessionBeanLocal {
    public MainAccount createMainAccount(MainAccount ma) throws DuplicateMainAccountExistException ;
    public MainAccount updateMainAccount(MainAccount ma) throws UpdateMainAccountException ;
    public MainAccount getMainAccountByUserId(String userID) throws MainAccountNotExistException ;
    public MainAccount getMainAccountByEmail(String email) throws MainAccountNotExistException;
}
