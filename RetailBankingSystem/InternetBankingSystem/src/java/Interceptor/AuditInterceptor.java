package Interceptor;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.interceptor.Interceptor;
import java.util.*;
import javax.ejb.EJB;
import utils.AuditUtils;
import utils.SessionUtils;

@Interceptor
@Audit
public class AuditInterceptor implements Serializable {

    @EJB
    AuditSessionBeanLocal auditSessionBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        Object result = null;
        String activityLog = "";
        List<String> input = new ArrayList<>();
        String functionName = context.getTarget().getClass().getSimpleName() + "." + context.getMethod().getName();
        activityLog = context.getMethod().getAnnotation(Audit.class).activtyLog();

        System.out.println(context.getMethod().getParameterAnnotations().length);
        System.out.println(context.getParameters().length);

        Annotation[][] anns = context.getMethod().getParameterAnnotations();
        Object[] params = context.getParameters();
        for (int i = 0; i < params.length; i++) {
            Object value = params[i];
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
        MainAccount ma = null;
        try {
            if (SessionUtils.getUserName() != null) {
                ma = loginSessionBean.getMainAccountByUserID(SessionUtils.getUserName());
            }
        } catch (NullPointerException ex) {
            System.out.println("Null Pointer");
        }

//        System.out.println("AuditInterceptor: ....has returned " + result);
        AuditLog al = AuditUtils.createAuditLog(activityLog, functionName, input, result.toString(), ma, null);
        auditSessionBean.insertAuditLog(al);
        return result;
    }
}
