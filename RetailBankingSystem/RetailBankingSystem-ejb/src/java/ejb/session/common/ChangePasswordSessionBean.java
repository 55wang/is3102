/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.MainAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author VIN-S
 */
@Stateless
public class ChangePasswordSessionBean implements ChangePasswordSessionBeanLocal, ChangePasswordSessionBeanRemote {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public MainAccount changeMainAccountPwd(MainAccount mainAccount) throws UpdateMainAccountException {
        
        try {
            if (mainAccount.getId() == null) {
                throw new UpdateMainAccountException("Not an entity!");
            }

            em.merge(mainAccount);
            return mainAccount;
        } catch (IllegalArgumentException e) {
            throw new UpdateMainAccountException("Not an entity!");
        }
    }
}
