/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.customer.MainAccount;
import entity.dams.account.MobileAccount;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class MobileAccountSessionBean implements MobileAccountSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public MobileAccount createMobileAccount(MainAccount ma) {
        MobileAccount mobileAccount = new MobileAccount();
        mobileAccount.setMainAccount(ma);
        mobileAccount.setPhoneNumber(ma.getCustomer().getPhone());
        em.persist(mobileAccount);
        return mobileAccount;
    }
    
}
