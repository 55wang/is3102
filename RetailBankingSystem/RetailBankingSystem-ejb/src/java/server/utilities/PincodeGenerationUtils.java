/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.security.SecureRandom;

/**
 *
 * @author wang
 */
public class PincodeGenerationUtils {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String num = "0123456789";

    private static SecureRandom rnd = new SecureRandom();

    public static String generateRandom(boolean numerical, int len) {
        StringBuilder sb = new StringBuilder(len);
        String temp;

        if (numerical == true) {
            temp = num;
        } else {
            temp = AB;
        }

        for (int i = 0; i < len; i++) {
            sb.append(temp.charAt(rnd.nextInt(temp.length())));
        }
        return sb.toString();
    }

    public static String generatePwd() {
        int pwdLen = 10;
        SecureRandom rnd = new SecureRandom();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(pwdLen);
        for (int i = 0; i < pwdLen; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

}
