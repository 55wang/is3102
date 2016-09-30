/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.mainaccount;

import entity.customer.MainAccount;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface MainAccountSessionBeanLocal {
    public MainAccount updateMainAccount(MainAccount ma);
    public MainAccount getMainAccountByUserId(String userID);
    public MainAccount createMainAccount(MainAccount ma);
}
