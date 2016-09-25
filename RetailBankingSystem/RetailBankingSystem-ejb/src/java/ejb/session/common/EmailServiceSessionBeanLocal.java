/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.customer.CustomerCase;
import entity.customer.MainAccount;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface EmailServiceSessionBeanLocal {
    public Boolean sendActivationEmailForCustomer(String recipient);
    public Boolean sendActivationGmailForCustomer(String recipient, String pwd);
    public Boolean sendCreditCardActivationGmailForCustomer(String recipient, String pwd, String ccNumber);
    
    public Boolean sendUserIDforForgottenCustomer(String recipient, MainAccount forgotAccount);
    public Boolean sendResetPwdLinkforForgottenCustomer(String recipient, MainAccount forgotAccount);
    public Boolean sendActivationGmailForStaff(String recipient, String pwd);
    public Boolean sendRequireAdditionalInfo(String recipient, String msg);
    public Boolean sendUserNameforForgottenStaff(String recipient, String username);
    public Boolean sendResetPwdLinkforForgottenStaff(String recipient);
    public Boolean sendNewCaseConfirmationToCustomer(String recipient, CustomerCase cc);
    public Boolean sendCancelCaseConfirmationToCustomer(String recipient, CustomerCase cc);
    public Boolean sendCaseStatusChangeToCustomer(String recipient, CustomerCase cc);
    public void sendUpdatedProfile(String recipient);
}
