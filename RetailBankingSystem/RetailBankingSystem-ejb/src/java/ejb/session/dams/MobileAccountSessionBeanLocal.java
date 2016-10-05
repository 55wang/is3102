/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.customer.MainAccount;
import entity.dams.account.MobileAccount;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface MobileAccountSessionBeanLocal {
    public MobileAccount createMobileAccount(MainAccount ma);
}
