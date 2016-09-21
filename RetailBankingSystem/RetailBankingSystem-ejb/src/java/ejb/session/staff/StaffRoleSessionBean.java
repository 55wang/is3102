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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class StaffRoleSessionBean implements StaffRoleSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public List<Role> getAllRoles() {
        Query q = em.createQuery("SELECT r FROM Role r");
        return q.getResultList();
    }
    
    @Override
    public Role addRole(Role r) {
        try {
            em.persist(r);
            return r;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public Role updateRole(Role r) {
        try {
            em.merge(r);
            return r;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Role findRoleByName(String roleName) {
        return em.find(Role.class, roleName);
    }
}
