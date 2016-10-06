/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

//import ejb.session.staff.StaffRoleSessionBeanRemote;
import entity.staff.Role;
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
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import server.utilities.EnumUtils;

/**
 *
 * @author VIN-S
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class EntityBuilderInitStaffAndRoleTest implements Serializable {
//    StaffRoleSessionBeanRemote staffRoleSessionBean = lookupStaffRoleSessionBeanRemote();

    
    
    public EntityBuilderInitStaffAndRoleTest() {
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
    /*
    
    @Test
    public void test01GetRole01() {
        System.out.println("EntityBuilderStaffAndRoleTest.test01GetRole01 - Role01");
        String roleName = EnumUtils.UserRole.SUPER_ADMIN.toString();        
        Role expResult = new Role(EnumUtils.UserRole.SUPER_ADMIN.toString());
        Role result = staffRoleSessionBean.findRoleByName(roleName);
        assertEquals(expResult, result);        
    } 
*/
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    /*
    private StaffRoleSessionBeanRemote lookupStaffRoleSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StaffRoleSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/StaffRoleSessionBean!ejb.session.staff.StaffRoleSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    */
}
