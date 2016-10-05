/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import entity.common.OneTimePassword;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import server.utilities.PincodeGenerationUtils;

/**
 *
 * @author leiyang
 */
@Stateless
public class OTPSessionBean implements OTPSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void generateOTP(String number) {
        String code = PincodeGenerationUtils.generateRandom(true, 6);
        System.out.println("OTP code is: " + code);
        OneTimePassword otp = em.find(OneTimePassword.class, number);
        if (otp != null) {
            // remove previous cache
            System.out.println("Removing otp");
            em.remove(otp);
            em.flush();
        }
        otp = new OneTimePassword();
        otp.setMobileNumber(number);
        otp.setPassword(code);
        em.persist(otp);
        em.flush();
    }
    
    @Override
    public OneTimePassword getOTPByPhoneNumber(String number) {
        return em.find(OneTimePassword.class, number);
    }
    
    @Override
    public void remove(OneTimePassword otp) {
        OneTimePassword toBeRemoved = em.merge(otp);
        em.remove(toBeRemoved);
    }
}
