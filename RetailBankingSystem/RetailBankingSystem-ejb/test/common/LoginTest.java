/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import ejb.session.common.LoginSessionBeanRemote;
import entity.customer.Customer;
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
import server.utilities.HashPwdUtils;
import util.exception.cms.CustomerNotExistException;
import util.exception.common.LoginFailException;
import util.exception.common.MainAccountNotExistException;

/**
 *
 * @author VIN-S
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest implements Serializable{
    LoginSessionBeanRemote loginSessionBean = lookupLoginSessionBeanRemote();
       
    public LoginTest() {
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
    public void test01LoginAccount() throws LoginFailException {
        System.out.println("LoginTest.test01LoginAccount");   
        MainAccount result = loginSessionBean.loginAccount("c1234567", HashPwdUtils.hashPwd("password"));
        assertNotNull(result);        
    }
    
    @Test(expected=LoginFailException.class)
    public void test02LoginAccountException() throws LoginFailException {
        System.out.println("LoginTest.test02LoginAccountException"); 
        MainAccount result = loginSessionBean.loginAccount("c123456", HashPwdUtils.hashPwd("passwor"));
        assertNotNull(result);            
    }
    
    @Test
    public void test03getCustomerByUserID() throws CustomerNotExistException {
        System.out.println("LoginTest.test03getCustomerByUserID");   
        Customer result = loginSessionBean.getCustomerByUserID("c1234567");
        assertNotNull(result);        
    }
    
    @Test(expected=CustomerNotExistException.class)
    public void test04getCustomerByUserIDException() throws CustomerNotExistException {
        System.out.println("LoginTest.test04getCustomerByUserIDException");   
        Customer result = loginSessionBean.getCustomerByUserID("c123456789");
        assertNotNull(result);
    }
    
    @Test
    public void test05getMainAccountByUserIC() throws MainAccountNotExistException {
        System.out.println("LoginTest.test05getMainAccountByUserIC");   
        MainAccount result = loginSessionBean.getMainAccountByUserIC("S1234567Z");
        assertNotNull(result);        
    }
    
    @Test(expected=MainAccountNotExistException.class)
    public void test06getMainAccountByUserICException() throws MainAccountNotExistException {
        System.out.println("LoginTest.test06getMainAccountByUserICException"); 
        MainAccount result = loginSessionBean.getMainAccountByUserIC("S123456789Z");
        assertNotNull(result);       
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    private LoginSessionBeanRemote lookupLoginSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (LoginSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/LoginSessionBean!ejb.session.common.LoginSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
