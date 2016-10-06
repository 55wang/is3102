/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import is3102.HOTP;

/**
 *
 * @author leiyang
 */
public class OTPUtils {
    
    public static String generateSingleToken(String ccNumber, int pinCode) {
        return HOTP.generateSingleToken(ccNumber, pinCode);
    }
}
