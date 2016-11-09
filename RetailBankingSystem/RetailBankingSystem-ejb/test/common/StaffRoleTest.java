/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import ejb.session.staff.StaffAccountSessionBeanRemote;
import ejb.session.staff.StaffRoleSessionBeanRemote;
import entity.staff.Role;
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
import util.exception.common.DuplicateStaffRoleException;
import util.exception.common.NoRolesExistException;
import util.exception.common.NoStaffAccountsExistException;
import util.exception.common.StaffAccountNotExistException;
import util.exception.common.StaffRoleNotFoundException;
import util.exception.common.UpdateStaffAccountException;
import util.exception.common.UpdateStaffRoleException;

/**
 *
 * @author leiyang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaffRoleTest implements Serializable {
    
    StaffRoleSessionBeanRemote roleBean = lookupStaffRoleSessionBeanRemote();
    
    private final String TEST_CASE_ID = "ceo";
    
    
    public StaffRoleTest() {
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
    public void test01saveStaffRole() throws DuplicateStaffRoleException {
        System.out.println("StaffRoleTest.test01saveStaffRole");   
        Role testCase = new Role();
        testCase.setRoleName(TEST_CASE_ID);
        testCase.setDescription("des1");
        Role result = roleBean.createRole(testCase);
        assertNotNull(result);        
    }
    
    @Test(expected=DuplicateStaffRoleException.class)
    public void test02saveStaffAccountException() throws DuplicateStaffRoleException {
        System.out.println("StaffAccountTest.test02saveStaffAccountException");  
        Role testCase = new Role();
        testCase.setRoleName(TEST_CASE_ID);
        Role result = roleBean.createRole(testCase);
        assertNotNull(result);           
    }
    
    @Test
    public void test03updateStaffRole() throws UpdateStaffRoleException, StaffRoleNotFoundException {
        System.out.println("StaffRoleTest.test03updateStaffRole");   
        Role testCase = roleBean.getRoleByName(TEST_CASE_ID);
        testCase.setDescription("des2");
        Role result = roleBean.updateRole(testCase);
        assertEquals(result, testCase);
    }
    
    @Test(expected=UpdateStaffRoleException.class)
    public void test04updateStaffRoleSaveException() throws UpdateStaffRoleException {
        System.out.println("StaffRoleTest.test04updateStaffRoleSaveException");   
        Role testCase = new Role();
        Role result = roleBean.updateRole(testCase);
        assertEquals(result, testCase);  
    }
    
    @Test
    public void test05searchStaffRoleByID() throws StaffRoleNotFoundException {
        System.out.println("StaffRoleTest.test05searchStaffRoleByID");
        Role testCase = roleBean.getRoleByName(TEST_CASE_ID);
        assertNotNull(testCase);
    }
    
    @Test(expected=StaffRoleNotFoundException.class)
    public void test06searchStaffByIDException() throws StaffRoleNotFoundException {
        System.out.println("StaffRoleTest.test06searchStaffByIDException");
        Role testCase = roleBean.getRoleByName(TEST_CASE_ID + "2");
        assertNotNull(testCase);
    }
    
    @Test
    public void test07getAllStaffRole() throws NoRolesExistException {
        System.out.println("StaffRoleTest.test07getAllStaffRole");
        List<Role> result = roleBean.getAllRoles();
        assertNotNull(result);
    }
    
//    @Test(expected=NoRolesExistException.class)
//    public void test08getAllStaffRoleException() throws NoRolesExistException {
//        System.out.println("StaffRoleTest.test08getAllStaffRoleException");  
//        
//        List<Role> result = roleBean.getAllRoles();
//        
//        for (Role r : result) {
//            roleBean.removeRole(r.getRoleName());
//        }
//        
//        result = roleBean.getAllRoles();
//        
//        assertNotNull(result);    
//    }
    
    private StaffRoleSessionBeanRemote lookupStaffRoleSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StaffRoleSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/StaffRoleSessionBean!ejb.session.staff.StaffRoleSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
