/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.StaffAccount;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leiyang
 */
@Stateless
//@DeclareRoles({
//    "superUserRight", 
//    "customerAccessRight", 
//    "depositAccessRight",
//    "cardAccessRight",
//    "loanAccessRight",
//    "billAccessRight",
//    "wealthAccessRight",
//    "portfolioAccessRight",
//    "analyticsAccessRight"
//})
public class StaffAccountSessionBean implements StaffAccountSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public StaffAccount loginAccount(String username, String password) {
        try {
            StaffAccount user = em.find(StaffAccount.class, username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public void createAccount(StaffAccount sa) {
        em.persist(sa);
    }
}
