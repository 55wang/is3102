/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cms;

import ejb.session.cms.CustomerCaseSessionBeanRemote;
import entity.customer.Customer;
import entity.customer.CustomerCase;
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

/**
 *
 * @author VIN-S
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class CustomerCaseTest implements Serializable{
    CustomerCaseSessionBeanRemote customerCaseSessionBean = lookupCustomerCaseSessionBeanRemote();
    
    public CustomerCaseTest() {
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
    public void test01saveCase() {
        System.out.println("CustomerCaseTest.test01saveCase");   
        CustomerCase testCase = new CustomerCase();
        Boolean result = customerCaseSessionBean.saveCase(testCase);
        assertNotNull(result);        
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    private CustomerCaseSessionBeanRemote lookupCustomerCaseSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CustomerCaseSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CustomerCaseSessionBean!ejb.session.cms.CustomerCaseSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
