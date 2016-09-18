/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.rules.TimeRangeInterest;
import entity.staff.Role;
import entity.staff.StaffAccount;
import java.math.BigDecimal;
import java.util.Date;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import utils.HashPwdUtils;

/**
 *
 * @author leiyang
 */
@Singleton
@LocalBean
@Startup
public class EntityBuilderBean {

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private InterestSessionBeanLocal interestSessionBean;

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
        if (needInit()) {
            buildEntities();
        }// else skip this.
    }

    // Use Super Admin Account as a flag
    private Boolean needInit() {
        String u = "adminadmin";
        String p = HashPwdUtils.hashPwd("password");
        StaffAccount sa = staffAccountSessionBean.loginAccount(u, p);
        if (sa == null) {
            StaffAccount superAccount = new StaffAccount();
            superAccount.setUsername(u);
            superAccount.setPassword(p);
            superAccount.setFirstName("Super");
            superAccount.setLastName("Account");
            Role r = staffRoleSessionBean.getSuperAdminRole();
            superAccount.setRole(r);
            staffAccountSessionBean.createAccount(superAccount);
            return true;
        } else {
            return false;
        }
    }

    private void buildEntities() {
        // TODO: init other entities here
        // TODO: init with an organized flow structure
        // these are just temporary data for emergency use.
        // Yifan pls help edit for me on top of these.
        initCustomer();
        initInterest();
    }
    
    private void initInterest() {
        initTimeRangeInterest();
    }
    
    private void initTimeRangeInterest() {
        // Add to a fixedDepositAccount
        // https://www.bankbazaar.sg/fixed-deposit/ocbc-fixed-deposit-rate.html
        TimeRangeInterest i = new TimeRangeInterest();
        i.setName("1month-2month-$5000-$20000-0.05%");
        i.setDefaultFixedDepositAccount(Boolean.FALSE);
        i.setStartMonth(1);
        i.setEndMonth(2);
        i.setMinimum(new BigDecimal(5000));
        i.setMaximum(new BigDecimal(20000));
        i.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i);
    }
    
    private void initCustomer() {
        String u = "c1234567";
        String p = HashPwdUtils.hashPwd("password");
        
        MainAccount ma = null;
        Customer c = new Customer();
        c.setAddress("some fake address"); //make it a bit more real
        c.setBirthDay(new Date()); //make some real birthday.
        c.setEmail("wangzhe.lynx@gmail.com");
        c.setFirstname("Yifan");
        c.setGender("MALE"); // pls modify gender to enum type
        c.setIdentityNumber("S1234567Z");
        c.setIdentityType("CITIZEN"); // same for this to enum type
        c.setIncome("5000");
        c.setLastname("Chen");
        c.setNationality("Singaporean"); //enum type if possible
        c.setOccupation("programmer");
        c.setPhone("81567758"); //must use real phone number as we need sms code
        c.setPostalCode("654321");
        c.setMainAccount(ma);
        ma = new MainAccount();
        ma.setUserID(u);
        ma.setPassword(p);
        ma.setStatus(MainAccount.StatusType.ACTIVE);
        ma.setCustomer(c);
        
        newCustomerSessionBean.createCustomer(c, ma);
    }

//    private void mark(String mark) {
//        Properties prop = new Properties();
//        OutputStream output = null;
//        try {
//            output = new FileOutputStream("init.properties");
//
//            // set the properties value
//            prop.setProperty("init", mark);
//
//            // save properties to project root folder
//            prop.store(output, null);
//
//        } catch (IOException io) {
//            io.printStackTrace();
//        } finally {
//            if (output != null) {
//                try {
//                    output.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
//
//    private Boolean isMarked() {
//        Properties prop = new Properties();
//        InputStream input = null;
//
//        try {
//            String filePath = "init.properties";
//            File f = new File(filePath);
//            if (f.exists() && !f.isDirectory()) {
//                input = new FileInputStream(filePath);
//                // load a properties file
//                prop.load(input);
//                String init = prop.getProperty("init");
//                // get the property value and print it out
//                System.out.println(init);
//                return init.equals("done");
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                    return true;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return false;
//    }
}
