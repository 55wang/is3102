/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.DepositAccountProduct;
import entity.dams.account.DepositProduct;
import entity.dams.account.FixedDepositAccountProduct;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.DepositAccountType;

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
    
    @Override
    public List<DepositAccountProduct> getAllPresentCurrentDepositProducts() {
        Query q = em.createQuery("SELECT dp FROM DepositProduct dp WHERE dp.isHistory = false AND dp.type =:type");
        q.setParameter("type", DepositAccountType.CURRENT);
        List<DepositProduct> dps = q.getResultList();
        List<DepositAccountProduct> results = new ArrayList<>();
        if (dps == null) {
            return null;
        } else {
            for (DepositProduct dp : dps) {
                if (dp instanceof DepositAccountProduct) {
                    results.add((DepositAccountProduct) dp);
                }
            }
        }
        return results;
    }
    @Override
    public List<DepositAccountProduct> getAllPresentSavingsDepositProducts() {
        Query q = em.createQuery("SELECT dp FROM DepositProduct dp WHERE dp.isHistory = false AND dp.type =:type");
        q.setParameter("type", DepositAccountType.SAVING);
        List<DepositProduct> dps = q.getResultList();
        List<DepositAccountProduct> results = new ArrayList<>();
        if (dps == null) {
            return null;
        } else {
            for (DepositProduct dp : dps) {
                if (dp instanceof DepositAccountProduct) {
                    results.add((DepositAccountProduct) dp);
                }
            }
        }
        return results;
    }
    @Override
    public List<DepositAccountProduct> getAllPresentCustomDepositProducts() {
        Query q = em.createQuery("SELECT dp FROM DepositProduct dp WHERE dp.isHistory = false AND dp.type =:type");
        q.setParameter("type", DepositAccountType.CUSTOM);
        List<DepositProduct> dps = q.getResultList();
        List<DepositAccountProduct> results = new ArrayList<>();
        if (dps == null) {
            return null;
        } else {
            for (DepositProduct dp : dps) {
                if (dp instanceof DepositAccountProduct) {
                    results.add((DepositAccountProduct) dp);
                }
            }
        }
        return results;
    }
    @Override
    public List<FixedDepositAccountProduct> getAllPresentFixedDepositProducts() {
        Query q = em.createQuery("SELECT dp FROM DepositProduct dp WHERE dp.isHistory = false AND dp.type =:type");
        q.setParameter("type", DepositAccountType.FIXED);
        List<DepositProduct> dps = q.getResultList();
        List<FixedDepositAccountProduct> results = new ArrayList<>();
        if (dps == null) {
            return null;
        } else {
            for (DepositProduct dp : dps) {
                if (dp instanceof FixedDepositAccountProduct) {
                    results.add((FixedDepositAccountProduct) dp);
                }
            }
        }
        return results;
    }
    
}
