/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class StaffAccountSessionBean implements StaffAccountSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public StaffAccount loginAccount(String username, String password) {
        try {
            StaffAccount user = em.find(StaffAccount.class, username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public StaffAccount getAccountByUsername(String username) {
        try {
            StaffAccount user = em.find(StaffAccount.class, username);
            return user;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public StaffAccount getAccountByEmail(String email) {
        
        Query q = em.createQuery("SELECT sa FROM StaffAccount sa WHERE sa.email = :email");
        
        q.setParameter("email", email);
        
        StaffAccount sa = null;
          
        try {
            List<StaffAccount> accounts = q.getResultList();
            if (accounts != null && !accounts.isEmpty() && accounts.size() == 1) {
                return accounts.get(0);
            } else {
                return null;
            }
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public StaffAccount createAccount(StaffAccount sa) {
        try {
            em.persist(sa);
            return sa;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public StaffAccount updateAccount(StaffAccount sa) {
        try {
            em.merge(sa);
            return sa;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public List<StaffAccount> getAllStaffs() {
        Query q = em.createQuery("SELECT sa FROM StaffAccount sa");
        return q.getResultList();
    }
    
    @Override
    public StaffAccount getStaffById(String id) {
        if (id != null) {
            return em.find(StaffAccount.class, id);
        } else {
            return null;
        }
    }
    
    @Override
    public List<StaffAccount> searchStaffByUsernameOrName(String searchText) {
        Query q = em.createQuery(
                "SELECT sa FROM StaffAccount sa WHERE "
                + "LOWER(sa.username) LIKE :searchText OR "
                + "LOWER(sa.firstName) LIKE :searchText OR "
                + "LOWER(sa.lastName) LIKE :searchText"
        );
        q.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
        return q.getResultList();
    }
}
