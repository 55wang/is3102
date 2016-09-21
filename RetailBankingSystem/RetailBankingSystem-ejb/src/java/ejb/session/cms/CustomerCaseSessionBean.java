/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.CustomerCase;
import entity.customer.MainAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utils.EnumUtils.CaseStatus;

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
        customerCase.setCaseStatus(CaseStatus.ONHOLD);
        
        try{
            
            em.merge(customerCase);
            em.flush();
          
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public CustomerCase searchCaseByID(String id){
        Query q = em.createQuery("SELECT a FROM CustomerCase a WHERE "
                                  + "a.id = :caseID AND " 
                                  + "a.caseStatus != :cancelStatus");
        
        try{
            q.setParameter("caseID", Long.parseLong(id));
        }catch (Exception ex) {
            System.out.println("CustomerCaseSessionBean.searchCaseById: "+ex.toString());
            return null;
        }
        q.setParameter("cancelStatus", CaseStatus.CANCELLED);
        
        CustomerCase customerCase = null;
          
        try {
            customerCase = (CustomerCase) q.getSingleResult();       
            return customerCase;
        } catch (NoResultException ex) {
            System.out.println("CustomerCaseSessionBean.searchCaseById: "+ex.toString());
            return null;
        }
    }

    @Override
    public List<CustomerCase> searchCaseByTitle(String title){
        Query q = em.createQuery("SELECT a FROM CustomerCase a WHERE "
                                 + "a.title = :caseTitle AND "
                                 + "a.caseStatus != :cancelStatus");
        
        q.setParameter("caseTitle", title);
        q.setParameter("cancelStatus", CaseStatus.CANCELLED);
        
        try {   
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("CustomerCaseSessionBean.searchCaseByTitle: "+ex.toString());
            return null;
        }
    }
    
    @Override
    public List<CustomerCase> getAllCase(){
        Query q = em.createQuery("SELECT a FROM CustomerCase a WHERE a.caseStatus != :cancelStatus");
        
        q.setParameter("cancelStatus", CaseStatus.CANCELLED);
        
        try {   
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("CustomerCaseSessionBean.getAllCase: "+ex.toString());
            return null;
        }
    }
    
    @Override
    public Boolean cancelCase(Long id){
        CustomerCase cc = (CustomerCase) em.find(CustomerCase.class, id); 
        em.remove(cc);
        em.flush();
           
        return true;
    }
    
    public void persist(Object object) {
        em.persist(object);
    }
}
