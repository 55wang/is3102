/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.staff.Role;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.common.DuplicateStaffRoleException;
import util.exception.common.NoRolesExistException;
import util.exception.common.StaffRoleNotFoundException;
import util.exception.common.UpdateStaffRoleException;

/**
 *
 * @author leiyang
 */
@Stateless
public class StaffRoleSessionBean implements StaffRoleSessionBeanLocal, StaffRoleSessionBeanRemote {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public List<Role> getAllRoles() throws NoRolesExistException {
        Query q = em.createQuery("SELECT r FROM Role r");
        try {
            
            List<Role> result = q.getResultList();
            
            if (result.isEmpty()) {
                throw new NoRolesExistException("No Staff Role Not Found");
            }
            
            return result;
        } catch (NoResultException ex) {
            System.out.println("Catch!");
            throw new NoRolesExistException("No Staff Role Not Found");
        }
    }
    
    @Override
    public Role createRole(Role r) throws DuplicateStaffRoleException {
        
        try {
            if (r.getRoleName() == null) {
                return null;
            } else {
                Role result = em.find(Role.class, r.getRoleName());
                if (result != null) {
                    throw new DuplicateStaffRoleException("Duplicate Role!");
                }
            }

            em.persist(r);
            return r;
        } catch (EntityExistsException e) {
            throw new DuplicateStaffRoleException("Duplicate Role!");
        }
        
    }
    
    @Override
    public Role updateRole(Role r) throws UpdateStaffRoleException {
        
        try {

            if (r.getRoleName() == null) {
                throw new UpdateStaffRoleException("Not an entity!");
            }

            em.merge(r);
            return r;
        } catch (IllegalArgumentException e) {
            throw new UpdateStaffRoleException("Not an entity!");
        }
    }
    
    @Override
    public Role getRoleByName(String roleName) throws StaffRoleNotFoundException {
        
        if (roleName == null) {
            throw new StaffRoleNotFoundException("Staff Role Not Found!");
        }

        try {
            
            Role result = em.find(Role.class, roleName);
            if (result == null) {
                throw new StaffRoleNotFoundException("Staff Role Not Found!");
            }
            // REMARK: See if we need to check cancelstatus
            return result;
        } catch (IllegalArgumentException e) {
            throw new StaffRoleNotFoundException("Staff Role Not Found!");
        }
        
    }
    
    @Override
    public Role removeRole(String id) {
        Role result = em.find(Role.class, id);
        if (result == null) {
            return null;
        }
        em.remove(result);
        return result;
    }
}
