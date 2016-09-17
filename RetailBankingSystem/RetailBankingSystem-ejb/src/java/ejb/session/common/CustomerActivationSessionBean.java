/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.Customer;
import entity.customer.MainAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CustomerActivationSessionBean implements CustomerActivationSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public MainAccount getMainAccountByEmail(String email){
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.email = :email");
        
        q.setParameter("email", email);
        
        Customer customer = null;
          
        try {
            customer = (Customer) q.getSingleResult();       
            return customer.getMainAccount();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public Boolean updateAccountStatus(MainAccount mainAccount){
        Long id = mainAccount.getId();
       
        try{
            MainAccount ma = (MainAccount) em.find(MainAccount.class , id); 
            ma.setStatus(MainAccount.StatusType.ACTIVE);
            em.merge(ma);
            em.flush();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
