/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.AccountRuleSessionBeanLocal;
import ejb.session.dams.BankAccountSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.common.Transaction;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.DepositRule;
import entity.dams.rules.Interest;
import entity.dams.rules.TimeRangeInterest;
import entity.staff.Role;
import entity.staff.StaffAccount;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import utils.EnumUtils;
import utils.HashPwdUtils;

/**
 *
 * @author leiyang
 */
@Singleton
@LocalBean
@Startup
public class EntityBuilderBean {

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private AccountRuleSessionBeanLocal accountRuleSessionBean;
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBean;

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
        if (needInit()) {
            buildEntities();
        }// else skip this.
    }

    // Use Super Admin Account as a flag
    private Boolean needInit() {
        String u = "adminadmin";
        String p = HashPwdUtils.hashPwd("password");
        StaffAccount sa = staffAccountSessionBean.loginAccount(u, p);
        if (sa == null) {
            StaffAccount superAccount = new StaffAccount();
            superAccount.setUsername(u);
            superAccount.setPassword(p);
            superAccount.setFirstName("Super");
            superAccount.setLastName("Account");
            Role r = staffRoleSessionBean.getSuperAdminRole();
            superAccount.setRole(r);
            staffAccountSessionBean.createAccount(superAccount);
            return true;
        } else {
            return false;
        }
    }

    private void buildEntities() {
        // TODO: init other entities here
        // TODO: init with an organized flow structure
        // these are just temporary data for emergency use.
        // Yifan pls help edit for me on top of these.
        initCustomer();
        initInterest();
        initDepositRules();
        initDepositAccounts();
    }
    
    private void initCustomer() {
        String u = "c1234567";
        String p = HashPwdUtils.hashPwd("password");
        
        MainAccount ma = null;
        Customer c = new Customer();
        c.setAddress("some fake address"); //make it a bit more real
        c.setBirthDay(new Date()); //make some real birthday.
        c.setEmail("wangzhe.lynx@gmail.com");
        c.setFirstname("Yifan");
        c.setGender("MALE"); // pls modify gender to enum type
        c.setIdentityNumber("S1234567Z");
        c.setIdentityType("CITIZEN"); // same for this to enum type
        c.setIncome(5000);
        c.setLastname("Chen");
        c.setNationality("Singapore"); //enum type if possible
        c.setOccupation("programmer");
        c.setPhone("81567758"); //must use real phone number as we need sms code
        c.setPostalCode("654321");
        c.setMainAccount(ma);
        ma = new MainAccount();
        ma.setUserID(u);
        ma.setPassword(p);
        ma.setStatus(EnumUtils.StatusType.ACTIVE);
        ma.setCustomer(c);
        
        newCustomerSessionBean.createCustomer(c, ma);
    }
    
    private void initInterest() {
        Interest i = new Interest();
        i.setName("Normal Interest");
        i.setVersion(0);
        i.setPercentage(new BigDecimal(0.0001));// 0.01%
        accountRuleSessionBean.addInterest(i);
        
        // Init other interests
        initTimeRangeInterest();
        initConditionalInterest();
    }
    
    private void initTimeRangeInterest() {
        // Add to a fixedDepositAccount
        // https://www.bankbazaar.sg/fixed-deposit/ocbc-fixed-deposit-rate.html
        TimeRangeInterest i = new TimeRangeInterest();
        i.setName("1month-2month-$5000-$20000-0.05%");
        i.setVersion(0);
        i.setDefaultFixedDepositAccount(Boolean.TRUE);
        i.setStartMonth(1);
        i.setEndMonth(2);
        i.setMinimum(new BigDecimal(5000));
        i.setMaximum(new BigDecimal(20000));
        i.setPercentage(new BigDecimal(0.0005));
        accountRuleSessionBean.addInterest(i);
    }
    
    private void initConditionalInterest() {
        // Credit your salary of at least $2,000 through GIRO
        ConditionInterest ci = new ConditionInterest();
        ci.setName("OCBC 360 Credit Salary");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.SALARY);
        ci.setAmount(new BigDecimal(2000));
        ci.setDefaultCustomizedAccountName("OCBC 360");
        ci.setPercentage(new BigDecimal(0.012));
        accountRuleSessionBean.addInterest(ci);
        // Pay any 3 bills online or through GIRO
        ci = new ConditionInterest();
        ci.setName("OCBC 360 Pay Bill");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.BILL);
        ci.setAmount(new BigDecimal(3));
        ci.setDefaultCustomizedAccountName("OCBC 360");
        ci.setPercentage(new BigDecimal(0.005));
        accountRuleSessionBean.addInterest(ci);
        // Spend at least $500 on OCBC Credit Cards
        ci = new ConditionInterest();
        ci.setName("OCBC 360 Credit Card Spend");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.CCSPENDING);
        ci.setAmount(new BigDecimal(500));
        ci.setDefaultCustomizedAccountName("OCBC 360");
        ci.setPercentage(new BigDecimal(0.005));
        accountRuleSessionBean.addInterest(ci);
        // Insure or Invest and get this bonus for 12 months
        ci = new ConditionInterest();
        ci.setName("OCBC 360 Invest");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.CCSPENDING);
        ci.setDefaultCustomizedAccountName("OCBC 360");
        ci.setPercentage(new BigDecimal(0.01));
        ci.setBenefitMonths(12);
        accountRuleSessionBean.addInterest(ci);
        // Increase your account balance from the previous month's balance
        ci = new ConditionInterest();
        ci.setName("OCBC 360 Increase Balance");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.INCREASE);
        ci.setDefaultCustomizedAccountName("OCBC 360");
        ci.setPercentage(new BigDecimal(0.01));
        accountRuleSessionBean.addInterest(ci);
    }
    
    private void initDepositRules() {
        DepositRule dr = new DepositRule();
        dr.setName("OCBC 360 Deposit Rules");
        dr.setVersion(0);
        dr.setDefaultCustomizedAccountName("OCBC 360");
        dr.setInitialDeposit(new BigDecimal(1000));
        dr.setMinBalance(new BigDecimal(3000));
        dr.setCharges(new BigDecimal(2));
        dr.setAnnualFees(BigDecimal.ZERO);
        dr.setWaivedFeesCounter(12);
        accountRuleSessionBean.addDepositRule(dr);
    }
    
    private void initDepositAccounts() {
        initCustomAccount();
        initCurrentAccount();
        initSavingAccount();
        initFixedDepositAccount();
    }
    
    private void initCustomAccount() {
        DepositAccount da = new DepositAccount();
        da.setName("OCBC 360");
        da.setDescription("Earn bonus interest when you do all or any of these");
        da.setType(EnumUtils.DepositAccountType.CUSTOM);
        da.setBalance(new BigDecimal(1000));
        da.setDepositRule(accountRuleSessionBean.getDepositRuleByAccountName(da.getName()));
        
        bankAccountSessionBean.createAccount(da);
    }
    
    private void initCurrentAccount() {
            
    }
    
    private void initSavingAccount() {
            
    }
    
    private void initFixedDepositAccount() {
            
    }
}
