/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import java.util.Date;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author VIN-S
 */
@Stateless
public class EmailServiceSessionBean implements EmailServiceSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    String emailServerName = "mailauth.comp.nus.edu.sg";
    String mailer = "JavaMailer";

    @Override
    public Boolean sendActivationEmailForNewCustomer(String recipient) {
        String activationCode = "123456";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse("wangzhe@comp.nus.edu.sg", false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
                msg.setSubject("Merlion Bank Account Activation");
                String messageText = "hello";
                msg.setText(messageText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
            return true;
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
            return false;
        }
    }

    @Override
    public Boolean sendActivationGmailForNewCustomer(String recipient) {

        String activationCode = "123456";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication("merlionbanking", "p@ssword1");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Welcome to Merlion Banking");
            message.setText("Dear Customer, Thank you to register merionlion banking.\n You activiation code is " + activationCode);
            message.setText("Link to activate your member account: https://localhost:8181/InternetBankingSystem/common/customer_activate_account.xhtml?email=" + recipient);

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);;
            return (false);
        }

    }

}
