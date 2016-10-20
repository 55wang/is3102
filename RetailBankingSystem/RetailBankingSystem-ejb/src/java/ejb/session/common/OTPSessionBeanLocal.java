/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.common.OneTimePassword;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface OTPSessionBeanLocal {
    public void generateOTP(String number);
    public OneTimePassword getOTPByPhoneNumber(String number);
    public void remove(OneTimePassword otp);
}
