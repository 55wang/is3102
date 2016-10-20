/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.MobileAccount;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import server.utilities.ConstantUtils;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class EntityPayLahBuilder {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private LoginSessionBeanLocal loginBean;
    
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;
    
    public void initPayLahDemoData() {
        em.flush();
        MainAccount ma1 = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        em.refresh(ma1);
        DepositAccount da1 = ma1.getBankAcounts().get(0);
        em.refresh(da1);
        MobileAccount m1 = mobileBean.createMobileAccount(ma1);
        mobileBean.topupMobileAccount(m1, da1, new BigDecimal(500));
        MainAccount ma2 = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_2);
        em.refresh(ma2);
        MobileAccount m2 = mobileBean.createMobileAccount(ma2);
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
