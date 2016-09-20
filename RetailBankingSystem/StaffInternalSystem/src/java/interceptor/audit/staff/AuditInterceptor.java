/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interceptor.audit.staff;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import utils.AuditUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Interceptor
@Audit
public class AuditInterceptor implements Serializable {

    @EJB
    AuditSessionBeanLocal auditSessionBean;
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        System.out.println("AuditInterceptor: Intercepting");
        Object result = null;
        String activityLog = "";
        List<String> input = new ArrayList<>();
        String functionName = context.getTarget().getClass().getSimpleName() + "." + context.getMethod().getName();
        activityLog = context.getMethod().getAnnotation(Audit.class).activtyLog();

        Annotation[][] anns = context.getMethod().getParameterAnnotations();
        Object[] params = context.getParameters();
        System.out.println("getParameterAnnotations()" + anns.toString());
        System.out.println("getParameters()" + params.toString());
        for (int i = 0; i < anns.length; i ++) {
            System.out.println("Annotation[].length = " + anns.length);
            for (int j = 0; j < anns[i].length; j ++) {
                System.out.println("Annotation["+ i +"][].length = " + anns[i].length);
            }
        }
        System.out.println("params[].length = " + params.length);
        
        for (int i = 0; i < params.length; i++) {
            Object value = params[i];
            System.out.println(":" + value.toString());
            if (anns[i].length > 0) {
                for (Annotation annotation : anns[i]) {
                    if (annotation instanceof HalfHidden) {
                        input.add(AuditUtils.hiddenFourString(value.toString()));
                    } else if (annotation instanceof FullHidden) {
                        input.add(AuditUtils.hiddenFullString(value.toString()));
                    }
                }
            } else {
                input.add(value.toString());
            }
        }

        System.out.println("Loop finished");
//        String parameterString
//                = Arrays.asList(context.getParameters()).toString();
//        System.out.println("AuditInterceptor: The call to "
//                + functionName
//                + parameterString + "...");
        try {
            result = context.proceed();
        } catch (Exception e) {
            System.out.println("AuditInterceptor: ....which raised " + e);
            throw e;
        }
        System.out.println("First try");
        StaffAccount sa = null;
        try {
            if (SessionUtils.getStaffUsername()!= null) {
                sa = staffAccountSessionBean.getAccountByUsername(SessionUtils.getStaffUsername());
            }
        } catch (NullPointerException ex) {
            System.out.println("Null Pointer");
        }
        System.out.println("Second try and StaffAccount is: " + sa.toString());
//        System.out.println("AuditInterceptor: ....has returned " + result);
        AuditLog al = AuditUtils.createAuditLog(activityLog, functionName, input, result.toString(), null, sa);
        System.out.println("AuditLog Created: " + al.toString());
        auditSessionBean.insertAuditLog(al);
        return result;
    }
}
