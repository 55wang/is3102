/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.MobileAccount;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.ConstantUtils;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class EntityPayLahBuilder {

    @EJB
    private LoginSessionBeanLocal loginBean;
    
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;
    
    public void initPayLahDemoData() {
        MainAccount ma1 = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        MobileAccount m1 = mobileBean.createMobileAccount(ma1);
        MainAccount ma2 = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_2);
        MobileAccount m2 = mobileBean.createMobileAccount(ma2);
    }
}
