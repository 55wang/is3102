/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cms;

import ejb.session.cms.CustomerCaseSessionBeanRemote;
import ejb.session.staff.StaffAccountSessionBeanRemote;
import entity.customer.CustomerCase;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class CustomerCaseTest implements Serializable {
    
    CustomerCaseSessionBeanRemote customerCaseSessionBean = lookupCustomerCaseSessionBeanRemote();
    StaffAccountSessionBeanRemote staffBean = lookupStaffAccountSessionBeanRemote();
    
    private final String TEST_CASE_ID = "c1234567";
    private final String TEST_CASE_TITLE = "Loan enquiry";
    private final String TEST_CASE_RM_ACCOUNT = "relationship_manager";
    private final String TEST_CASE_LOAN_ACCOUNT = "loan_officer";
        
    public CustomerCaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void test01saveCase() throws DuplicateCaseExistException {
        System.out.println("CustomerCaseTest.test01saveCase");   
        CustomerCase testCase = new CustomerCase();
        testCase.setId(TEST_CASE_ID);
        testCase.setTitle(TEST_CASE_TITLE);
        CustomerCase result = customerCaseSessionBean.createCase(testCase);
        assertNotNull(result);        
    }
    
    @Test(expected=DuplicateCaseExistException.class)
    public void test02saveCaseConflictException() throws DuplicateCaseExistException {
        System.out.println("CustomerCaseTest.test02saveCaseConflictException"); 
        CustomerCase testCase = new CustomerCase();
        testCase.setId(TEST_CASE_ID);
        CustomerCase result = customerCaseSessionBean.createCase(testCase);
        System.out.println(result);
        result = customerCaseSessionBean.createCase(result);
        System.out.println(result);
        assertNotNull(result);        
    }
    
    @Test
    public void test03updateCase() throws UpdateCaseException, CustomerCaseNotFoundException {
        System.out.println("CustomerCaseTest.test03updateCase");   
        CustomerCase testCase = customerCaseSessionBean.getCaseById(TEST_CASE_ID);
        testCase.setTitle(TEST_CASE_TITLE);
        CustomerCase result = customerCaseSessionBean.updateCase(testCase);
        assertEquals(result, testCase);
    }
    
    @Test(expected=UpdateCaseException.class)
    public void test04updateCaseSaveException() throws UpdateCaseException {
        System.out.println("CustomerCaseTest.test04updateCaseSaveException");   
        CustomerCase testCase = new CustomerCase();
        CustomerCase result = customerCaseSessionBean.updateCase(testCase);
        assertEquals(result, testCase);      
    }
    
    @Test
    public void test05searchCaseByID() throws CustomerCaseNotFoundException {
        System.out.println("CustomerCaseTest.test05searchCaseByID");
        CustomerCase testCase = customerCaseSessionBean.getCaseById(TEST_CASE_ID);
        assertNotNull(testCase);
    }
    
    @Test(expected=CustomerCaseNotFoundException.class)
    public void test06searchCaseByIDException() throws CustomerCaseNotFoundException {
        System.out.println("CustomerCaseTest.test06searchCaseByIDExceotion");   
        CustomerCase testCase = customerCaseSessionBean.getCaseById("2");
        assertNotNull(testCase);    
    }
    
    @Test
    public void test07searchCaseByTitle() throws CustomerCaseNotFoundByTitleException {
        System.out.println("CustomerCaseTest.test07searchCaseByTitle");
        List<CustomerCase> testCase = customerCaseSessionBean.getCaseByTitle(TEST_CASE_TITLE);
        assertEquals(testCase.size() > 0, true);
    }
    
    @Test(expected=CustomerCaseNotFoundByTitleException.class)
    public void test08searchCaseByTitleException() throws CustomerCaseNotFoundByTitleException {
        System.out.println("CustomerCaseTest.test08searchCaseByTitleException");   
        List<CustomerCase> testCase = customerCaseSessionBean.getCaseByTitle("2");
        assertEquals(testCase.size() > 0, true);   
    }
    
    @Test
    public void test09cancelCase() throws CancelCustomerCaseException {
        System.out.println("CustomerCaseTest.test09cancelCase");
        CustomerCase result = customerCaseSessionBean.removeCase(TEST_CASE_ID);
        System.out.println(result);
        assertNotNull(result);  
    }
    
    @Test(expected=CancelCustomerCaseException.class)
    public void test10cancelCaseException() throws CancelCustomerCaseException {
        System.out.println("CustomerCaseTest.test10cancelCaseException");   
        CustomerCase result = customerCaseSessionBean.removeCase(TEST_CASE_ID);
        assertNotNull(result);    
    }
    
    @Test
    public void test11getAllCase() throws AllCustomerCaseException {
        System.out.println("CustomerCaseTest.test11getAllCase");
        List<CustomerCase> result = customerCaseSessionBean.getAllCase();
        assertNotNull(result);
    }
    
    @Test
    public void test12getAllCaseUnderCertainStaff() throws AllCustomerCaseException {
        System.out.println("CustomerCaseTest.test12getAllCaseUnderCertainStaff");
        StaffAccount sa = staffBean.getAccountByUsername(TEST_CASE_RM_ACCOUNT);
        List<CustomerCase> result = customerCaseSessionBean.getAllCaseUnderCertainStaff(sa);
        assertNotNull(result);
    }
    
    @Test
    public void test13getAllCaseUnderCertainStaff() throws AllCustomerCaseException {
        System.out.println("CustomerCaseTest.test13getAllCaseUnderCertainStaff");
        StaffAccount sa = staffBean.getAccountByUsername(TEST_CASE_LOAN_ACCOUNT);
        List<CustomerCase> result = customerCaseSessionBean.getAllCaseUnderCertainStaff(sa);
        assertNotNull(result);
    }
    
    @Test(expected=AllCustomerCaseException.class)
    public void test14getAllCaseException() throws AllCustomerCaseException, CancelCustomerCaseException {
        System.out.println("CustomerCaseTest.test13getAllCaseException");  
        
        List<CustomerCase> result = customerCaseSessionBean.getAllCase();
        
        for (CustomerCase r : result) {
            customerCaseSessionBean.removeCase(r.getId());
        }
        
        result = customerCaseSessionBean.getAllCase();
        
        assertNotNull(result);    
    }

    private CustomerCaseSessionBeanRemote lookupCustomerCaseSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CustomerCaseSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CustomerCaseSessionBean!ejb.session.cms.CustomerCaseSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private StaffAccountSessionBeanRemote lookupStaffAccountSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StaffAccountSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/StaffAccountSessionBean!ejb.session.staff.StaffAccountSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
