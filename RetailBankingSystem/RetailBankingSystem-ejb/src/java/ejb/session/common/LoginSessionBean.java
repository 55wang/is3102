/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.Customer;
import entity.MainAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class LoginSessionBean implements LoginSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public MainAccount loginAccount(String username, String password) {
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :inUserName "
                + "AND a.password = :inPassword");
        q.setParameter("inUserName", username);
        q.setParameter("inPassword", password);

        MainAccount user = null;

        try {
            user = (MainAccount) q.getSingleResult();
            return user;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public Customer getCustomerByUserID(String userID){    
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :userID");
        
        q.setParameter("userID", userID);
        
        MainAccount mainAccount = null;
          
        try {
            mainAccount = (MainAccount) q.getSingleResult();       
            return mainAccount.getCustomer();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public MainAccount getMainAccountByEmail(String email){
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.email = :email");
        
        q.setParameter("email", email);
        
        Customer customer = null;
          
        try {
            customer = (Customer) q.getSingleResult(); 
            if(customer != null)
                return customer.getMainAccount();
            else
                return null;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public List<MainAccount> showAllAccounts() {
        Query q = em.createQuery("SELECT a FROM MainAccount a");
        return q.getResultList();
    }
}
