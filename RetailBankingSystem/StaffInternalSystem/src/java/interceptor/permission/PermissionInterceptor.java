/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interceptor.permission;

import entity.staff.StaffAccount;
import exception.UserNotInRoleException;
import java.io.Serializable;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import utils.EnumUtils.Permission;
import utils.SessionUtils;
import utils.UserUtils;

/**
 *
 * @author leiyang
 */
@Interceptor
public class PermissionInterceptor implements Serializable {
    //https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part2/chapter29/example_ex15_1_custom_security.html
    //http://docs.oracle.com/javaee/6/tutorial/doc/gkedm.html
    //https://anismiles.wordpress.com/2012/03/02/securing-versioning-and-auditing-rest-jax-rs-jersey-apis/
    
    @AroundInvoke
    public Object interceptUserRole(InvocationContext context) throws Exception {
        System.out.println("PermissionInterceptor");
        StaffAccount sa = SessionUtils.getStaff();
        //TODO: throw exception if not in role
        Permission role = context.getMethod().getAnnotation(RoleAllowed.class).role();
        Permission[] roles = context.getMethod().getAnnotation(RolesAllowed.class).roles();
        String functionName = context.getTarget().getClass().getSimpleName() + "." + context.getMethod().getName();
        
        if (role != null) {
            if (!UserUtils.isUserInRole(role)) {
                System.out.println("User is not allowed to access:" + functionName);
                throw new UserNotInRoleException("User is not allowed to access:" + functionName);
            }
        } else if (roles != null && roles.length > 0) {
            if (!UserUtils.isUserInRoles(roles)) {
                System.out.println("User is not allowed to access:" + functionName);
                throw new UserNotInRoleException("User is not allowed to access:" + functionName);
            }
        }
        
        return context.proceed();
    }
}
