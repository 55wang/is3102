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
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CustomerActivationSessionBean implements CustomerActivationSessionBeanLocal, CustomerActivationSessionBeanRemote {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    @Override
    public MainAccount updateMainAccount(MainAccount ma) throws UpdateMainAccountException {
        
        try {

            if (ma.getId() == null) {
                throw new UpdateMainAccountException("Not an entity!");
            }

            em.merge(ma);
            return ma;
        } catch (IllegalArgumentException e) {
            throw new UpdateMainAccountException("Not an entity!");
        }
        
    }
}
