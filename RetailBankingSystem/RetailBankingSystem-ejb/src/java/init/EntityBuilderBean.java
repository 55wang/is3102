/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.Role;
import entity.StaffAccount;
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
