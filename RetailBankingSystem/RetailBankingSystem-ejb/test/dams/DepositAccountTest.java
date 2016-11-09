/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dams;

import ejb.session.dams.CustomerDepositSessionBeanRemote;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
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
import util.exception.dams.DepositAccountNotFoundException;
import util.exception.dams.DuplicateDepositAccountException;
import util.exception.dams.UpdateDepositAccountException;

/**
 *
 * @author leiyang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepositAccountTest implements Serializable {
    
    CustomerDepositSessionBeanRemote depositBean = lookupDepositAccountSessionBeanRemote();
    
    private final String TEST_CASE_ID = "c1234567";
        
    public DepositAccountTest() {
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
    public void test01createDepositAccount() throws DuplicateDepositAccountException {
        System.out.println("DepositAccountTest.test01createDepositAccount");   
        DepositAccount da = new CustomerDepositAccount();
        da.setAccountNumber(TEST_CASE_ID);
        DepositAccount result = depositBean.createAccount(da);
        assertNotNull(result);        
    }
    
    @Test(expected=DuplicateDepositAccountException.class)
    public void test02createDepositAccountException() throws DuplicateDepositAccountException {
        System.out.println("DepositAccountTest.test02createDepositAccountException"); 
        DepositAccount da = new CustomerDepositAccount();
        da.setAccountNumber(TEST_CASE_ID);
        DepositAccount result = depositBean.createAccount(da);
        assertNotNull(result);   
    }
    
    @Test
    public void test03updateDepositAccount() throws UpdateDepositAccountException, DepositAccountNotFoundException {
        System.out.println("DepositAccountTest.test03updateDepositAccount");   
        DepositAccount da = depositBean.getAccountFromId(TEST_CASE_ID);
        da.setBalance(new BigDecimal(567));
        DepositAccount result = depositBean.updateAccount(da);
        assertEquals(result, da);
    }
    
    @Test(expected=UpdateDepositAccountException.class)
    public void test04updateDepositAccountException() throws UpdateDepositAccountException {
        System.out.println("DepositAccountTest.test03updateDepositAccount");   
        DepositAccount da = new CustomerDepositAccount();
        DepositAccount result = depositBean.updateAccount(da);
        assertEquals(result, da);      
    }
    
    @Test
    public void test05getDepositAccountByAccountNumber() throws DepositAccountNotFoundException {
        System.out.println("DepositAccountTest.test05getDepositAccountByAccountNumber");
        DepositAccount da = depositBean.getAccountFromId(TEST_CASE_ID);
        assertNotNull(da);
    }
    
    @Test(expected=DepositAccountNotFoundException.class)
    public void test06getDepositAccountByAccountNumberException() throws DepositAccountNotFoundException {
        System.out.println("DepositAccountTest.test06getDepositAccountByAccountNumberException");   
        DepositAccount da = depositBean.getAccountFromId("2");
        assertNotNull(da); 
    }
    
    
    private CustomerDepositSessionBeanRemote lookupDepositAccountSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CustomerDepositSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CustomerDepositSessionBean!ejb.session.dams.CustomerDepositSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
