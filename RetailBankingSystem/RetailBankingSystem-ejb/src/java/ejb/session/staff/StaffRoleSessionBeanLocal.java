/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.staff.Role;
import java.util.List;
import javax.ejb.Local;
import util.exception.common.DuplicateStaffRoleException;
import util.exception.common.NoRolesExistException;
import util.exception.common.StaffRoleNotFoundException;
import util.exception.common.UpdateStaffRoleException;

/**
 *
 * @author leiyang
 */
@Local
public interface StaffRoleSessionBeanLocal {
    public List<Role> getAllRoles() throws NoRolesExistException ;
    public Role createRole(Role r) throws DuplicateStaffRoleException ;
    public Role updateRole(Role r) throws UpdateStaffRoleException ;
    public Role getRoleByName(String roleName) throws StaffRoleNotFoundException;
    public Role removeRole(String id);
}
