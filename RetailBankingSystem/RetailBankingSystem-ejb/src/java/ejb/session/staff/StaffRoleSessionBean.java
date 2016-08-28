/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.Role;
import javax.ejb.Stateless;

/**
 *
 * @author leiyang
 */
@Stateless
public class StaffRoleSessionBean implements StaffRoleSessionBeanLocal {
    @Override
    public Role getSuperAdminRoles() {
        Role superRole = new Role();
        superRole.setSuperUserRight(Boolean.TRUE);
        superRole.setRoleName("Super Admin");
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
}
