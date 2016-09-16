/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import ejb.session.audit.AuditSessionBeanLocal;
import entity.AuditLog;
import entity.MainAccount;
import entity.StaffAccount;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;

/**
 *
 * @author wang
 */
public class AuditUtils implements Serializable {

    public static AuditLog createAuditLog(String activityLog, String functionName,
            String input, String output, MainAccount ma, StaffAccount sa) {

        Date date = new Date();

        AuditLog al = new AuditLog();
        al.setCreationDate(date);
        al.setActivityLog(activityLog);
        al.setFunctionName(functionName);
        al.setInput(input);
        al.setOutput(output);
        String ip = SessionUtils.getIpAddress();
        al.setIpAddress(ip);

        if (ma != null) {
            al.setMainAccount(ma);
        } else if (sa != null) {
            al.setStaffAccount(sa);
        } else {
            return al;
        }

        return al;
    }

    //only show first 4 character
    public static String hiddenString(String text) {
        String output;
        if (text.length() >= 4) {
            output = text.substring(0, 4);
            output = output + "****";
        } else {
            output = text.substring(0);
            while (output.length() < 8) {
                output = output + "*";
            }
        }
        return output;
    }

}
