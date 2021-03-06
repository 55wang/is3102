/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.Customer;
import entity.customer.CustomerCase;
import entity.customer.MainAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import entity.staff.StaffAccount;
import java.util.Date;
import java.util.Properties;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;

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

    @Asynchronous
    @Override
    public void sendEmailCreditCardEStatement(String recipient, String subject, String content) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText("Your E-statment is ready at this link: " + content);

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendEmailMarketingCampaign(String recipient, String subject, String content, String landingPage) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);

            message.setText(content + "\n Apply at this link: "+"https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/landing_page/" + landingPage);

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendUpdatePortfolioNotice(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your Portfolio is updated - Merlion Bank");
            message.setText("Dear Customer, \n Your portfolio has been updated.");

            Transport.send(message);

            System.out.println("Email send out successfully");
            return;

        } catch (MessagingException e) {
            System.out.println(e);
            return;
        }
    }

    @Asynchronous
    @Override
    public void sendActivationEmailForCustomer(String recipient) {
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
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }

    @Asynchronous
    @Override
    public void sendActivationGmailForCustomer(String recipient, String pwd) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Welcome to Merlion Banking");
            message.setText("Dear Customer, Thank you to register merionlion banking.\n"
                    + "Link to activate your member account: https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/common/customer_activate_account.xhtml?email=" + recipient + "&code=" + pwd);

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendCreditCardActivationGmailForCustomer(String recipient, String pwd, String ccNumber, String ccv, String userName) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your credit card application is successful");
            message.setText("Dear Customer, Thank you for applying credit card at merionlion banking.\n"
                    + "Your Reward Credit Card No.  " + ccNumber + " is on the way.\n"
                    + "CCV:   " + ccv + " is on the way.\n"
                    + "Your ibanking account is " + userName + " \n"
                    + "Click to activate your member account: https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/common/customer_activate_account.xhtml?email=" + recipient + "&code=" + pwd);

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendCreditCardApplicationRejectionToCustomers(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your credit card application is rejected.");
            message.setText("Dear Customer, Thank you for applying credit card in merionlion banking.\n"
                    + "However, we are sorry to inform that your application is rejected.\n"
                    + "Please contact service center for information. Thank you.");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);

        }

    }

    @Asynchronous
    @Override
    public void sendchargeBackGmailForSuccessfulCustomer(String recipient, String ID) {

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

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendchargeBackGmailForRejectedCustomer(String recipient, String ID) {

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

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendRequireAdditionalInfo(String recipient, String msg) {

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

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendActivationGmailForStaff(String recipient, String pwd) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Welcome to Merlion Banking");
            message.setText("Dear Staff, Thank you to work for merionlion banking.\n"
                    + "Link to activate your staff account: https://" + ConstantUtils.ipAddress + ":8181/StaffInternalSystem/common/staff_activate_account.xhtml?email=" + recipient + "&code=" + pwd);

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendUserIDforForgottenCustomer(String recipient, MainAccount forgotAccount) {
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

        } catch (MessagingException e) {
            System.out.println(e);;
        }
    }

    @Asynchronous
    @Override
    public void sendUserNameforForgottenStaff(String recipient, String username) {
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

        } catch (MessagingException e) {
            System.out.println(e);;
        }
    }

    @Asynchronous
    @Override
    public void sendResetPwdLinkforForgottenCustomer(String recipient, MainAccount forgotAccount) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Reset your password");
            message.setText("Dear Customer, please go to following link to reset your password: \n"
                    + "https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/common/customer_activate_account.xhtml?email=" + recipient + "&code=" + forgotAccount.getPassword());

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendResetPwdLinkforForgottenStaff(String recipient, StaffAccount forgotAccount) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Reset your password");
            message.setText("Dear Staff, please go to following link to reset your password: \n"
                    + "https://" + ConstantUtils.ipAddress + ":8181/StaffInternalSystem/common/staff_activate_account.xhtml?email=" + recipient + "&code=" + forgotAccount.getPassword());

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendNewCaseConfirmationToCustomer(String recipient, CustomerCase cc) {
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

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendCancelCaseConfirmationToCustomer(String recipient, CustomerCase cc) {
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

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
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
                    + "https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/header_user/view_profile.xhtml .");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendCaseStatusChangeToCustomer(String recipient, CustomerCase cc) {
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

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
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
                    + "https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/personal_cards/credit_card_summary.xhtml .");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendLoanApplicationReceivedNotice(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your Loan Application is Approved! - Merlion Bank");
            message.setText("Dear Customer, \n Your Loan Application is Approved! "
                    + "Click the link to view more details "
                    + "https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/main_loan/check_loan_application.xhtml .");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendLoanApplicationApprovalNotice(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your Loan Application is Approved! - Merlion Bank");
            message.setText("Dear Customer, \n Your Loan Application is Approved! "
                    + "Click the link to view more details "
                    + "https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/personal_loan/view_loan_summary.xhtml .");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendLoanApplicationRejectNotice(String recipient) {
        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your Loan Application is Rejected! - Merlion Bank");
            message.setText("Dear Customer, \n We are sorry that your Loan Application is Approved! "
                    + "Please contact our loan officer for more information.");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
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
                    + "https://" + ConstantUtils.ipAddress + ":8181/InternetBankingSystem/main_cards/default.xhtml.");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Asynchronous
    @Override
    public void sendLoanApplicationNoticeToStaff(LoanApplication lp) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("merlionbanking@gmail.com"));
            message.setSubject("New Loan Application Submitted");
            message.setText("Hi, there is a new loan application.\n"
                    + "  Applicant Name: " + lp.getFullName() + "\n"
                    + "  Applicant Income: " + lp.getActualIncome() + "\n"
                    + "  Product Type: " + lp.getProductType() + "\n"
                    + "  Product Name: " + lp.getLoanProduct().getProductName() + "\n"
                    + "  Requested Amount: " + lp.getRequestedAmount() + "\n"
                    + "View more details at: https://" + ConstantUtils.ipAddress + ":8181/StaffInternalSystem/loan/view_loan_application.xhtml");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendLoanApplicationNoticeToCustomer(String recipient) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your loan application is submitted - Merlion Bank");
            message.setText("Dear Customer, \n Your loan application is submitted.\n"
                    + "We are processing your application and will reply to you soon. Thank you.\n"
                    + "View more details at: https://" + ConstantUtils.ipAddress + ":8181/StaffInternalSystem/loan/view_loan_application.xhtml");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendPaymentReminderEmailToCustomer(String recipient, String loanAccountNumber, Date paymentDate) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your loan repayment is due soon - Merlion Bank");
            message.setText("Dear Customer, \n Your loan repayment is due on " + DateUtils.normalDisplayDate(paymentDate) + "\n"
                    + "Please be informed that addtional charge applies after due date. Thank you.");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendLatePaymentReminderEmailToCustomer(String recipient, String loanAccountNumber, Date paymentDate) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Your loan repayment is due already - Merlion Bank");
            message.setText("Dear Customer, \n Your loan repayment is due already on " + DateUtils.normalDisplayDate(paymentDate) + "\n"
                    + "Please be informed that addtional charge applies after due date. Thank you.");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendBadLoanNoticeToLoanOfficer(LoanAccount loanAccount) {

        Session session = getSession();
        Customer customer = loanAccount.getMainAccount().getCustomer();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("merlionbanking@gmail.com"));
            message.setSubject("Bad Loan Notice");
            message.setText("Hi, please notice that there is a bad loan for this customer:\n"
                    + "  Name: " + customer.getFullName() + "\n"
                    + "  Phone: " + customer.getPhone() + "\n"
                    + "  Email: " + customer.getEmail() + "\n"
                    + "  Loan Account Number: " + loanAccount.getAccountNumber() + "\n"
                    + "  Loan Amount: " + loanAccount.getPrincipal() + "\n"
                    + "Please contact the customer ASAP");

            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
        }

    }

    @Asynchronous
    @Override
    public void sendEmailUnauthorised(String recipient, String msg) {

        Session session = getSession();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("merlionbanking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Unauthorise Transaction");
            message.setText(msg);
            Transport.send(message);

            System.out.println("Email send out successfully");

        } catch (MessagingException e) {
            System.out.println(e);
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
