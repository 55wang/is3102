
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.session.staff;

import entity.staff.Role;
import java.util.List;
import javax.ejb.Remote;
import util.exception.common.DuplicateStaffRoleException;
import util.exception.common.NoRolesExistException;
import util.exception.common.StaffRoleNotFoundException;
import util.exception.common.UpdateStaffRoleException;

/**
 *
 * @author leiyang
 */

@Remote
public interface StaffRoleSessionBeanRemote {
    public List<Role> getAllRoles() throws NoRolesExistException ;
    public Role createRole(Role r) throws DuplicateStaffRoleException ;
    public Role updateRole(Role r) throws UpdateStaffRoleException ;
    public Role getRoleByName(String roleName) throws StaffRoleNotFoundException;
    public Role removeRole(String id);
}
