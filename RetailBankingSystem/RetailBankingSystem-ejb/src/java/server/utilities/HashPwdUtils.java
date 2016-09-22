/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author leiyang
 */
public class HashPwdUtils {

    public static String hashPwd(String pwd) {
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return pwd;
        }
    }
}
