/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.Role;
import entity.StaffAccount;
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
    public Role getSuperAdminRole() {
        Role superRole = em.find(Role.class, "Super Admin");
        if (superRole == null) {
            superRole = new Role("Super Admin");
            superRole.setSuperUserRight(Boolean.TRUE);
            em.persist(superRole);
        }
//        superRole.setAnalyticsAccessRight(Boolean.TRUE);
//        superRole.setBillAccessRight(Boolean.TRUE);
//        superRole.setCardAccessRight(Boolean.TRUE);
//        superRole.setCustomerAccessRight(Boolean.TRUE);
//        superRole.setDepositAccessRight(Boolean.TRUE);
//        superRole.setLoanAccessRight(Boolean.TRUE);
//        superRole.setPortfolioAccessRight(Boolean.TRUE);
//        superRole.setWealthAccessRight(Boolean.TRUE);
        return superRole;
    }
    @Override
    public List<Role> getAllRoles() {
        Query q = em.createQuery("SELECT r FROM Role r");
        return q.getResultList();
    }
    
    @Override
    public Boolean addRole(Role r) {
        try {
            em.persist(r);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }
    
    @Override
    public Boolean updateRole(Role r) {
        try {
            em.merge(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Role findRoleByName(String roleName) {
        return em.find(Role.class, roleName);
    }
    
//    @Override 
//    public void addUserToRole(StaffAccount sa, Role r) {
//        try {
//            r.addStaffAccount(sa);
//            em.merge(r);
//            em.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
