/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.mainaccount;

import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class MainAccountSessionBean implements MainAccountSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public MainAccount updateMainAccount(MainAccount ma) {
        em.merge(ma);
        return ma;
    }
    
    @Override
    public MainAccount getMainAccountByUserId(String userID) {
        
        Query q = em.createQuery("SELECT ma FROM MainAccount ma WHERE ma.userID = :userID");
        
        q.setParameter("userID", userID);
        
        MainAccount sa = null;
          
        try {
            List<MainAccount> accounts = q.getResultList();
            if (accounts != null && !accounts.isEmpty() && accounts.size() == 1) {
                return accounts.get(0);
            } else {
                return null;
            }
        } catch (NoResultException ex) {
            return null;
        }
    }
}
