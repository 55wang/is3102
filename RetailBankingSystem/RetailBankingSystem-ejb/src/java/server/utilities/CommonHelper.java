/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

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
            String [] parts = full.split(ConstantUtils.DELIMITER);
            String result = "";
            if (parts.length < 2 ) {
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
            String [] parts = full.split(ConstantUtils.DELIMITER);
            for (String p : parts) {
                if (p.equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }
}
