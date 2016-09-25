/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.CustomerCase;
import entity.customer.MainAccount;
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
    public Boolean sendActivationEmailForCustomer(String recipient) {
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
    public Boolean sendActivationGmailForCustomer(String recipient, String pwd) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Welcome to Merlion Banking");
            message.setText("Dear Customer, Thank you to register merionlion banking.\n"
                    + "Link to activate your member account: https://localhost:8181/InternetBankingSystem/common/customer_activate_account.xhtml?email=" + recipient + "&code=" + pwd);

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }

    }

    @Override
    public Boolean sendCreditCardActivationGmailForCustomer(String recipient, String pwd, String ccNumber) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Welcome to Merlion Banking");
            message.setText("Dear Customer, Thank you to register merionlion banking.\n"
                    + "Your Reward Credit Card xxxx-xxxx-xxxx-"+ccNumber.substring(ccNumber.length() - 4, ccNumber.length() )+" is on the way.\n"
                    + "Click to activate your member account: https://localhost:8181/InternetBankingSystem/common/customer_activate_account.xhtml?email=" + recipient + "&code=" + pwd);

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }

    }
    
    @Override
    public Boolean sendchargeBackGmailForSuccessfulCustomer(String recipient, Long ID) {
        
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Chargeback notification");
            message.setText("Dear Customer, Thank you to transact with merionlion banking.\n"
                    + "Your chargebank request is successful");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }
        
    }
    
    @Override
    public Boolean sendchargeBackGmailForRejectedCustomer(String recipient, Long ID) {
        
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Chargeback notification");
            message.setText("Dear Customer, Thank you to transact with merionlion banking.\n"
                    + "We are sorry to inform you that your chargebank request is rejected.\n");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }
        
    }
    
    

    @Override
    public Boolean sendRequireAdditionalInfo(String recipient, String msg) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Welcome to Merlion Banking");
            message.setText("Dear Customer, \n" + msg);

            Transport.send(message);

            System.out.println("sendRequireAdditionalInfo: Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }

    }

    @Override
    public Boolean sendActivationGmailForStaff(String recipient, String pwd) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Welcome to Merlion Banking");
            message.setText("Dear Staff, Thank you to work for merionlion banking.\n"
                    + "Link to activate your staff account: https://localhost:8181/StaffInternalSystem/common/staff_activate_account.xhtml?email=" + recipient + "&code=" + pwd);

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }

    }

    @Override
    public Boolean sendUserIDforForgottenCustomer(String recipient, MainAccount forgotAccount) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Please check your User ID");
            message.setText("Dear Customer, your User ID is: " + forgotAccount.getUserID());

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);;
            return (false);
        }
    }

    @Override
    public Boolean sendUserNameforForgottenStaff(String recipient, String username) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Please check your User Name");
            message.setText("Dear Customer, your User Name is: " + username);

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);;
            return (false);
        }
    }

    @Override
    public Boolean sendResetPwdLinkforForgottenCustomer(String recipient, MainAccount forgotAccount) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Reset your password");
            message.setText("Dear Customer, please go to following link to reset your password: ");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }
    }

    @Override
    public Boolean sendResetPwdLinkforForgottenStaff(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Reset your password");
            message.setText("Dear Staff, please go to following link to reset your password: ");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }
    }

    @Override
    public Boolean sendNewCaseConfirmationToCustomer(String recipient, CustomerCase cc) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("You have submitted a new case");
            message.setText("Dear Customer, here is your case ID(which you can use to retrieve your case): "
                    + "Case: " + cc.getTitle() + "ID: " + cc.getId());

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }
    }

    @Override
    public Boolean sendCancelCaseConfirmationToCustomer(String recipient, CustomerCase cc) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("You have cancelled a case");
            message.setText("Dear Customer, \nyou have cancelled the following case: "
                    + "Case: " + cc.getTitle() + "ID: " + cc.getId() + "If the action is not done by you, please contact our staff.");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return (true);

        } catch (MessagingException e) {
            System.out.println(e);
            return (false);
        }
    }

    @Override
    public void sendUpdatedProfile(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your profile is updated - Merlion Bank");
            message.setText("Dear Customer, \n Your profile has been updated. "
                    + "Click the link to check updated profile: "
                    + "https://localhost:8181/InternetBankingSystem/customer_cms/view_profile.xhtml .");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return;

        } catch (MessagingException e) {
            System.out.println(e);
            return;
        }
    }

    public Boolean sendCaseStatusChangeToCustomer(String recipient, CustomerCase cc) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("The status of your case(ID: " + cc.getId() + ") has been changed");
            message.setText("Dear Customer, \n The staff has updated your case status to "
                    + cc.getCaseStatus()
                    + "\nIf there is anything wrong, please check with our staff.");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return true;

        } catch (MessagingException e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public void sendTransactionLimitChangeNotice(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your transaction limit is updated - Merlion Bank");
            message.setText("Dear Customer, \n Your transaction limit of MBS card has been updated. "
                    + "Click the link to check updated transaction limit: "
                    + "https://localhost:8181/InternetBankingSystem/customer_card/credit_card_summary.xhtml .");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return;

        } catch (MessagingException e) {
            System.out.println(e);
            return;
        }
    }

    @Override
    public void sendCreditCardApplicationNotice(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your credit card application is submitted - Merlion Bank");
            message.setText("Dear Customer, \n Your credit card application is submitted."
                    + "We are processing your application. Meanwhile, you can go to this link to check your application status."
                    + "https://localhost:8181/InternetBankingSystem/customer_card/credit_card_summary.xhtml .");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return;

        } catch (MessagingException e) {
            System.out.println(e);
            return;
        }
    }

    private Properties getGmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        return props;
    }

    private Session getSession() {
        return Session.getInstance(getGmailProperties(),
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication("merlionbanking", "p@ssword1");
                    }
                });
    }


}
