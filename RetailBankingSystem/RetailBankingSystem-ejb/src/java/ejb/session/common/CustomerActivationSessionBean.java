/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.Customer;
import entity.customer.MainAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import util.exception.common.MainAccountNotExistException;
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
    public MainAccount getMainAccountByEmail(String email) throws MainAccountNotExistException{
        Query q = em.createQuery("SELECT ma FROM MainAccount ma WHERE ma.customer.email = :email");
        
        q.setParameter("email", email);
             
        try {
            List<MainAccount> accounts = q.getResultList();
            if (accounts != null && !accounts.isEmpty() && accounts.size() == 1) {
                return accounts.get(0);
            } else {
                throw new MainAccountNotExistException("Main Account not found with email:" + email);
            }
        } catch (NoResultException ex) {
            throw new MainAccountNotExistException("Main Account not found with email:" + email);
        }
    }
    
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
