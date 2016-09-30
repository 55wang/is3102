/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.security.SecureRandom;

/**
 *
 * @author leiyang
 */
public class CommonHelper {

    public static String delimAdd(String full, String s) {
        if (full == null || full.isEmpty()) {
            return s;
        } else {
            return full + ConstantUtils.DELIMITER + s;
        }
    }

    public static String delimRemove(String full, String s) {
        if (full.equals(s)) {
            return "";
        } else {
            Integer counter = 0;
            String[] parts = full.split(ConstantUtils.DELIMITER);
            String result = "";
            if (parts.length < 2) {
                // not found
                return full;
            }
            for (String p : parts) {
                if (p.equals(s)) {
                    continue;
                }
                if (counter > 0) {
                    result += ConstantUtils.DELIMITER + p;
                } else {
                    result = p;
                }
            }
            return result;
        }
    }

    public static Boolean delimContains(String full, String s) {
        if (full == null || full.isEmpty()) {
            return false;
        } else {
            String[] parts = full.split(ConstantUtils.DELIMITER);
            for (String p : parts) {
                if (p.equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }

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

    public static String generateUserID(EnumUtils.IdentityType identityType, String identityNum) {

        if (identityType.equals(EnumUtils.IdentityType.NRIC)) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else if (identityType.equals(EnumUtils.IdentityType.PASSPORT)) {
            return "c" + identityNum.substring(1);
        } else {
            return "error";
        }
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
