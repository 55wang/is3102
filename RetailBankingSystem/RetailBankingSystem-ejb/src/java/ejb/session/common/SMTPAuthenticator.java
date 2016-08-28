/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;

/**
 *
 * @author yang
 */
public class SMTPAuthenticator extends javax.mail.Authenticator {
    // Replace with your actual unix id

//    private static final String SMTP_AUTH_USER = "A0097787";
// Replace with your actual unix password
//    private static final String SMTP_AUTH_PWD = "opkl3255";
    private static String SMTP_AUTH_USER;
    private static String SMTP_AUTH_PWD;

    public SMTPAuthenticator() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("/Users/wang/is3102/RetailBankingSystem/RetailBankingSystem-ejb/src/java/ejb/session/common/config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("SMTP_AUTH_USER"));
            System.out.println(prop.getProperty("SMTP_AUTH_PWD"));
            SMTP_AUTH_USER = prop.getProperty("SMTP_AUTH_USER");
            SMTP_AUTH_PWD = prop.getProperty("SMTP_AUTH_PWD");
            

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        System.out.println("Password authentication() called:");
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }

}
