/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.CustomerCase;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.CaseStatus;
import server.utilities.GenerateAccountAndCCNumber;
import util.exception.cms.AllCustomerCaseException;
import util.exception.cms.CancelCustomerCaseException;
import util.exception.cms.CustomerCaseNotFoundByTitleException;
import util.exception.cms.CustomerCaseNotFoundException;
import util.exception.cms.DuplicateCaseExistException;
import util.exception.cms.UpdateCaseException;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CustomerCaseSessionBean implements CustomerCaseSessionBeanLocal, CustomerCaseSessionBeanRemote {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    private String generateReferenceNumber() {
        String referenceNumber = "";
        for (;;) {
            referenceNumber = GenerateAccountAndCCNumber.generateReferenceNumber();
            CustomerCase a = em.find(CustomerCase.class, referenceNumber);
            if (a == null) {
                return referenceNumber;
            }
        }
    }

    @Override
    public CustomerCase createCase(CustomerCase customerCase) throws DuplicateCaseExistException {

        try {
            if (customerCase.getId() == null) {
                customerCase.setId(generateReferenceNumber());
            } else {
                CustomerCase c = em.find(CustomerCase.class, customerCase.getId());
                if (c != null) {
                    throw new DuplicateCaseExistException("Duplicate Customer Case!");
                }
            }

            em.persist(customerCase);
            return customerCase;
        } catch (EntityExistsException e) {
            throw new DuplicateCaseExistException("Catch Duplicate Customer Case!");
        }

    }

    @Override
    public CustomerCase updateCase(CustomerCase customerCase) throws UpdateCaseException {

        try {

            if (customerCase.getId() == null) {
                throw new UpdateCaseException("Not an entity!");
            }

            em.merge(customerCase);
            return customerCase;
        } catch (IllegalArgumentException e) {
            throw new UpdateCaseException("Not an entity!");
        }

    }

    @Override
    public CustomerCase getCaseById(String id) throws CustomerCaseNotFoundException {

        if (id == null) {
            throw new CustomerCaseNotFoundException("Customer Case Not Found!");
        }

        try {
            
            CustomerCase c = em.find(CustomerCase.class, id);
            if (c == null) {
                throw new CustomerCaseNotFoundException("Customer Case Not Found!");
            }
            // REMARK: See if we need to check cancelstatus
            return c;
        } catch (IllegalArgumentException e) {
            throw new CustomerCaseNotFoundException("Customer Case Not Found!");
        }
        

//        Query q = em.createQuery("SELECT a FROM CustomerCase a WHERE "
//                                  + "a.id = :caseID AND " 
//                                  + "a.caseStatus != :cancelStatus");
//        
//        try{
//            q.setParameter("caseID", Long.parseLong(id));
//        }catch (Exception ex) {
//            System.out.println("CustomerCaseSessionBean.searchCaseById: "+ex.toString());
//            return null;
//        }
//        q.setParameter("cancelStatus", CaseStatus.CANCELLED);
//        
//        CustomerCase customerCase = null;
//          
//        try {
//            customerCase = (CustomerCase) q.getSingleResult();       
//            return customerCase;
//        } catch (NoResultException ex) {
//            System.out.println("CustomerCaseSessionBean.searchCaseById: "+ex.toString());
//            return null;
//        }
    }

    @Override
    public List<CustomerCase> getCaseByTitle(String title) throws CustomerCaseNotFoundByTitleException {
        
        Query q = em.createQuery("SELECT a FROM CustomerCase a WHERE "
                + "a.title LIKE :caseTitle AND "
                + "a.caseStatus != :cancelStatus ORDER BY a.createDate DESC");

        q.setParameter("caseTitle", '%' + title + '%');
        q.setParameter("cancelStatus", CaseStatus.CANCELLED);

        try {
            List<CustomerCase> result = q.getResultList();
            
            if (result.isEmpty()) {
                throw new CustomerCaseNotFoundByTitleException("Customer Case Not Found by Title:" + title);
            }
            
            return result;
        } catch (NoResultException ex) {
            System.out.println("Catch!");
            throw new CustomerCaseNotFoundByTitleException("Customer Case Not Found by Title:" + title);
        }
    }

    @Override
    public List<CustomerCase> getAllCase() throws AllCustomerCaseException {
        Query q = em.createQuery("SELECT a FROM CustomerCase a WHERE a.caseStatus != :cancelStatus ORDER BY a.createDate DESC");

        q.setParameter("cancelStatus", CaseStatus.CANCELLED);

        try {
            
            List<CustomerCase> result = q.getResultList();
            
            if (result.isEmpty()) {
                throw new AllCustomerCaseException("Customer Case Not Found");
            }
            
            return result;
        } catch (NoResultException ex) {
            System.out.println("Catch!");
            throw new AllCustomerCaseException("Customer Case Not Found");
        }
    }

    @Override
    public List<CustomerCase> getAllCaseUnderCertainStaff(StaffAccount staffAccount) {
        Query q = em.createQuery("SELECT a FROM CustomerCase a WHERE a.staffAccount.username = :staffUserName");

        q.setParameter("staffUserName", staffAccount.getUsername());

        try {
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("CustomerCaseSessionBean.getAllCaseUnderCertainStaff: " + ex.toString());
            return null;
        }
    }

    @Override
    public CustomerCase removeCase(String id) throws CancelCustomerCaseException {
        try {
            CustomerCase cc = em.find(CustomerCase.class, id);
            if (cc == null) {
                throw new CancelCustomerCaseException("Customer Case Cancel failed!");
            }
            em.remove(cc);
            return cc;
        } catch (IllegalArgumentException e) {
            throw new CancelCustomerCaseException("Customer Case Cancel failed!");
        }
    }

}
