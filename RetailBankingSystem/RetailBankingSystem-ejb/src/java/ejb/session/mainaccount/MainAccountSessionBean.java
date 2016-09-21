/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.mainaccount;

import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leiyang
 */
@Stateless
public class MainAccountSessionBean implements MainAccountSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public MainAccount updateMainAccount(MainAccount ma) {
        em.merge(ma);
        return ma;
    }
    
    @Override
    public MainAccount addDepositAccountToMainAccount(DepositAccount da, MainAccount ma) {
        da.setMainAccount(ma);
        ma.addDepositAccount(da);
        em.merge(ma);
        em.merge(da);
        em.flush();
        return ma;
    }
}
