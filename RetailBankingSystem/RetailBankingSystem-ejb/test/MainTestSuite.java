/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import cms.*;
import common.ChangePasswordTest;
import common.CustomerActivationTest;
import common.LoginTest;
import common.MainAccountTest;
import common.StaffAccountTest;
import common.StaffRoleTest;
import dams.DepositAccountTest;

/**
 *
 * @author VIN-S
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // CMS
    CustomerProfileTest.class, 
    CustomerCaseTest.class,
    // Main
    MainAccountTest.class,
    StaffAccountTest.class,
    StaffRoleTest.class,
    // dams
//    DepositAccountTest.class
        
    ChangePasswordTest.class,
    CustomerActivationTest.class,
    LoginTest.class
})
public class MainTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
