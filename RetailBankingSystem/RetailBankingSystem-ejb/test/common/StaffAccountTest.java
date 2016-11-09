/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

//import ejb.session.staff.StaffRoleSessionBeanRemote;
import ejb.session.staff.StaffAccountSessionBeanRemote;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import util.exception.common.DuplicateStaffAccountException;
import util.exception.common.NoStaffAccountsExistException;
import util.exception.common.StaffAccountNotExistException;
import util.exception.common.UpdateStaffAccountException;

/**
 *
 * @author Lei Yang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaffAccountTest implements Serializable {
    
    StaffAccountSessionBeanRemote staffBean = lookupStaffAccountSessionBeanRemote();
    
    private final String TEST_CASE_ID = "c1234567";
    
    
    public StaffAccountTest() {
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
    
    // TODO:
    @Test
    public void test01saveStaffAccount() throws DuplicateStaffAccountException {
        System.out.println("StaffAccountTest.test01saveStaffAccount");   
        StaffAccount testCase = new StaffAccount();
        testCase.setUsername(TEST_CASE_ID);
        testCase.setEmail("lei_flash@hotmail.com");
        StaffAccount result = staffBean.createAccount(testCase);
        assertNotNull(result);        
    }
    
    @Test(expected=DuplicateStaffAccountException.class)
    public void test02saveStaffAccountException() throws DuplicateStaffAccountException {
        System.out.println("StaffAccountTest.test02saveStaffAccountException");  
        StaffAccount testCase = new StaffAccount();
        testCase.setUsername(TEST_CASE_ID);
        StaffAccount result = staffBean.createAccount(testCase);
        assertNotNull(result);           
    }
    
    @Test
    public void test03updateStaffAccount() throws UpdateStaffAccountException, StaffAccountNotExistException {
        System.out.println("StaffAccountTest.test03updateStaffAccount");   
        StaffAccount testCase = staffBean.getStaffById(TEST_CASE_ID);
        testCase.setEmail("raymondlei90s@gmail.com");
        StaffAccount result = staffBean.updateAccount(testCase);
        assertEquals(result, testCase);
    }
    
    @Test(expected=UpdateStaffAccountException.class)
    public void test04updateStaffAccountSaveException() throws UpdateStaffAccountException {
        System.out.println("StaffAccountTest.test04updateStaffAccountSaveException");   
        StaffAccount testCase = new StaffAccount();
        StaffAccount result = staffBean.updateAccount(testCase);
        assertEquals(result, testCase);    
    }
    
    @Test
    public void test05searchStaffByID() throws StaffAccountNotExistException {
        System.out.println("StaffAccountTest.test05searchStaffByID");
        StaffAccount testCase = staffBean.getStaffById(TEST_CASE_ID);
        assertNotNull(testCase);
    }
    
    @Test(expected=StaffAccountNotExistException.class)
    public void test06searchStaffByIDException() throws StaffAccountNotExistException {
        System.out.println("StaffAccountTest.test06searchStaffByIDException");
        StaffAccount testCase = staffBean.getStaffById(TEST_CASE_ID + "2");
        assertNotNull(testCase);
    }
    
    @Test
    public void test07getAllStaffAccount() throws NoStaffAccountsExistException {
        System.out.println("StaffAccountTest.test07getAllStaffAccount");
        List<StaffAccount> result = staffBean.getAllStaffs();
        assertNotNull(result);
    }
    
//    @Test(expected=NoStaffAccountsExistException.class)
//    public void test08getAllStaffAccountException() throws NoStaffAccountsExistException {
//        System.out.println("StaffAccountTest.test08getAllStaffAccountException");  
//        
//        List<StaffAccount> result = staffBean.getAllStaffs();
//        
//        for (StaffAccount r : result) {
//            staffBean.removeStaffAccount(r.getUsername());
//        }
//        
//        result = staffBean.getAllStaffs();
//        
//        assertNotNull(result);    
//    }
    
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
