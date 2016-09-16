/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.Customer;
import entity.MainAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author qiuxiaqing
 */
@Stateless
public class CustomerProfileSessionBean implements CustomerProfileSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

   
    @Override
    public Customer getCustomerByUserID(String userID){    
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :inUserID");
        
        q.setParameter("inUserID", userID);
        
        MainAccount mainAccount = null;
          
        try {
            mainAccount = (MainAccount) q.getSingleResult();       
            return mainAccount.getCustomer();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public Boolean saveProfile(Customer customer){
        try{
            
            em.merge(customer);
            em.flush();
            
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    
    
}
