/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.mainaccount;

import entity.customer.CustomerCase;
import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.GenerateAccountAndCCNumber;
import util.exception.cms.DuplicateCaseExistException;
import util.exception.cms.UpdateCaseException;
import util.exception.common.DuplicateMainAccountExistException;
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author leiyang
 */
@Stateless
public class MainAccountSessionBean implements MainAccountSessionBeanLocal, MainAccountSessionBeanRemote {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    private String generateReferenceNumber() {
        String referenceNumber = "";
        for (;;) {
            referenceNumber = GenerateAccountAndCCNumber.generateReferenceNumber();
            MainAccount a = em.find(MainAccount.class, referenceNumber);
            if (a == null) {
                return referenceNumber;
            }
        }
    }
    
    @Override
    public MainAccount createMainAccount(MainAccount ma) throws DuplicateMainAccountExistException {
        
        try {
            if (ma.getId() == null) {
                ma.setId(generateReferenceNumber());
            } else {
                MainAccount m = em.find(MainAccount.class, ma.getId());
                if (m != null) {
                    throw new DuplicateMainAccountExistException("Duplicate Main Account!");
                }
            }

            em.persist(ma);
            return ma;
        } catch (EntityExistsException e) {
            throw new DuplicateMainAccountExistException("Catch Duplicate Main Account!");
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

    @Override
    public MainAccount getMainAccountByUserId(String userID)  throws MainAccountNotExistException  {

        Query q = em.createQuery("SELECT ma FROM MainAccount ma WHERE ma.userID = :userID");
        q.setParameter("userID", userID);

        try {
            List<MainAccount> accounts = q.getResultList();
            if (accounts != null && !accounts.isEmpty() && accounts.size() == 1) {
                return accounts.get(0);
            } else {
                throw new MainAccountNotExistException("Main Account not found with ID:" + userID);
            }
        } catch (NoResultException ex) {
            throw new MainAccountNotExistException("Main Account not found with ID:" + userID);
        }
    }
    
    @Override
    public MainAccount getMainAccountByEmail(String email) throws MainAccountNotExistException{
        Query q = em.createQuery("SELECT ma FROM MainAccount ma WHERE ma.customer.email = :email");
        
        q.setParameter("email", email);
        
        System.out.println("MainAccount Email is:" + email);
             
        try {
            List<MainAccount> accounts = q.getResultList();
            System.out.println(accounts);
            if (!accounts.isEmpty()) {
                return accounts.get(0);
            } else {
                throw new MainAccountNotExistException("Main Account not found with email:" + email);
            }
        } catch (NoResultException ex) {
            System.out.println("Catch");
            throw new MainAccountNotExistException("Main Account not found with email:" + email);
        }
    }
}
