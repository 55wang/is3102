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
import util.exception.cms.CustomerNotExistException;
import util.exception.common.LoginFailException;
import util.exception.common.MainAccountNotExistException;

/**
 *
 * @author wang
 */
@Stateless
public class LoginSessionBean implements LoginSessionBeanLocal, LoginSessionBeanRemote{

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public MainAccount loginAccount(String username, String password) throws LoginFailException{
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :inUserName "
                + "AND a.password = :inPassword");
        q.setParameter("inUserName", username);
        q.setParameter("inPassword", password);

        try {
            MainAccount user = (MainAccount) q.getSingleResult();
            return user;
        } catch (NoResultException ex) {
            throw new LoginFailException("LoginFail");
        }
    }

    @Override
    public Customer getCustomerByUserID(String userID) throws CustomerNotExistException{
        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.mainAccount.userID = :userID");
        q.setParameter("userID", userID);
        try{
            Customer c = (Customer) q.getSingleResult();
            return c;
        }catch (NoResultException ex) {
            throw new CustomerNotExistException("getCustomerByUserID.CustomerNotExistException");
        }
    }

    @Override
    public MainAccount getMainAccountByUserIC(String userIC) throws MainAccountNotExistException{
        try {
            Query q = em.createQuery("SELECT ma FROM MainAccount ma WHERE ma.customer.identityNumber = :userIC");
            q.setParameter("userIC", userIC);
            return (MainAccount) q.getSingleResult();
        } catch (NoResultException e) {
            throw new MainAccountNotExistException("getMainAccountByUserIC.MainAccountNotExistException");
        }
    }

//    public List<MainAccount> showAllAccounts() {
//        Query q = em.createQuery("SELECT a FROM MainAccount a");
//        return q.getResultList();
//    }
}
