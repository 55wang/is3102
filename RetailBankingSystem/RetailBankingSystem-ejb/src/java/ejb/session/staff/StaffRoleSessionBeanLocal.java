/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.Role;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface StaffRoleSessionBeanLocal {
    public Role getSuperAdminRoles();
}
