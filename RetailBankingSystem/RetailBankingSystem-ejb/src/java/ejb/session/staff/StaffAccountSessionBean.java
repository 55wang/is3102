/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.StaffAccount;
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
    public Boolean createAccount(StaffAccount sa) {
        try {
            em.persist(sa);
            return true;
        } catch (EntityExistsException e) {
            return false;
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
