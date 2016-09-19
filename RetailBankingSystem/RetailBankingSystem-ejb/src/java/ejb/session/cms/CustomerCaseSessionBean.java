/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.CustomerCase;
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
public class CustomerCaseSessionBean implements CustomerCaseSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public MainAccount getMainAccountByUserID(String userID){    
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :inUserID");
        
        q.setParameter("inUserID", userID);
        
        MainAccount mainAccount = null;
          
        try {
            mainAccount = (MainAccount) q.getSingleResult();       
            return mainAccount;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public Boolean saveCase(CustomerCase customerCase){
        
        try{
            
            em.merge(customerCase);
            em.flush();
          
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
