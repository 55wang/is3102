/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.MainAccount;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface EmailServiceSessionBeanLocal {
    public Boolean sendActivationEmailForNewCustomer(String recipient);
    public Boolean sendActivationGmailForNewCustomer(String recipient, String pwd);
    public Boolean sendUserIDforForgottenCustomer(String recipient, MainAccount forgotAccount);
    public Boolean sendResetPwdLinkforForgottenCustomer(String recipient, MainAccount forgotAccount);
    public void sendUpdatedProfile(String recipient);
}
