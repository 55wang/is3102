/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.cms.CustomerCaseSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import entity.customer.MainAccount;
import java.util.ArrayList;
import java.util.Date;
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
public class EntityCaseBuilder {
    
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private LoginSessionBeanLocal loginBean;

    public void initCase() {
        MainAccount demoMainAccount = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        CustomerCase cc = new CustomerCase();
        Issue issue = new Issue();
        List<Issue> issues = new ArrayList<>();

        issue.setTitle("Deposit Account Problem");
        issue.setField(EnumUtils.IssueField.DEPOSIT);
        issue.setDetails("My deposit account has some suspicious credit histories. Could you please help me to check?");
        issue.setCustomerCase(cc);

        issues.add(issue);

        cc.setIssues(issues);
        cc.setTitle("My Deposit Account has Some problems");
        cc.setCreateDate(new Date());
        cc.setMainAccount(demoMainAccount);
        cc.setStaffAccount(staffAccountSessionBean.getAccountByUsername(ConstantUtils.SUPER_ADMIN_USERNAME));
        cc.setCaseStatus(EnumUtils.CaseStatus.ONHOLD);

        customerCaseSessionBean.saveCase(cc);

        cc = new CustomerCase();
        issue = new Issue();
        issues = new ArrayList<Issue>();

        issue.setTitle("Loan Problem");
        issue.setField(EnumUtils.IssueField.DEPOSIT);
        issue.setDetails("My loan account has some problems. Could you please help me to check?");
        issue.setCustomerCase(cc);

        issues.add(issue);

        issue = new Issue();

        issue.setTitle("Loan Problem");
        issue.setField(EnumUtils.IssueField.INVESTMENT);
        issue.setDetails("My loan account has some problems. Could you please help me to check?");
        issue.setCustomerCase(cc);

        issues.add(issue);

        cc.setIssues(issues);
        cc.setTitle("Loan Problem");
        cc.setCreateDate(new Date());
        cc.setMainAccount(demoMainAccount);
        cc.setStaffAccount(staffAccountSessionBean.getAccountByUsername(ConstantUtils.SUPER_ADMIN_USERNAME));
        cc.setCaseStatus(EnumUtils.CaseStatus.ONHOLD);

        customerCaseSessionBean.saveCase(cc);
    }
}
