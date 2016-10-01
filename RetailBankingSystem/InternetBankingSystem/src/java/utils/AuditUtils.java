/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entity.common.AuditLog;
import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
public class AuditUtils implements Serializable {

    public static AuditLog createAuditLog(String activityLog, String functionName,
            List<String> input, String output, MainAccount ma, StaffAccount sa) {

        Date date = new Date();

        AuditLog al = new AuditLog();
        al.setCreationDate(date);
        al.setActivityLog(activityLog);
        al.setFunctionName(functionName);
        al.setInput(input.toString());
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
    public static String hiddenFourString(String text) {
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

    //hide all string only show ********
    public static String hiddenFullString(String text) {
        return "********";
    }

}
