/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import util.exception.common.DuplicateMainAccountExistException;
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author leiyang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainAccountTest implements Serializable {
    
    MainAccountSessionBeanRemote mainBean = lookupMainAccountSessionBeanRemote();
    
    private final String TEST_CASE_ID = "c1234567";
        
    public MainAccountTest() {
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
    public void test01createMainAccount() throws DuplicateMainAccountExistException {
        System.out.println("MainAccountTest.test01createMainAccount");   
        MainAccount ma = new MainAccount();
        ma.setId(TEST_CASE_ID);
        MainAccount result = mainBean.createMainAccount(ma);
        assertNotNull(result);        
    }
    
    @Test(expected=DuplicateMainAccountExistException.class)
    public void test02createMainAccountException() throws DuplicateMainAccountExistException {
        System.out.println("MainAccountTest.test02createMainAccountException"); 
        MainAccount ma = new MainAccount();
        ma.setId(TEST_CASE_ID);
        MainAccount result = mainBean.createMainAccount(ma);
        assertNotNull(result);      
    }
    
    @Test
    public void test03updateCase() throws UpdateMainAccountException, MainAccountNotExistException {
        System.out.println("MainAccountTest.test03updateCase");   
        MainAccount ma = mainBean.getMainAccountByUserId(TEST_CASE_ID);
        ma.setUserID(TEST_CASE_ID);
        MainAccount result = mainBean.updateMainAccount(ma);
        assertEquals(result, ma);
    }
    
    @Test(expected=UpdateMainAccountException.class)
    public void test04updateCaseSaveException() throws UpdateMainAccountException {
        System.out.println("MainAccountTest.test04updateCaseSaveException");   
        MainAccount ma = new MainAccount();
        MainAccount result = mainBean.updateMainAccount(ma);
        assertEquals(result, ma);     
    }
    
    @Test
    public void test05getMainAccountByUserId() throws MainAccountNotExistException {
        System.out.println("MainAccountTest.test09getCustomerByID");
        MainAccount ma = mainBean.getMainAccountByUserId(TEST_CASE_ID);
        assertNotNull(ma);
    }

    @Test(expected = MainAccountNotExistException.class)
    public void test10getCustomerByIDException() throws MainAccountNotExistException {
        System.out.println("MainAccountTest.test10getCustomerByIDException");
        MainAccount ma = mainBean.getMainAccountByUserId(TEST_CASE_ID + "2");
        assertNotNull(ma);
    }
    
        @Test
    public void test11getMainAccountByEmail() throws MainAccountNotExistException {
        System.out.println("MainAccountTest.test11getMainAccountByEmail");   
        MainAccount result = mainBean.getMainAccountByEmail("wangzhe.lynx@gmail.com");
        assertNotNull(result);        
    }
    
    @Test(expected=MainAccountNotExistException.class)
    public void test12getMainAccountByEmailException() throws MainAccountNotExistException {
        System.out.println("MainAccountTest.test12getMainAccountByEmailException");   
        MainAccount result = mainBean.getMainAccountByEmail("customer@merlionbank.com");
        assertNotNull(result);    
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
