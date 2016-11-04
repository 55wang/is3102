/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cms;

import ejb.session.cms.CustomerProfileSessionBeanRemote;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.ArrayList;
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
import util.exception.CustomerNotExistException;

/**
 *
 * @author VIN-S
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

    public class CustomerProfileTest implements Serializable{
    CustomerProfileSessionBeanRemote customerProfileSessionBean = lookupCustomerProfileSessionBeanRemote();
    
    
    private static String revertInfo;
    
    public CustomerProfileTest() {
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
    
    @Test()
    public void test01getCustomerByUserID() throws CustomerNotExistException{
        System.out.println("CustomerProfileTest.test01getCustomerByID");   
        Customer result = customerProfileSessionBean.getCustomerByUserID("c1234567");
        assertNotNull(result);        
    }
    
    @Test(expected=CustomerNotExistException.class)
    public void test01getCustomerByUserIDException() throws CustomerNotExistException{
        System.out.println("CustomerProfileTest.test01getCustomerByID");   
        Customer result = customerProfileSessionBean.getCustomerByUserID("c1234568");
        assertNotNull(result);        
    }
    
//    @Test
//    public void test02saveProfile() {
//        System.out.println("CustomerProfileTest.test02saveProfile");
//        Customer tester = customerProfileSessionBean.getCustomerByUserID("c1234567");
//        revertInfo=tester.getLastname();
//        System.out.println("!!!"+revertInfo);
//        tester.setLastname("test");
//        Customer result = customerProfileSessionBean.saveProfile(tester);  
//        assertEquals(result, tester);
//    }
//    
//    @Test
//    public void test03revertSaveProfile() {
//        System.out.println("CustomerProfileTest.test03revertSaveProfile");
//        Customer tester = customerProfileSessionBean.getCustomerByUserID("c1234567");
//        System.out.println("!!!2"+revertInfo);
//        tester.setLastname(revertInfo);
//        Customer result = customerProfileSessionBean.saveProfile(tester);  
//        assertNotNull(result);
//    }
    
    @Test
    public void test04retrieveActivatedCustomers() {
        System.out.println("CustomerProfileTest.test04retrieveActivatedCustomers");
        List<Customer> resultlist = customerProfileSessionBean.retrieveActivatedCustomers();
        assertNotNull(resultlist);
    }
    
    @Test
    public void test05searchCustomerByIdentityNumber() {
        System.out.println("CustomerProfileTest.test05searchCustomerByIdentityNumber");   
        Customer tester = customerProfileSessionBean.searchCustomerByIdentityNumber("S1234567Z");
        assertNotNull(tester);        
    }
    
    @Test
    public void test06getCustomerByID() {
        System.out.println("CustomerProfileTest.test06getCustomerByID");   
        Customer tester = customerProfileSessionBean.getCustomerByID(1L);
        assertNotNull(tester);        
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    private CustomerProfileSessionBeanRemote lookupCustomerProfileSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CustomerProfileSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CustomerProfileSessionBean!ejb.session.cms.CustomerProfileSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }


}
