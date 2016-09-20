/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.DepositProduct;
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
public class DepositProductSessionBean implements DepositProductSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public DepositProduct createDepositProduct(DepositProduct dp) {
        try {
            em.persist(dp);
            return dp;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public DepositProduct updateDepositProduct(DepositProduct dp) {
        try {
            em.merge(dp);
            return dp;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public DepositProduct getDepositProductByName(String name) {
        Query q = em.createQuery("SELECT dp FROM DepositProduct dp WHERE dp.name = :name");
        
        q.setParameter("name", name);
        
        DepositProduct dp = null;
          
        try {
            List<DepositProduct> dps = q.getResultList();
            if (dps != null && !dps.isEmpty() && dps.size() == 1) {
                return dps.get(0);
            } else {
                return null;
            }
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public List<DepositProduct> getAllPresentProducts() {
        Query q = em.createQuery("SELECT dp FROM DepositProduct dp WHERE dp.isHistory = false");
        return q.getResultList();
    }
}
