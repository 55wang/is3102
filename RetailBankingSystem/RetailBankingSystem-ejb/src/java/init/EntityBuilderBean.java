/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.cms.CustomerCaseSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import entity.customer.MainAccount;
import entity.staff.Announcement;
import entity.staff.StaffAccount;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.CaseStatus;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author leiyang
 */
@Singleton
@LocalBean
@Startup
public class EntityBuilderBean {

    // builders
    @EJB
    private EntityCustomerBuilder entityCustomerBuilder;
    @EJB
    private EntityCreditCardOrderBuilder entityCreditCardOrderBuilder;
    @EJB
    private EntityCreditCardProductBuilder entityCreditCardProductBuilder;
    @EJB
    private EntityPromoProductBuilder entityPromoProductBuilder;
    @EJB
    private EntityBillOrganizationBuilder entityBillOrgBuilder;
    @EJB
    private EntityStaffBuilder entityStaffBuilder;
    @EJB
    private EntityDAMSBuilder entityDAMSBuilder;
    
    // session beans
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private CardTransactionSessionBeanLocal cardTransactionBean;

    
    private MainAccount demoMainAccount;
    private RewardCardProduct demoRewardCardProduct;
    private PromoProduct demoPromoProduct;

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
        System.out.println(GenerateAccountAndCCNumber.generateAccountNumber());
        if (needInit()) {
            buildEntities();
        } else {
            testInterestRules();
        }
    }

    // Use Super Admin Account as a flag
    private Boolean needInit() {
        StaffAccount sa = staffAccountSessionBean.getAccountByUsername(ConstantUtils.SUPER_ADMIN_USERNAME);
        return sa == null;
    }

    private void buildEntities() {
        entityStaffBuilder.initStaffAndRoles();
        demoMainAccount = entityCustomerBuilder.initCustomer();
        entityDAMSBuilder.initDAMS(demoMainAccount);
        demoPromoProduct = entityPromoProductBuilder.initPromoProduct(demoPromoProduct);
        demoRewardCardProduct = entityCreditCardProductBuilder.initCreditCardProduct(demoMainAccount, demoPromoProduct);
        initCase();
        initNotification();
        entityCreditCardOrderBuilder.initCreditCardOrder(demoMainAccount, demoRewardCardProduct, demoPromoProduct);
        entityBillOrgBuilder.initBillOrganization();
    }

    public void initCase() {
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
        cc.setCaseStatus(CaseStatus.ONHOLD);

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
        cc.setCaseStatus(CaseStatus.ONHOLD);

        customerCaseSessionBean.saveCase(cc);
    }

    public void initNotification() {
        Announcement n1 = new Announcement();
        n1.setTitle("Happy Holidy!!");
        n1.setTitle("Happy Holidy!!");
    }

    private void testCreditCard() {

        Date startDate = DateUtils.getBeginOfDay();
        Date endDate = DateUtils.getEndOfDay();

        System.out.println(startDate);
        System.out.println(startDate);
        List<CardTransaction> result = cardTransactionBean.getTransactionByStartDateAndEndDate(startDate, endDate);
        System.out.println(result);
//        System.out.println("testCreditCard");
//        try {
//            CreditCardAccount c = cardAcctSessionBean.getCardByCardNumber("4545454545454545");
//            System.out.println(c);
//        } catch(Exception e) {
////            e.printStackTrace();
//        }
//        List<CreditCardAccount> cards = cardAcctSessionBean.showAllCreditCardAccount(EnumUtils.CardAccountStatus.CLOSED, 8L);
//        System.out.println(cards);
    }

    private void testInterestRules() {

        // Get Product
//        DepositAccount da = customerDepositSessionBean.getAccountFromId(1L);
//        // Get Interest
//        List<Interest> interests = ((DepositAccountProduct) da.getProduct()).getInterestRules();
//        for (Interest i : interests) {
//            if (i instanceof ConditionInterest) {
//                System.out.print(interestAccrualSessionBean.isAccountMeetCondition(da, (ConditionInterest) i));
//            }
//        }
    }
}
