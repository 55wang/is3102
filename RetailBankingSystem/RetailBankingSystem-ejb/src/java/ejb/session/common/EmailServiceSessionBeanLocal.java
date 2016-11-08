/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.CustomerCase;
import entity.customer.MainAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import entity.staff.StaffAccount;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface EmailServiceSessionBeanLocal {
    
    public void sendEmailMarketingCampaign(String recipient, String subject, String content, String landingPage);
    public void sendCreditCardActivationGmailForCustomer(String recipient, String pwd, String ccNumber, String ccv, String userName);
    public void sendCreditCardApplicationRejectionToCustomers(String recipient);
    public void sendActivationEmailForCustomer(String recipient);
    public void sendActivationGmailForCustomer(String recipient, String pwd);

    public void sendUpdatePortfolioNotice(String recipient);
    public void sendUserIDforForgottenCustomer(String recipient, MainAccount forgotAccount);
    public void sendResetPwdLinkforForgottenCustomer(String recipient, MainAccount forgotAccount);
    public void sendchargeBackGmailForSuccessfulCustomer(String recipient, Long ID);
    public void sendchargeBackGmailForRejectedCustomer(String recipient, Long ID);
    public void sendActivationGmailForStaff(String recipient, String pwd);
    public void sendRequireAdditionalInfo(String recipient, String msg);
    public void sendUserNameforForgottenStaff(String recipient, String username);
    public void sendResetPwdLinkforForgottenStaff(String recipient, StaffAccount forgotAccount);
    public void sendNewCaseConfirmationToCustomer(String recipient, CustomerCase cc);
    public void sendCancelCaseConfirmationToCustomer(String recipient, CustomerCase cc);
    public void sendCaseStatusChangeToCustomer(String recipient, CustomerCase cc);
    public void sendUpdatedProfile(String recipient);
    public void sendTransactionLimitChangeNotice(String recipient);
    public void sendLoanApplicationReceivedNotice(String recipient);
    public void sendLoanApplicationApprovalNotice(String recipient);
    public void sendLoanApplicationRejectNotice(String recipient);
    public void sendCreditCardApplicationNotice(String recipient);
    public void sendLoanApplicationNoticeToStaff(LoanApplication lp);
    public void sendLoanApplicationNoticeToCustomer(String recipient);
    public void sendPaymentReminderEmailToCustomer(String recipient, String loanAccountNumber, Date paymentDate);
    public void sendLatePaymentReminderEmailToCustomer(String recipient, String loanAccountNumber, Date paymentDate);
    public void sendBadLoanNoticeToLoanOfficer(LoanAccount loanAccount);
    public void sendEmailUnauthorised(String phoneNumber, String msg);
}
