/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.dams.MobileAccountSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.MobileAccount;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.ConstantUtils;
import util.exception.common.MainAccountNotExistException;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class EntityPayLahBuilder {

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;
    
    public void initPayLahDemoData() {
        try{
            MainAccount ma1 = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_1);
            MobileAccount m1 = mobileBean.createMobileAccount(ma1);
            MainAccount ma2 = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_0);
            MobileAccount m2 = mobileBean.createMobileAccount(ma2);
        }catch (MainAccountNotExistException ex) {
            System.out.println("EntityPayLahBuilder.initPayLahDemoData.MainAccountNotExistException");
        }
    }
}
