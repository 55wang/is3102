/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardOrder;
import entity.customer.MainAccount;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.ApplicationStatus;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CreditCardOrderSessionBean implements CreditCardOrderSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<CreditCardOrder> getAllCreditCardOrders (){
        Query q = em.createQuery("SELECT a FROM CreditCardOrder a WHERE a.applicationStatus != :cancelStatus");
        
        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);
        
        try {   
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("CreditCardOrderSessionBean.getAllCreditCardOrders: "+ex.toString());
            return null;
        }
    }
    
    @Override
    public List<CreditCardOrder> getMainAccountAllCreditCardOrders (MainAccount mainAccount){
        Query q = em.createQuery("SELECT a FROM CreditCardOrder a WHERE a.applicationStatus != :cancelStatus "
                + "AND a.mainAccount.id = :id");
        
        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);
        q.setParameter("id", mainAccount.getId());
        
        try {   
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("CreditCardOrderSessionBean.getMainAccountAllCreditCardOrders: "+ex.toString());
            return null;
        }
    }
    
    @Override
    public CreditCardOrder searchCreditCardOrderByID(String id){
        Query q = em.createQuery("SELECT a FROM CreditCardOrder a WHERE "
                                  + "a.id = :ID AND " 
                                  + "a.applicationStatus != :cancelStatus");
        
        try{
            q.setParameter("ID", Long.parseLong(id));
        }catch (Exception ex) {
            System.out.println("CreditCardOrderSessionBean.searchCreditCardOrderByID: "+ex.toString());
            return null;
        }
        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);
        
        CreditCardOrder cco = null;
          
        try {
            cco = (CreditCardOrder) q.getSingleResult();       
            return cco;
        } catch (NoResultException ex) {
            System.out.println("CreditCardOrderSessionBean.searchCreditCardOrderByID: "+ex.toString());
            return null;
        }
    }
    
    @Override
    public CreditCardOrder searchMainAccountCreditCardOrderByID(MainAccount ma, String id){
        Query q = em.createQuery("SELECT a FROM CreditCardOrder a WHERE "
                                  + "a.id = :ID AND " 
                                  + "a.applicationStatus != :cancelStatus "
                                  + "AND a.mainAccount.id = :mainAccountId");
        
        try{
            q.setParameter("ID", Long.parseLong(id));
            q.setParameter("mainAccountId", ma.getId());
        }catch (Exception ex) {
            System.out.println("CreditCardOrderSessionBean.searchMainAccountCreditCardOrderByID: "+ex.toString());
            return null;
        }
        q.setParameter("cancelStatus", ApplicationStatus.CANCELLED);
        
        CreditCardOrder cco = null;
          
        try {
            cco = (CreditCardOrder) q.getSingleResult();       
            return cco;
        } catch (NoResultException ex) {
            System.out.println("CreditCardOrderSessionBean.searchMainAccountCreditCardOrderByID: "+ex.toString());
            return null;
        }
  
    }
    
    @Override
    public Boolean cancelCreditCardOrder(CreditCardOrder cco){
        try{
            cco.setApplicationStatus(ApplicationStatus.CANCELLED);
            em.merge(cco);
            em.flush();
            return true;
        }catch(Exception ex){
            System.out.println("CreditCardOrderSessionBean.cancelCreditCardOrder: "+ex.toString());
            return false;
        }
    }
}
