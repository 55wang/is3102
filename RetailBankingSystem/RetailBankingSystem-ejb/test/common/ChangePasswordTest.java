/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import ejb.session.common.ChangePasswordSessionBean;
import ejb.session.common.ChangePasswordSessionBeanRemote;
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
import util.exception.common.DuplicateMainAccountExistException;
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author VIN-S
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChangePasswordTest implements Serializable {
    ChangePasswordSessionBeanRemote changePwdBean = lookupChangePasswordSessionBeanRemote();
    
    MainAccountSessionBeanRemote mainBean = lookupMainAccountSessionBeanRemote();
    
    private final String TEST_CASE_ID = "c1234567";
    
    public ChangePasswordTest() {
    }
    
    @Test
    public void test01ChangeMainAccountPwd() throws UpdateMainAccountException, MainAccountNotExistException {
        System.out.println("ChangePasswordTest.test01ChangeMainAccountPwd");   
        MainAccount ma = mainBean.getMainAccountByUserId(TEST_CASE_ID);
        ma.setUserID(TEST_CASE_ID);
        MainAccount result = changePwdBean.changeMainAccountPwd(ma);
        assertEquals(result, ma);
    }
    
    @Test(expected=UpdateMainAccountException.class)
    public void test01ChangeMainAccountPwdException() throws UpdateMainAccountException {
        System.out.println("ChangePasswordTest.test01ChangeMainAccountPwdException");   
        MainAccount ma = new MainAccount();
        MainAccount result = changePwdBean.changeMainAccountPwd(ma);
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
    private ChangePasswordSessionBeanRemote lookupChangePasswordSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ChangePasswordSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/ChangePasswordSessionBean!ejb.session.common.ChangePasswordSessionBeanRemote");
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
