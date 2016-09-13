/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leiyang
 */
public class LoggingUtil {
    
    private static final Boolean logStaffMessage = true;
    
    public static void StaffMessageLog(Class c, String m) {
        if (logStaffMessage) {
            Logger.getLogger(c.getName()).log(Level.INFO, m);
        }
    }
}
