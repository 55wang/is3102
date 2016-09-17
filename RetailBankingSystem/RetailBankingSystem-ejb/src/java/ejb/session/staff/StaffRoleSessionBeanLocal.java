/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.staff.Role;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface StaffRoleSessionBeanLocal {
    public Role getSuperAdminRole();
    public List<Role> getAllRoles();
    public Boolean addRole(Role r);
    public Boolean updateRole(Role r);
    public Role findRoleByName(String roleName);
//    public void addUserToRole(StaffAccount sa, Role r);
}
