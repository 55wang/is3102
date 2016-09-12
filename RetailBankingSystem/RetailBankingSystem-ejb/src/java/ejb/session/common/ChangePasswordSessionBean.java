/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.MainAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author VIN-S
 */
@Stateless
public class ChangePasswordSessionBean implements ChangePasswordSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Boolean changePwd(String newPwd, MainAccount mainAccount){
        try{
            MainAccount ma = (MainAccount) em.find(MainAccount.class, mainAccount.getId()); 
            ma.setPassword(newPwd);
            em.merge(ma);
            em.flush();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
