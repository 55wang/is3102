/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import ejb.session.common.CustomerActivationSessionBeanRemote;
import ejb.session.mainaccount.MainAccountSessionBeanRemote;
import entity.customer.MainAccount;
import java.io.Serializable;
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
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author VIN-S
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerActivationTest implements Serializable {
    MainAccountSessionBeanRemote mainBean = lookupMainAccountSessionBeanRemote();
    
    CustomerActivationSessionBeanRemote customerActivationSessionBean = lookupCustomerActivationSessionBeanRemote();
    
    private final String TEST_CASE_ID = "c1234567";
    
    public CustomerActivationTest() {
    }
    
    @Test
    public void test01updateMainAccount() throws UpdateMainAccountException, MainAccountNotExistException {
        System.out.println("CustomerActivationTest.test01updateMainAccount");   
        MainAccount ma = mainBean.getMainAccountByUserId(TEST_CASE_ID);
        ma.setUserID(TEST_CASE_ID);
        MainAccount result = mainBean.updateMainAccount(ma);
        assertEquals(result, ma);  
    }
    
    @Test(expected=UpdateMainAccountException.class)
    public void test02updateMainAccountException() throws UpdateMainAccountException {
        System.out.println("CustomerActivationTest.test02updateMainAccountException");   
        MainAccount ma = new MainAccount();
        MainAccount result = mainBean.updateMainAccount(ma);
        assertEquals(result, ma);     
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    private CustomerActivationSessionBeanRemote lookupCustomerActivationSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CustomerActivationSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CustomerActivationSessionBean!ejb.session.common.CustomerActivationSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private MainAccountSessionBeanRemote lookupMainAccountSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (MainAccountSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/MainAccountSessionBean!ejb.session.mainaccount.MainAccountSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
