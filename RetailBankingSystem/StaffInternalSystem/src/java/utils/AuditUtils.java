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

/**
 *
 * @author leiyang
 */
public class AuditUtils implements Serializable {

    public static AuditLog createAuditLog(String activityLog, String functionName,
            List<String> input, String output, MainAccount ma, StaffAccount sa) {

        Date date = new Date();
        System.out.println("Creating AuditLog");
        AuditLog al = new AuditLog();
        System.out.println("Input Date is:" + date.toString());
        al.setCreationDate(date);
        System.out.println("activityLog is:" + activityLog);
        al.setActivityLog(activityLog);
        System.out.println("functionName is:" + functionName);
        al.setFunctionName(functionName);
        System.out.println("Input is:" + input.toString());
        al.setInput(input.toString());
        System.out.println("output is:" + output);
        al.setOutput(output);
        String ip = SessionUtils.getIpAddress();
        System.out.println("ip is:" + ip);
        al.setIpAddress(ip);

        if (ma != null) {
            al.setMainAccount(ma);
        } else if (sa != null) {
            al.setStaffAccount(sa);
        }
        System.out.println("StaffAccount is:" + sa.toString());
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
