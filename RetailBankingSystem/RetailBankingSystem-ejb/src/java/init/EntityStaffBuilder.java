/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.staff.Role;
import entity.staff.StaffAccount;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class EntityStaffBuilder {

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    
    public void initStaffAndRoles() {
        Role superAdminRole = new Role(EnumUtils.UserRole.SUPER_ADMIN.toString());
        superAdminRole.setDescription("- Manage staffs in the workspace\n"
                + "- Possess highest accessibility to the functionalities in the system\n"
                + "- Guarantee the system to function normally\n"
                + "- Prevent unexpected operations from unauthorised access\n");
        superAdminRole = staffRoleSessionBean.addRole(superAdminRole);
        Role customerServiceRole = new Role(EnumUtils.UserRole.CUSTOMER_SERVICE.toString());
        customerServiceRole = staffRoleSessionBean.addRole(customerServiceRole);
        Role financialAnalystRole = new Role(EnumUtils.UserRole.FINANCIAL_ANALYST.toString());
        financialAnalystRole.setDescription("Generate and make use of BI results to support decision making");
        financialAnalystRole = staffRoleSessionBean.addRole(financialAnalystRole);
        Role financialOfficerRole = new Role(EnumUtils.UserRole.FINANCIAL_OFFICER.toString());
        financialOfficerRole.setDescription("Offer wealth management services to the customers\n"
                + "(Offering wealth services but do not design wealth products)");
        financialOfficerRole = staffRoleSessionBean.addRole(financialOfficerRole);
        Role generalTellerRole = new Role(EnumUtils.UserRole.GENERAL_TELLER.toString());
        generalTellerRole = staffRoleSessionBean.addRole(generalTellerRole);
        Role loanOfficerRole = new Role(EnumUtils.UserRole.LOAN_OFFICIER.toString());
        loanOfficerRole.setDescription("- Provide loan-related services to the customers\n"
                + "- Process loan applications based on security requirements\n"
                + "- Assess customers' credit scoring\n"
                + "- Promote loan products\n");
        loanOfficerRole = staffRoleSessionBean.addRole(loanOfficerRole);
        Role productManagerRole = new Role(EnumUtils.UserRole.PRODUCT_MANAGER.toString());
        productManagerRole.setDescription("- Carry out decision making practices according to analytic result\n"
                + "- Design new financial products\n"
                + "- Adjust existing financial products\n"
                + "- Launch financial products to the market\n");
        productManagerRole = staffRoleSessionBean.addRole(productManagerRole);
        Role relationshipManagerRole = new Role(EnumUtils.UserRole.RELATIONSHIP_MANAGER.toString());
        relationshipManagerRole = staffRoleSessionBean.addRole(relationshipManagerRole);

        StaffAccount superAdminAccount = new StaffAccount();
        superAdminAccount.setUsername(ConstantUtils.SUPER_ADMIN_USERNAME);
        superAdminAccount.setPassword(ConstantUtils.SUPER_ADMIN_PASSWORD);
        superAdminAccount.setFirstName("Account");
        superAdminAccount.setLastName("Super");
        superAdminAccount.setEmail("superadmin@merlionbank.com");
        superAdminAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        superAdminAccount.addRole(superAdminRole);
        List<StaffAccount> staffAccounts1 = new ArrayList<StaffAccount>();
        staffAccounts1.add(superAdminAccount);
        superAdminRole.setStaffAccounts(staffAccounts1);
        staffAccountSessionBean.createAccount(superAdminAccount);

        StaffAccount customerServiceAccount = new StaffAccount();
        customerServiceAccount.setUsername(ConstantUtils.CUSTOMER_SERVICE_USERNAME);
        customerServiceAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        customerServiceAccount.setFirstName("Service");
        customerServiceAccount.setLastName("Customer");
        customerServiceAccount.setEmail("customer_service@merlionbank.com");
        customerServiceAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        customerServiceAccount.addRole(customerServiceRole);
        List<StaffAccount> staffAccounts2 = new ArrayList<StaffAccount>();
        staffAccounts2.add(customerServiceAccount);
        customerServiceRole.setStaffAccounts(staffAccounts2);
        staffAccountSessionBean.createAccount(customerServiceAccount);

        StaffAccount financialAnalystAccount = new StaffAccount();
        financialAnalystAccount.setUsername(ConstantUtils.FINANCIAL_ANALYST_USERNAME);
        financialAnalystAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        financialAnalystAccount.setFirstName("Analyst");
        financialAnalystAccount.setLastName("Financial");
        financialAnalystAccount.setEmail("financial_analyst@merlionbank.com");
        financialAnalystAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        financialAnalystAccount.addRole(financialAnalystRole);
        List<StaffAccount> staffAccounts3 = new ArrayList<StaffAccount>();
        staffAccounts3.add(financialAnalystAccount);
        financialAnalystRole.setStaffAccounts(staffAccounts3);
        staffAccountSessionBean.createAccount(financialAnalystAccount);

        StaffAccount financialOfficerAccount = new StaffAccount();
        financialOfficerAccount.setUsername(ConstantUtils.FINANCIAL_OFFICER_USERNAME);
        financialOfficerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        financialOfficerAccount.setFirstName("Officer");
        financialOfficerAccount.setLastName("Financial");
        financialOfficerAccount.setEmail("financial_officer@merlionbank.com");
        financialOfficerAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        financialOfficerAccount.addRole(financialOfficerRole);
        List<StaffAccount> staffAccounts4 = new ArrayList<StaffAccount>();
        staffAccounts4.add(financialOfficerAccount);
        financialOfficerRole.setStaffAccounts(staffAccounts4);
        staffAccountSessionBean.createAccount(financialOfficerAccount);

        StaffAccount generalTellerAccount = new StaffAccount();
        generalTellerAccount.setUsername(ConstantUtils.GENERAL_TELLER_USERNAME);
        generalTellerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        generalTellerAccount.setFirstName("General");
        generalTellerAccount.setLastName("Teller");
        generalTellerAccount.setEmail("general_teller@merlionbank.com");
        generalTellerAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        generalTellerAccount.addRole(generalTellerRole);
        List<StaffAccount> staffAccounts5 = new ArrayList<StaffAccount>();
        staffAccounts5.add(generalTellerAccount);
        generalTellerRole.setStaffAccounts(staffAccounts5);
        staffAccountSessionBean.createAccount(generalTellerAccount);

        StaffAccount loanOfficerAccount = new StaffAccount();
        loanOfficerAccount.setUsername(ConstantUtils.LOAN_OFFICIER_USERNAME);
        loanOfficerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        loanOfficerAccount.setFirstName("Loan");
        loanOfficerAccount.setLastName("Officer");
        loanOfficerAccount.setEmail("loan_officer@merlionbank.com");
        loanOfficerAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        loanOfficerAccount.addRole(loanOfficerRole);
        List<StaffAccount> staffAccounts6 = new ArrayList<StaffAccount>();
        staffAccounts6.add(loanOfficerAccount);
        loanOfficerRole.setStaffAccounts(staffAccounts6);
        staffAccountSessionBean.createAccount(loanOfficerAccount);

        StaffAccount productManagerAccount = new StaffAccount();
        productManagerAccount.setUsername(ConstantUtils.PRODUCT_MANAGER_USERNAME);
        productManagerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        productManagerAccount.setFirstName("Product");
        productManagerAccount.setLastName("Manager");
        productManagerAccount.setEmail("product_manager@merlionbank.com");
        productManagerAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        productManagerAccount.addRole(productManagerRole);
        List<StaffAccount> staffAccounts7 = new ArrayList<StaffAccount>();
        staffAccounts7.add(productManagerAccount);
        productManagerRole.setStaffAccounts(staffAccounts7);
        staffAccountSessionBean.createAccount(productManagerAccount);
        
        StaffAccount relationshipManagerAccount = new StaffAccount();
        relationshipManagerAccount.setUsername(ConstantUtils.RELATIONSHIP_MANAGER_USERNAME);
        relationshipManagerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        relationshipManagerAccount.setFirstName("Relationship");
        relationshipManagerAccount.setLastName("Manager");
        relationshipManagerAccount.setEmail("relationship_manager@merlionbank.com");
        relationshipManagerAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        relationshipManagerAccount.addRole(relationshipManagerRole);
        List<StaffAccount> staffAccounts8 = new ArrayList<StaffAccount>();
        staffAccounts8.add(relationshipManagerAccount);
        relationshipManagerRole.setStaffAccounts(staffAccounts8);
        staffAccountSessionBean.createAccount(relationshipManagerAccount);
    }
}
