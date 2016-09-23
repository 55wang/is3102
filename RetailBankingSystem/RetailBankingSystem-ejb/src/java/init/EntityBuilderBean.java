/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import BatchProcess.InterestAccrualSessionBeanLocal;
import ejb.session.card.NewCardProductSessionBeanLocal;
import ejb.session.cms.CustomerCaseSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.MileCardProduct;
import entity.customer.Customer;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.DepositAccountProduct;
import entity.dams.account.FixedDepositAccountProduct;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import entity.staff.Role;
import entity.staff.StaffAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.CaseStatus;
import server.utilities.EnumUtils.DepositAccountType;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.Nationality;
import server.utilities.EnumUtils.Occupation;
import server.utilities.EnumUtils.StatusType;
import server.utilities.HashPwdUtils;

/**
 *
 * @author leiyang
 */
@Singleton
@LocalBean
@Startup
public class EntityBuilderBean {

    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private InterestSessionBeanLocal interestSessionBean;
    @EJB
    private CustomerDepositSessionBeanLocal customerDepositSessionBean;
    @EJB
    private InterestAccrualSessionBeanLocal interestAccrualSessionBean;
    @EJB
    private DepositProductSessionBeanLocal depositProductSessionBean;
    @EJB
    private NewCardProductSessionBeanLocal newCardProductSessionBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;

    private Interest demoNormalInterestData;
    private List<Interest> demoRangeInterestData = new ArrayList<>();
    private List<Interest> demoTimeRangeInterestData = new ArrayList<>();
    private List<Interest> demoConditionalInterestDataForCustomDepositProduct = new ArrayList<>();
    private List<Interest> demoConditionalInterestDataForSavingsDepositProduct = new ArrayList<>();
    private MainAccount demoMainAccount;

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
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

    private void testInterestRules() {
        // Get Product
        DepositAccount da = customerDepositSessionBean.getAccountFromId(1L);
        // Get Interest
        List<Interest> interests = ((DepositAccountProduct) da.getProduct()).getInterestRules();
        for (Interest i : interests) {
            if (i instanceof ConditionInterest) {
                System.out.print(interestAccrualSessionBean.isAccountMeetCondition(da, (ConditionInterest) i));
            }
        }
    }

    private void buildEntities() {
        // TODO: init other entities here
        // TODO: init with an organized flow structure
        // these are just temporary data for emergency use.
        // Yifan pls help edit for me on top of these.
        initStaffAndRoles();
        initCustomer();
        initInterest();
        initDepositProducts();
        initDepositAccounts();

        initCreditCardProduct();
        initCase();
    }

    public void initCreditCardProduct() {
        MileCardProduct mca = new MileCardProduct();
        mca.setLocalMileRate(1.3);
        mca.setOverseaMileRate(2);
        mca.setMinSpending(true);
        mca.setMinSpendingAmount(2000);
        mca.setProductName("Merlion MileCard");
        newCardProductSessionBean.createMileProduct(mca);
    }

    private void initStaffAndRoles() {
        Role superAdminRole = new Role(EnumUtils.UserRole.SUPER_ADMIN.toString());
        superAdminRole.setDescription("Everything includes front end services and back end administrations");
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
        loanOfficerRole.setDescription("Provide loan-related services to the customers\n"
                + "(Provide service but do not design loan products)");
        loanOfficerRole = staffRoleSessionBean.addRole(loanOfficerRole);
        Role productManagerRole = new Role(EnumUtils.UserRole.PRODUCT_MANAGER.toString());
        productManagerRole.setDescription("Design, adjust, and launch financial products to the market");
        productManagerRole = staffRoleSessionBean.addRole(productManagerRole);

        StaffAccount superAdminAccount = new StaffAccount();
        superAdminAccount.setUsername(ConstantUtils.SUPER_ADMIN_USERNAME);
        superAdminAccount.setPassword(ConstantUtils.SUPER_ADMIN_PASSWORD);
        superAdminAccount.setFirstName("Account");
        superAdminAccount.setLastName("Super");
        superAdminAccount.setEmail("superadmin@merlionbank.com");
        superAdminAccount.setStatus(StatusType.ACTIVE);
        superAdminAccount.setRole(superAdminRole);
        staffAccountSessionBean.createAccount(superAdminAccount);

        StaffAccount customerServiceAccount = new StaffAccount();
        customerServiceAccount.setUsername(ConstantUtils.CUSTOMER_SERVICE_USERNAME);
        customerServiceAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        customerServiceAccount.setFirstName("Service");
        customerServiceAccount.setLastName("Customer");
        customerServiceAccount.setEmail("customer_service@merlionbank.com");
        customerServiceAccount.setStatus(StatusType.ACTIVE);
        customerServiceAccount.setRole(customerServiceRole);
        staffAccountSessionBean.createAccount(customerServiceAccount);

        StaffAccount financialAnalystAccount = new StaffAccount();
        financialAnalystAccount.setUsername(ConstantUtils.FINANCIAL_ANALYST_USERNAME);
        financialAnalystAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        financialAnalystAccount.setFirstName("Analyst");
        financialAnalystAccount.setLastName("Financial");
        financialAnalystAccount.setEmail("financial_analyst@merlionbank.com");
        financialAnalystAccount.setStatus(StatusType.ACTIVE);
        financialAnalystAccount.setRole(financialAnalystRole);
        staffAccountSessionBean.createAccount(financialAnalystAccount);

        StaffAccount financialOfficerAccount = new StaffAccount();
        financialOfficerAccount.setUsername(ConstantUtils.FINANCIAL_OFFICER_USERNAME);
        financialOfficerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        financialOfficerAccount.setFirstName("Officer");
        financialOfficerAccount.setLastName("Financial");
        financialOfficerAccount.setEmail("financial_officer@merlionbank.com");
        financialOfficerAccount.setStatus(StatusType.ACTIVE);
        financialOfficerAccount.setRole(financialOfficerRole);
        staffAccountSessionBean.createAccount(financialOfficerAccount);

        StaffAccount generalTellerAccount = new StaffAccount();
        generalTellerAccount.setUsername(ConstantUtils.GENERAL_TELLER_USERNAME);
        generalTellerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        generalTellerAccount.setFirstName("General");
        generalTellerAccount.setLastName("Teller");
        generalTellerAccount.setEmail("general_teller@merlionbank.com");
        generalTellerAccount.setStatus(StatusType.ACTIVE);
        generalTellerAccount.setRole(generalTellerRole);
        staffAccountSessionBean.createAccount(generalTellerAccount);

        StaffAccount loanOfficerAccount = new StaffAccount();
        loanOfficerAccount.setUsername(ConstantUtils.LOAN_OFFICIER_USERNAME);
        loanOfficerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        loanOfficerAccount.setFirstName("Loan");
        loanOfficerAccount.setLastName("Officer");
        loanOfficerAccount.setEmail("loan_officer@merlionbank.com");
        loanOfficerAccount.setStatus(StatusType.ACTIVE);
        loanOfficerAccount.setRole(loanOfficerRole);
        staffAccountSessionBean.createAccount(loanOfficerAccount);

        StaffAccount productManagerAccount = new StaffAccount();
        productManagerAccount.setUsername(ConstantUtils.PRODUCT_MANAGER_USERNAME);
        productManagerAccount.setPassword(ConstantUtils.STAFF_DEMO_PASSWORD);
        productManagerAccount.setFirstName("Product");
        productManagerAccount.setLastName("Manager");
        productManagerAccount.setEmail("product_manager@merlionbank.com");
        productManagerAccount.setStatus(StatusType.ACTIVE);
        productManagerAccount.setRole(productManagerRole);
        staffAccountSessionBean.createAccount(productManagerAccount);
    }

    private void initCustomer() {
        String p = HashPwdUtils.hashPwd("password");

        Customer c = new Customer();
        c.setAddress("some fake address"); //make it a bit more real
        c.setBirthDay(new Date()); //make some real birthday.
        c.setEmail("wangzhe.lynx@gmail.com");
        c.setFirstname("Yifan");
        c.setGender(Gender.MALE); // pls modify gender to enum type
        c.setIdentityNumber("S1234567Z");
        c.setIdentityType(IdentityType.NRIC); // same for this to enum type
        c.setIncome(Income.FROM_2000_TO_4000);
        c.setLastname("Chen");
        c.setNationality(Nationality.CHINA); //enum type if possible
        c.setOccupation(Occupation.DIRECTOR);
        c.setPhone("81567758"); //must use real phone number as we need sms code
        c.setPostalCode("654321");
        c.setMainAccount(new MainAccount());
        c.getMainAccount().setUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        c.getMainAccount().setPassword(p);
        c.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c.getMainAccount().setCustomer(c);

        demoMainAccount = newCustomerSessionBean.createCustomer(c).getMainAccount();

        String u2 = "c0000002";
        String p2 = HashPwdUtils.hashPwd("password");

        Customer c2 = new Customer();
        c2.setAddress("19 Tanglin Road, 131-1-1"); //make it a bit more real
        c2.setBirthDay(new Date()); //make some real birthday.
        c2.setEmail("wangzhe.lynx@gmail.com");
        c2.setFirstname("Zhe");
        c2.setGender(Gender.MALE); // pls modify gender to enum type
        c2.setIdentityNumber("S1234223Z");
        c2.setIdentityType(IdentityType.PASSPORT); // same for this to enum type
        c2.setIncome(Income.FROM_4000_TO_6000);
        c2.setLastname("Wang");
        c2.setNationality(Nationality.CHINA_HONG_KONG); //enum type if possible
        c2.setOccupation(Occupation.FREELANCER);
        c2.setPhone("81567712"); //must use real phone number as we need sms code
        c2.setPostalCode("654302");
        c2.setMainAccount(new MainAccount());
        c2.getMainAccount().setUserID(u2);
        c2.getMainAccount().setPassword(p2);
        c2.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c2.getMainAccount().setCustomer(c2);

        newCustomerSessionBean.createCustomer(c2);

        String u3 = "c0000003";
        String p3 = HashPwdUtils.hashPwd("password");

        Customer c3 = new Customer();
        c3.setAddress("9 Thomson Road, 9-1-B"); //make it a bit more real
        c3.setBirthDay(new Date()); //make some real birthday.
        c3.setEmail("leiyang007@gmail.com");
        c3.setFirstname("Yang");
        c3.setGender(Gender.FEMALE); // pls modify gender to enum type
        c3.setIdentityNumber("S1234902Z");
        c3.setIdentityType(IdentityType.PASSPORT); // same for this to enum type
        c3.setIncome(Income.FROM_6000_TO_8000);
        c3.setLastname("Lei");
        c3.setNationality(Nationality.GERMANY); //enum type if possible
        c3.setOccupation(Occupation.MANAGERIAL);
        c3.setPhone("89212758"); //must use real phone number as we need sms code
        c3.setPostalCode("654111");
        c3.setMainAccount(new MainAccount());
        c3.getMainAccount().setUserID(u3);
        c3.getMainAccount().setPassword(p3);
        c3.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c3.getMainAccount().setCustomer(c3);

        newCustomerSessionBean.createCustomer(c3);

        String u4 = "c0000004";
        String p4 = HashPwdUtils.hashPwd("password");

        Customer c4 = new Customer();
        c4.setAddress("3 Sim Lim Avenue, 898B-501"); //make it a bit more real
        c4.setBirthDay(new Date()); //make some real birthday.
        c4.setEmail("sunyuxuan123@gmail.com");
        c4.setFirstname("Yuxuan");
        c4.setGender(Gender.MALE); // pls modify gender to enum type
        c4.setIdentityNumber("S1243267Z");
        c4.setIdentityType(IdentityType.PASSPORT); // same for this to enum type
        c4.setIncome(Income.FROM_8000_TO_10000);
        c4.setLastname("Sun");
        c4.setNationality(Nationality.INDONESIA); //enum type if possible
        c4.setOccupation(Occupation.EXECUTIVE);
        c4.setPhone("81123558"); //must use real phone number as we need sms code
        c4.setPostalCode("621329");
        c4.setMainAccount(new MainAccount());
        c4.getMainAccount().setUserID(u4);
        c4.getMainAccount().setPassword(p4);
        c4.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c4.getMainAccount().setCustomer(c4);

        newCustomerSessionBean.createCustomer(c4);

        String u5 = "c0000005";
        String p5 = HashPwdUtils.hashPwd("password");

        Customer c5 = new Customer();
        c5.setAddress("28 West Coast Road, B-2"); //make it a bit more real
        c5.setBirthDay(new Date()); //make some real birthday.
        c5.setEmail("lilitong01@gmail.com");
        c5.setFirstname("Litong");
        c5.setGender(Gender.MALE); // pls modify gender to enum type
        c5.setIdentityNumber("S1289812Z");
        c5.setIdentityType(IdentityType.PASSPORT); // same for this to enum type
        c5.setIncome(Income.FROM_2000_TO_4000);
        c5.setLastname("Chen");
        c5.setNationality(Nationality.JAPAN); //enum type if possible
        c5.setOccupation(Occupation.MANAGERIAL);
        c5.setPhone("90028125");//must use real phone number as we need sms code
        c5.setPostalCode("001409");
        c5.setMainAccount(new MainAccount());
        c5.getMainAccount().setUserID(u5);
        c5.getMainAccount().setPassword(p5);
        c5.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c5.getMainAccount().setCustomer(c5);

        newCustomerSessionBean.createCustomer(c5);

        String u6 = "c0000006";
        String p6 = HashPwdUtils.hashPwd("password");

        Customer c6 = new Customer();
        c6.setAddress("67 Ang Mo Kio Avenue, 256-1"); //make it a bit more real
        c6.setBirthDay(new Date()); //make some real birthday.
        c6.setEmail("daisyqiu@gmail.com");
        c6.setFirstname("Xiaqing");
        c6.setGender(Gender.MALE); // pls modify gender to enum type
        c6.setIdentityNumber("S1209183Z");
        c6.setIdentityType(IdentityType.PASSPORT); // same for this to enum type
        c6.setIncome(Income.FROM_2000_TO_4000);
        c6.setLastname("Qiu");
        c6.setNationality(Nationality.INDONESIA); //enum type if possible
        c6.setOccupation(Occupation.EXECUTIVE);
        c6.setPhone("81509281"); //must use real phone number as we need sms code
        c6.setPostalCode("118921");
        c6.setMainAccount(new MainAccount());
        c6.getMainAccount().setUserID(u6);
        c6.getMainAccount().setPassword(p6);
        c6.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c6.getMainAccount().setCustomer(c6);

        newCustomerSessionBean.createCustomer(c6);

        String u7 = "c0000007";
        String p7 = HashPwdUtils.hashPwd("password");

        Customer c7 = new Customer();
        c7.setAddress("555 Clementi Road, 419-807"); //make it a bit more real
        c7.setBirthDay(new Date()); //make some real birthday.
        c7.setEmail("daisykoo@gmail.com");
        c7.setFirstname("Daisy");
        c7.setGender(Gender.MALE); // pls modify gender to enum type
        c7.setIdentityNumber("S1290528Z");
        c7.setIdentityType(IdentityType.PASSPORT); // same for this to enum type
        c7.setIncome(Income.FROM_2000_TO_4000);
        c7.setLastname("Koo");
        c7.setNationality(Nationality.INDONESIA); //enum type if possible
        c7.setOccupation(Occupation.TEACHER);
        c7.setPhone("91027903"); //must use real phone number as we need sms code
        c7.setPostalCode("002987");
        c7.setMainAccount(new MainAccount());
        c7.getMainAccount().setUserID(u7);
        c7.getMainAccount().setPassword(p7);
        c7.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c7.getMainAccount().setCustomer(c7);

        newCustomerSessionBean.createCustomer(c7);

        String u8 = "c0000008";
        String p8 = HashPwdUtils.hashPwd("password");

        Customer c8 = new Customer();
        c8.setAddress("9 Hougang Road, 193-303"); //make it a bit more real
        c8.setBirthDay(new Date()); //make some real birthday.
        c8.setEmail("vincentlee@gmail.com");
        c8.setFirstname("Vincent");
        c8.setGender(Gender.MALE); // pls modify gender to enum type
        c8.setIdentityNumber("S12091235Z");
        c8.setIdentityType(IdentityType.PASSPORT); // same for this to enum type
        c8.setIncome(Income.FROM_6000_TO_8000);
        c8.setLastname("Lee");
        c8.setNationality(Nationality.SINGAPOREAN); //enum type if possible
        c8.setOccupation(Occupation.SUPERVISOR);
        c8.setPhone("99910888"); //must use real phone number as we need sms code
        c8.setPostalCode("020988");
        c8.setMainAccount(new MainAccount());
        c8.getMainAccount().setUserID(u8);
        c8.getMainAccount().setPassword(p8);
        c8.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c8.getMainAccount().setCustomer(c8);

        newCustomerSessionBean.createCustomer(c8);

        String u9 = "c0000009";
        String p9 = HashPwdUtils.hashPwd("password");

        Customer c9 = new Customer();
        c9.setAddress("17 South Buona Vista Road, 29-905"); //make it a bit more real
        c9.setBirthDay(new Date()); //make some real birthday.
        c9.setEmail("cassychoi@gmail.com");
        c9.setFirstname("Cassy");
        c9.setGender(Gender.FEMALE); // pls modify gender to enum type
        c9.setIdentityNumber("S1234567Z");
        c9.setIdentityType(IdentityType.NRIC); // same for this to enum type
        c9.setIncome(Income.FROM_6000_TO_8000);
        c9.setLastname("Choi");
        c9.setNationality(Nationality.UNITED_STATES); //enum type if possible
        c9.setOccupation(Occupation.SELF_EMPLOYED);
        c9.setPhone("80031182"); //must use real phone number as we need sms code
        c9.setPostalCode("019090");
        c9.setMainAccount(new MainAccount());
        c9.getMainAccount().setUserID(u9);
        c9.getMainAccount().setPassword(p9);
        c9.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c9.getMainAccount().setCustomer(c9);

        newCustomerSessionBean.createCustomer(c9);
    }

    private void initInterest() {
        Interest i = new Interest();
        i.setName("0.05% basenterest");
        i.setDescription("Merlion Unique Account customer earns at least 0.05% interest rate per year.");
        i.setVersion(0);
        i.setPercentage(new BigDecimal(0.0001));// 0.01%
        demoNormalInterestData = interestSessionBean.addInterest(i);

        // Init other interests
        initTimeRangeInterest();
        initRangeInterest();
        initConditionalInterest();
    }

    private void initRangeInterest() {
        RangeInterest i1 = new RangeInterest();
        i1.setName("First 10,000");
        i1.setVersion(0);
        i1.setMinimum(new BigDecimal(0));
        i1.setMaximum(new BigDecimal(10000));
        i1.setPercentage(new BigDecimal(0.0005));
        demoRangeInterestData.add(interestSessionBean.addInterest(i1));

        RangeInterest i2 = new RangeInterest();
        i2.setName("10,001~100,000");
        i2.setVersion(0);
        i2.setMinimum(new BigDecimal(100001));
        i2.setMaximum(new BigDecimal(100000));
        i2.setPercentage(new BigDecimal(0.00075));
        demoRangeInterestData.add(interestSessionBean.addInterest(i2));

        RangeInterest i3 = new RangeInterest();
        i3.setName("100,001~500,000");
        i3.setVersion(0);
        i3.setMinimum(new BigDecimal(100001));
        i3.setMaximum(new BigDecimal(500000));
        i3.setPercentage(new BigDecimal(0.00075));
        demoRangeInterestData.add(interestSessionBean.addInterest(i3));

        RangeInterest i4 = new RangeInterest();
        i4.setName("Above 500,000");
        i4.setVersion(0);
        i4.setMinimum(new BigDecimal(500000));
        i4.setMaximum(new BigDecimal(Integer.MAX_VALUE));
        i4.setPercentage(new BigDecimal(0.00075));
        demoRangeInterestData.add(interestSessionBean.addInterest(i4));
    }

    private void initTimeRangeInterest() {
        // Add to a fixedDepositAccount
        // https://www.bankbazaar.sg/fixed-deposit/ocbc-fixed-deposit-rate.html
        TimeRangeInterest i1 = new TimeRangeInterest();
        i1.setName("1-2month-$5000-$20000-0.05%");
        i1.setVersion(0);
        i1.setStartMonth(1);
        i1.setEndMonth(2);
        i1.setMinimum(new BigDecimal(5000));
        i1.setMaximum(new BigDecimal(20000));
        i1.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i1);

        TimeRangeInterest i2 = new TimeRangeInterest();
        i2.setName("3-5month-$5000-$20000-0.10%");
        i2.setVersion(0);
        i2.setStartMonth(3);
        i2.setEndMonth(5);
        i2.setMinimum(new BigDecimal(5000));
        i2.setMaximum(new BigDecimal(20000));
        i2.setPercentage(new BigDecimal(0.0010));
        interestSessionBean.addInterest(i2);

        TimeRangeInterest i3 = new TimeRangeInterest();
        i3.setName("6-8month-$5000-$20000-0.15%");
        i3.setVersion(0);
        i3.setStartMonth(6);
        i3.setEndMonth(8);
        i3.setMinimum(new BigDecimal(5000));
        i3.setMaximum(new BigDecimal(20000));
        i3.setPercentage(new BigDecimal(0.0015));
        interestSessionBean.addInterest(i3);

        TimeRangeInterest i4 = new TimeRangeInterest();
        i4.setName("9-11month-$5000-$20000-0.20%");
        i4.setVersion(0);
        i4.setStartMonth(9);
        i4.setEndMonth(11);
        i4.setMinimum(new BigDecimal(5000));
        i4.setMaximum(new BigDecimal(20000));
        i4.setPercentage(new BigDecimal(0.0020));
        interestSessionBean.addInterest(i4);

        TimeRangeInterest i5 = new TimeRangeInterest();
        i5.setName("12-15month-$5000-$20000-0.25%");
        i5.setVersion(0);
        i5.setStartMonth(12);
        i5.setEndMonth(15);
        i5.setMinimum(new BigDecimal(5000));
        i5.setMaximum(new BigDecimal(20000));
        i5.setPercentage(new BigDecimal(0.0025));
        interestSessionBean.addInterest(i5);

        TimeRangeInterest i6 = new TimeRangeInterest();
        i6.setName("16-18month-$5000-$20000-0.50%");
        i6.setVersion(0);
        i6.setStartMonth(16);
        i6.setEndMonth(18);
        i6.setMinimum(new BigDecimal(5000));
        i6.setMaximum(new BigDecimal(20000));
        i6.setPercentage(new BigDecimal(0.0050));
        interestSessionBean.addInterest(i6);

        TimeRangeInterest i7 = new TimeRangeInterest();
        i7.setName("19-24month-$5000-$20000-0.55%");
        i7.setVersion(0);
        i7.setStartMonth(19);
        i7.setEndMonth(24);
        i7.setMinimum(new BigDecimal(5000));
        i7.setMaximum(new BigDecimal(20000));
        i7.setPercentage(new BigDecimal(0.0055));
        interestSessionBean.addInterest(i7);

        TimeRangeInterest i8 = new TimeRangeInterest();
        i8.setName("25-36month-$5000-$20000-0.65%");
        i8.setVersion(0);
        i8.setStartMonth(25);
        i8.setEndMonth(36);
        i8.setMinimum(new BigDecimal(5000));
        i8.setMaximum(new BigDecimal(20000));
        i8.setPercentage(new BigDecimal(0.0065));
        interestSessionBean.addInterest(i8);

        TimeRangeInterest i9 = new TimeRangeInterest();
        i9.setName("1-2month-$20000-$50000-0.05%");
        i9.setVersion(0);
        i9.setStartMonth(1);
        i9.setEndMonth(2);
        i9.setMinimum(new BigDecimal(20000));
        i9.setMaximum(new BigDecimal(50000));
        i9.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i9);

        TimeRangeInterest i10 = new TimeRangeInterest();
        i10.setName("3-5month-$20000-$50000-0.10%");
        i10.setVersion(0);
        i10.setStartMonth(3);
        i10.setEndMonth(5);
        i10.setMinimum(new BigDecimal(20000));
        i10.setMaximum(new BigDecimal(50000));
        i10.setPercentage(new BigDecimal(0.0010));
        interestSessionBean.addInterest(i10);

        TimeRangeInterest i11 = new TimeRangeInterest();
        i11.setName("6-8month-$20000-$50000-0.15%");
        i11.setVersion(0);
        i11.setStartMonth(6);
        i11.setEndMonth(8);
        i11.setMinimum(new BigDecimal(20000));
        i11.setMaximum(new BigDecimal(50000));
        i11.setPercentage(new BigDecimal(0.0015));
        interestSessionBean.addInterest(i11);

        TimeRangeInterest i12 = new TimeRangeInterest();
        i12.setName("9-11month-$20000-$50000-0.20%");
        i12.setVersion(0);
        i12.setStartMonth(9);
        i12.setEndMonth(11);
        i12.setMinimum(new BigDecimal(20000));
        i12.setMaximum(new BigDecimal(50000));
        i12.setPercentage(new BigDecimal(0.0020));
        interestSessionBean.addInterest(i12);

        TimeRangeInterest i13 = new TimeRangeInterest();
        i13.setName("12-15month-$20000-$50000-0.25%");
        i13.setVersion(0);
        i13.setStartMonth(12);
        i13.setEndMonth(15);
        i13.setMinimum(new BigDecimal(20000));
        i13.setMaximum(new BigDecimal(50000));
        i13.setPercentage(new BigDecimal(0.0025));
        interestSessionBean.addInterest(i13);

        TimeRangeInterest i14 = new TimeRangeInterest();
        i14.setName("16-18month-$20000-$50000-0.50%");
        i14.setVersion(0);
        i14.setStartMonth(16);
        i14.setEndMonth(18);
        i14.setMinimum(new BigDecimal(20000));
        i14.setMaximum(new BigDecimal(50000));
        i14.setPercentage(new BigDecimal(0.0050));
        interestSessionBean.addInterest(i14);

        TimeRangeInterest i15 = new TimeRangeInterest();
        i15.setName("19-24month-$20000-$50000-0.55%");
        i15.setVersion(0);
        i15.setStartMonth(19);
        i15.setEndMonth(24);
        i15.setMinimum(new BigDecimal(20000));
        i15.setMaximum(new BigDecimal(50000));
        i15.setPercentage(new BigDecimal(0.0055));
        interestSessionBean.addInterest(i15);

        TimeRangeInterest i16 = new TimeRangeInterest();
        i16.setName("25-36month-$20000-$50000-0.65%");
        i16.setVersion(0);
        i16.setStartMonth(25);
        i16.setEndMonth(36);
        i16.setMinimum(new BigDecimal(20000));
        i16.setMaximum(new BigDecimal(50000));
        i16.setPercentage(new BigDecimal(0.0065));
        interestSessionBean.addInterest(i16);

        TimeRangeInterest i17 = new TimeRangeInterest();
        i17.setName("1-2month-$50000-$99999-0.05%");
        i17.setVersion(0);
        i17.setStartMonth(1);
        i17.setEndMonth(2);
        i17.setMinimum(new BigDecimal(50000));
        i17.setMaximum(new BigDecimal(99999));
        i17.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i17);

        TimeRangeInterest i18 = new TimeRangeInterest();
        i18.setName("3-5month-$50000-$99999-0.10%");
        i18.setVersion(0);
        i18.setStartMonth(3);
        i18.setEndMonth(5);
        i18.setMinimum(new BigDecimal(50000));
        i18.setMaximum(new BigDecimal(90000));
        i18.setPercentage(new BigDecimal(0.0010));
        interestSessionBean.addInterest(i18);

        TimeRangeInterest i19 = new TimeRangeInterest();
        i19.setName("6-8month-$50000-$99999-0.15%");
        i19.setVersion(0);
        i19.setStartMonth(6);
        i19.setEndMonth(8);
        i19.setMinimum(new BigDecimal(50000));
        i19.setMaximum(new BigDecimal(99999));
        i19.setPercentage(new BigDecimal(0.0015));
        interestSessionBean.addInterest(i19);

        TimeRangeInterest i20 = new TimeRangeInterest();
        i20.setName("9-11month-$50000-$99999-0.20%");
        i20.setVersion(0);
        i20.setStartMonth(9);
        i20.setEndMonth(11);
        i20.setMinimum(new BigDecimal(50000));
        i20.setMaximum(new BigDecimal(99999));
        i20.setPercentage(new BigDecimal(0.0020));
        interestSessionBean.addInterest(i20);

        TimeRangeInterest i21 = new TimeRangeInterest();
        i21.setName("12-15month-$50000-$99999-0.25%");
        i21.setVersion(0);
        i21.setStartMonth(12);
        i21.setEndMonth(15);
        i21.setMinimum(new BigDecimal(50000));
        i21.setMaximum(new BigDecimal(99999));
        i21.setPercentage(new BigDecimal(0.0025));
        interestSessionBean.addInterest(i21);

        TimeRangeInterest i22 = new TimeRangeInterest();
        i22.setName("16-18month-$50000-$99999-0.50%");
        i22.setVersion(0);
        i22.setStartMonth(16);
        i22.setEndMonth(18);
        i22.setMinimum(new BigDecimal(50000));
        i22.setMaximum(new BigDecimal(99999));
        i22.setPercentage(new BigDecimal(0.0050));
        interestSessionBean.addInterest(i22);

        TimeRangeInterest i23 = new TimeRangeInterest();
        i23.setName("19-24month-$50000-$99999-0.55%");
        i23.setVersion(0);
        i23.setStartMonth(19);
        i23.setEndMonth(24);
        i23.setMinimum(new BigDecimal(50000));
        i23.setMaximum(new BigDecimal(99999));
        i23.setPercentage(new BigDecimal(0.0055));
        interestSessionBean.addInterest(i23);

        TimeRangeInterest i24 = new TimeRangeInterest();
        i24.setName("25-36month-$50000-$99999-0.65%");
        i24.setVersion(0);
        i24.setStartMonth(25);
        i24.setEndMonth(36);
        i24.setMinimum(new BigDecimal(50000));
        i24.setMaximum(new BigDecimal(99999));
        i24.setPercentage(new BigDecimal(0.0065));
        interestSessionBean.addInterest(i24);

        TimeRangeInterest i25 = new TimeRangeInterest();
        i25.setName("1-2month-$100000-$249999-0.05%");
        i25.setVersion(0);
        i25.setStartMonth(1);
        i25.setEndMonth(2);
        i25.setMinimum(new BigDecimal(100000));
        i25.setMaximum(new BigDecimal(249999));
        i25.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i25);

        TimeRangeInterest i26 = new TimeRangeInterest();
        i26.setName("3-5month-$100000-$249999-0.10%");
        i26.setVersion(0);
        i26.setStartMonth(3);
        i26.setEndMonth(5);
        i26.setMinimum(new BigDecimal(100000));
        i26.setMaximum(new BigDecimal(249999));
        i26.setPercentage(new BigDecimal(0.0010));
        interestSessionBean.addInterest(i26);

        TimeRangeInterest i27 = new TimeRangeInterest();
        i27.setName("6-8month-$100000-$249999-0.15%");
        i27.setVersion(0);
        i27.setStartMonth(6);
        i27.setEndMonth(8);
        i27.setMinimum(new BigDecimal(100000));
        i27.setMaximum(new BigDecimal(249999));
        i27.setPercentage(new BigDecimal(0.0015));
        interestSessionBean.addInterest(i27);

        TimeRangeInterest i28 = new TimeRangeInterest();
        i28.setName("9-11month-$100000-$249999-0.20%");
        i28.setVersion(0);
        i28.setStartMonth(9);
        i28.setEndMonth(11);
        i28.setMinimum(new BigDecimal(100000));
        i28.setMaximum(new BigDecimal(249999));
        i28.setPercentage(new BigDecimal(0.0020));
        interestSessionBean.addInterest(i28);

        TimeRangeInterest i29 = new TimeRangeInterest();
        i29.setName("12-15month-$100000-$249999-0.25%");
        i29.setVersion(0);
        i29.setStartMonth(12);
        i29.setEndMonth(15);
        i29.setMinimum(new BigDecimal(100000));
        i29.setMaximum(new BigDecimal(249999));
        i29.setPercentage(new BigDecimal(0.0025));
        interestSessionBean.addInterest(i29);

        TimeRangeInterest i30 = new TimeRangeInterest();
        i30.setName("16-18month-$100000-$249999-0.50%");
        i30.setVersion(0);
        i30.setStartMonth(16);
        i30.setEndMonth(18);
        i30.setMinimum(new BigDecimal(100000));
        i30.setMaximum(new BigDecimal(249999));
        i30.setPercentage(new BigDecimal(0.0050));
        interestSessionBean.addInterest(i30);

        TimeRangeInterest i31 = new TimeRangeInterest();
        i31.setName("19-24month-$100000-$249999-0.55%");
        i31.setVersion(0);
        i31.setStartMonth(19);
        i31.setEndMonth(24);
        i31.setMinimum(new BigDecimal(100000));
        i31.setMaximum(new BigDecimal(249999));
        i31.setPercentage(new BigDecimal(0.0055));
        interestSessionBean.addInterest(i31);

        TimeRangeInterest i32 = new TimeRangeInterest();
        i32.setName("25-36month-$100000-$249999-0.65%");
        i32.setVersion(0);
        i32.setStartMonth(25);
        i32.setEndMonth(36);
        i32.setMinimum(new BigDecimal(100000));
        i32.setMaximum(new BigDecimal(249999));
        i32.setPercentage(new BigDecimal(0.0065));
        interestSessionBean.addInterest(i32);

        TimeRangeInterest i33 = new TimeRangeInterest();
        i33.setName("1-2month-$250000-$499999-0.05%");
        i33.setVersion(0);
        i33.setStartMonth(1);
        i33.setEndMonth(2);
        i33.setMinimum(new BigDecimal(250000));
        i33.setMaximum(new BigDecimal(499999));
        i33.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i33);

        TimeRangeInterest i34 = new TimeRangeInterest();
        i34.setName("3-5month-$250000-$499999-0.10%");
        i34.setVersion(0);
        i34.setStartMonth(3);
        i34.setEndMonth(5);
        i34.setMinimum(new BigDecimal(250000));
        i34.setMaximum(new BigDecimal(499999));
        i34.setPercentage(new BigDecimal(0.0010));
        interestSessionBean.addInterest(i34);

        TimeRangeInterest i35 = new TimeRangeInterest();
        i35.setName("6-8month-$250000-$499999-0.15%");
        i35.setVersion(0);
        i35.setStartMonth(6);
        i35.setEndMonth(8);
        i35.setMinimum(new BigDecimal(250000));
        i35.setMaximum(new BigDecimal(499999));
        i35.setPercentage(new BigDecimal(0.0015));
        interestSessionBean.addInterest(i35);

        TimeRangeInterest i36 = new TimeRangeInterest();
        i36.setName("9-11month-$250000-$499999-0.20%");
        i36.setVersion(0);
        i36.setStartMonth(9);
        i36.setEndMonth(11);
        i36.setMinimum(new BigDecimal(250000));
        i36.setMaximum(new BigDecimal(499999));
        i36.setPercentage(new BigDecimal(0.0020));
        interestSessionBean.addInterest(i36);

        TimeRangeInterest i37 = new TimeRangeInterest();
        i37.setName("12-15month-$250000-$499999-0.25%");
        i37.setVersion(0);
        i37.setStartMonth(12);
        i37.setEndMonth(15);
        i37.setMinimum(new BigDecimal(250000));
        i37.setMaximum(new BigDecimal(499999));
        i37.setPercentage(new BigDecimal(0.0025));
        interestSessionBean.addInterest(i37);

        TimeRangeInterest i38 = new TimeRangeInterest();
        i38.setName("16-18month-$250000-$499999-0.50%");
        i38.setVersion(0);
        i38.setStartMonth(16);
        i38.setEndMonth(18);
        i38.setMinimum(new BigDecimal(250000));
        i38.setMaximum(new BigDecimal(499999));
        i38.setPercentage(new BigDecimal(0.0050));
        interestSessionBean.addInterest(i38);

        TimeRangeInterest i39 = new TimeRangeInterest();
        i39.setName("19-24month-$250000-$499999-0.55%");
        i39.setVersion(0);
        i39.setStartMonth(19);
        i39.setEndMonth(24);
        i39.setMinimum(new BigDecimal(250000));
        i39.setMaximum(new BigDecimal(499999));
        i39.setPercentage(new BigDecimal(0.0055));
        interestSessionBean.addInterest(i39);

        TimeRangeInterest i40 = new TimeRangeInterest();
        i40.setName("25-36month-$250000-$499999-0.65%");
        i40.setVersion(0);
        i40.setStartMonth(25);
        i40.setEndMonth(36);
        i40.setMinimum(new BigDecimal(250000));
        i40.setMaximum(new BigDecimal(499999));
        i40.setPercentage(new BigDecimal(0.0065));
        interestSessionBean.addInterest(i40);

        TimeRangeInterest i41 = new TimeRangeInterest();
        i41.setName("1-2month-$500000-$XXXXXX-0.05%");
        i41.setVersion(0);
        i41.setStartMonth(1);
        i41.setEndMonth(2);
        i41.setMinimum(new BigDecimal(500000));
        i41.setMaximum(new BigDecimal(999999));
        i41.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i41);

        TimeRangeInterest i42 = new TimeRangeInterest();
        i42.setName("3-5month-$500000-$XXXXXX-0.10%");
        i42.setVersion(0);
        i42.setStartMonth(3);
        i42.setEndMonth(5);
        i42.setMinimum(new BigDecimal(500000));
        i42.setMaximum(new BigDecimal(999999));
        i42.setPercentage(new BigDecimal(0.0010));
        interestSessionBean.addInterest(i42);

        TimeRangeInterest i43 = new TimeRangeInterest();
        i43.setName("6-8month-$500000-$XXXXXX-0.15%");
        i43.setVersion(0);
        i43.setStartMonth(6);
        i43.setEndMonth(8);
        i43.setMinimum(new BigDecimal(500000));
        i43.setMaximum(new BigDecimal(999999));
        i43.setPercentage(new BigDecimal(0.0015));
        interestSessionBean.addInterest(i43);

        TimeRangeInterest i44 = new TimeRangeInterest();
        i44.setName("9-11month-$500000-$XXXXXX-0.20%");
        i44.setVersion(0);
        i44.setStartMonth(9);
        i44.setEndMonth(11);
        i44.setMinimum(new BigDecimal(500000));
        i44.setMaximum(new BigDecimal(999999));
        i44.setPercentage(new BigDecimal(0.0020));
        interestSessionBean.addInterest(i44);

        TimeRangeInterest i45 = new TimeRangeInterest();
        i45.setName("12-15month-$500000-$XXXXXX-0.25%");
        i45.setVersion(0);
        i45.setStartMonth(12);
        i45.setEndMonth(15);
        i45.setMinimum(new BigDecimal(500000));
        i45.setMaximum(new BigDecimal(999999));
        i45.setPercentage(new BigDecimal(0.0025));
        interestSessionBean.addInterest(i45);

        TimeRangeInterest i46 = new TimeRangeInterest();
        i46.setName("16-18month-$500000-$XXXXXX-0.50%");
        i46.setVersion(0);
        i46.setStartMonth(16);
        i46.setEndMonth(18);
        i46.setMinimum(new BigDecimal(500000));
        i46.setMaximum(new BigDecimal(999999));
        i46.setPercentage(new BigDecimal(0.0050));
        interestSessionBean.addInterest(i46);

        TimeRangeInterest i47 = new TimeRangeInterest();
        i47.setName("19-24month-$500000-$XXXXXX-0.55%");
        i47.setVersion(0);
        i47.setStartMonth(19);
        i47.setEndMonth(24);
        i47.setMinimum(new BigDecimal(500000));
        i47.setMaximum(new BigDecimal(999999));
        i47.setPercentage(new BigDecimal(0.0055));
        interestSessionBean.addInterest(i47);

        TimeRangeInterest i48 = new TimeRangeInterest();
        i48.setName("25-36month-$500000-$XXXXXX-0.65%");
        i48.setVersion(0);
        i48.setStartMonth(25);
        i48.setEndMonth(36);
        i48.setMinimum(new BigDecimal(500000));
        i48.setMaximum(new BigDecimal(999999));
        i48.setPercentage(new BigDecimal(0.0065));
        interestSessionBean.addInterest(i48);
    }

    private void initConditionalInterest() {
        // Credit your salary of at least $2,000 through GIRO
        ConditionInterest ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Credit Salary");
        ci.setDescription("Get 1.2% Bonus by crediting salary of at least S$3000 through GIRO");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.SALARY);
        ci.setAmount(new BigDecimal(2000));
        ci.setPercentage(new BigDecimal(0.012));
        demoConditionalInterestDataForCustomDepositProduct.add(interestSessionBean.addInterest(ci));
        // Pay any 3 bills online or through GIRO
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Pay Bill");
        ci.setDescription("Get 0.5% Bonus by paying at least three bills online");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.BILL);
        ci.setAmount(new BigDecimal(3));
        ci.setPercentage(new BigDecimal(0.005));
        demoConditionalInterestDataForCustomDepositProduct.add(interestSessionBean.addInterest(ci));
        // Spend at least $500 on OCBC Credit Cards
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Credit Card Spend");
        ci.setDescription("Get 0.5% Bonus by spending at least S$600 on Merlion credit card");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.CCSPENDING);
        ci.setAmount(new BigDecimal(500));
        ci.setPercentage(new BigDecimal(0.005));
        demoConditionalInterestDataForCustomDepositProduct.add(interestSessionBean.addInterest(ci));
        // Insure or Invest and get this bonus for 12 months
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Invest");
        ci.setDescription("Get 1% Bonus by investing in our financial products for 12 months");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.INVEST);
        ci.setPercentage(new BigDecimal(0.01));
        ci.setBenefitMonths(12);
        demoConditionalInterestDataForCustomDepositProduct.add(interestSessionBean.addInterest(ci));
        // Increase your account balance from the previous month's balance
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Increase Balance");
        ci.setDescription("Get 1% Bonus By Increasing amount from the previous month’s balance");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.INCREASE);
        ci.setPercentage(new BigDecimal(0.01));
        demoConditionalInterestDataForCustomDepositProduct.add(interestSessionBean.addInterest(ci));
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " No Withdraw");
        ci.setDescription("If the customer does not withdraw any money and he/she deposits at least S$50 in a month, he/she will earn bonus interest rate 0.35%.");
        ci.setVersion(0);
        ci.setAmount(new BigDecimal(50));
        ci.setConditionType(EnumUtils.InterestConditionType.NOWITHDRAW);
        ci.setPercentage(new BigDecimal(0.0035));
        demoConditionalInterestDataForSavingsDepositProduct.add(interestSessionBean.addInterest(ci));
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " No Withdraw +");
        ci.setDescription("If the customer does not withdraw any money and maintain the minimum balance of S$3,000 every day, then he/she will earn the bonus interest rate of 0.75%.");
        ci.setVersion(0);
        ci.setAmount(new BigDecimal(3000));
        ci.setConditionType(EnumUtils.InterestConditionType.NOWITHDRAW);
        ci.setPercentage(new BigDecimal(0.0075));
        demoConditionalInterestDataForSavingsDepositProduct.add(interestSessionBean.addInterest(ci));
    }

    private void initDepositProducts() {
        DepositAccountProduct customProduct = new DepositAccountProduct();
        customProduct.setType(DepositAccountType.CUSTOM);
        customProduct.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME);
        customProduct.setVersion(0);
        customProduct.setInitialDeposit(new BigDecimal(1000));
        customProduct.setMinBalance(new BigDecimal(3000));
        customProduct.setCharges(new BigDecimal(2));
        customProduct.setAnnualFees(BigDecimal.ZERO);
        customProduct.setWaivedMonths(12);
        customProduct.setInterestRules(demoConditionalInterestDataForCustomDepositProduct);
        customProduct.addInterest(demoNormalInterestData);
        depositProductSessionBean.createDepositProduct(customProduct);

        DepositAccountProduct savingProduct = new DepositAccountProduct();
        savingProduct.setType(DepositAccountType.SAVING);
        savingProduct.setName("Monthly Savings Account");
        savingProduct.setVersion(0);
        savingProduct.setInitialDeposit(new BigDecimal(1000));
        savingProduct.setMinBalance(BigDecimal.ZERO);
        savingProduct.setCharges(BigDecimal.ZERO);
        savingProduct.setAnnualFees(BigDecimal.ZERO);
        savingProduct.setWaivedMonths(0);
        savingProduct.addInterest(demoConditionalInterestDataForSavingsDepositProduct.get(0));
        savingProduct.addInterest(demoNormalInterestData);
        depositProductSessionBean.createDepositProduct(savingProduct);

        savingProduct = new DepositAccountProduct();
        savingProduct.setType(DepositAccountType.SAVING);
        savingProduct.setName("Bonus+Savings");
        savingProduct.setVersion(0);
        savingProduct.setInitialDeposit(new BigDecimal(10000));
        savingProduct.setMinBalance(BigDecimal.ZERO);
        savingProduct.setCharges(BigDecimal.ZERO);
        savingProduct.setAnnualFees(BigDecimal.ZERO);
        savingProduct.setWaivedMonths(0);
        savingProduct.addInterest(demoConditionalInterestDataForSavingsDepositProduct.get(1));
        savingProduct.addInterest(demoNormalInterestData);
        depositProductSessionBean.createDepositProduct(savingProduct);

        DepositAccountProduct currentProduct = new DepositAccountProduct();
        currentProduct.setType(DepositAccountType.CURRENT);
        currentProduct.setName("Current Account");
        currentProduct.setVersion(0);
        currentProduct.setInitialDeposit(BigDecimal.ZERO);
        currentProduct.setMinBalance(BigDecimal.ZERO);
        currentProduct.setCharges(BigDecimal.ZERO);
        currentProduct.setAnnualFees(BigDecimal.ZERO);
        currentProduct.setWaivedMonths(0);
        currentProduct.setInterestRules(demoRangeInterestData);
        depositProductSessionBean.createDepositProduct(currentProduct);

        FixedDepositAccountProduct fixedProduct = new FixedDepositAccountProduct();
        fixedProduct.setType(DepositAccountType.FIXED);
        fixedProduct.setName("Time Deposit");
        fixedProduct.setVersion(0);
        fixedProduct.setTerms("Any early withdraw will stop interests immediately");
        fixedProduct.setMaxAmount(new BigDecimal(999999));
        fixedProduct.setMinAmount(new BigDecimal(5000));
        fixedProduct.setMaximumMaturityMonths(36);
        fixedProduct.setMinimumMaturityMonths(1);
        depositProductSessionBean.createDepositProduct(fixedProduct);
    }

    private void initDepositAccounts() {
        initDepositAccount();
    }

    // custom account for demo
    private void initDepositAccount() {
        CustomerDepositAccount customAccount = new CustomerDepositAccount();
        customAccount.setType(DepositAccountType.CUSTOM);
        customAccount.setProduct(depositProductSessionBean.getDepositProductByName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME));
        customAccount.setBalance(new BigDecimal(1000));
        customAccount.setMainAccount(demoMainAccount);
        DepositAccount dp = customerDepositSessionBean.createAccount(customAccount);
        initTransactions(dp);
        
        CustomerDepositAccount savingAccount = new CustomerDepositAccount();
        savingAccount.setType(DepositAccountType.SAVING);
        savingAccount.setProduct(depositProductSessionBean.getDepositProductByName(ConstantUtils.DEMO_SAVING1_DEPOSIT_PRODUCT_NAME));
        savingAccount.setBalance(new BigDecimal(1000));
        savingAccount.setMainAccount(demoMainAccount);
        customerDepositSessionBean.createAccount(savingAccount);
        
        CustomerDepositAccount savingAccount2 = new CustomerDepositAccount();
        savingAccount2.setType(DepositAccountType.SAVING);
        savingAccount2.setProduct(depositProductSessionBean.getDepositProductByName(ConstantUtils.DEMO_SAVING2_DEPOSIT_PRODUCT_NAME));
        savingAccount2.setBalance(new BigDecimal(1000));
        savingAccount2.setMainAccount(demoMainAccount);
        customerDepositSessionBean.createAccount(savingAccount2);
        
        CustomerDepositAccount currentAccount = new CustomerDepositAccount();
        currentAccount.setType(DepositAccountType.SAVING);
        currentAccount.setProduct(depositProductSessionBean.getDepositProductByName(ConstantUtils.DEMO_CURRENT_DEPOSIT_PRODUCT_NAME));
        currentAccount.setBalance(new BigDecimal(1000));
        currentAccount.setMainAccount(demoMainAccount);
        customerDepositSessionBean.createAccount(currentAccount);
        
        CustomerFixedDepositAccount fixedAccount = new CustomerFixedDepositAccount();
        fixedAccount.setType(DepositAccountType.FIXED);
        fixedAccount.setProduct(depositProductSessionBean.getDepositProductByName(ConstantUtils.DEMO_FIXED_DEPOSIT_PRODUCT_NAME));
        fixedAccount.setBalance(new BigDecimal(750000));
        fixedAccount.setMainAccount(demoMainAccount);
        customerDepositSessionBean.createAccount(fixedAccount);
    }

    private void initTransactions(DepositAccount account) {
        DepositAccount da = account;
        for (int i = 1; i < 20; i++) {
            da = customerDepositSessionBean.depositIntoAccount(da, new BigDecimal(500 * i));
        }
        // credit salary of at least 2000
        da = customerDepositSessionBean.creditSalaryIntoAccount(da, new BigDecimal(2000));
        // pay 3 bills
        da = customerDepositSessionBean.payBillFromAccount(da, new BigDecimal(45));
        da = customerDepositSessionBean.payBillFromAccount(da, new BigDecimal(45));
        da = customerDepositSessionBean.payBillFromAccount(da, new BigDecimal(45));
        // credit card spending at least 500
        da = customerDepositSessionBean.ccSpendingFromAccount(da, new BigDecimal(500));
        // invest once and for a year
        da = customerDepositSessionBean.investFromAccount(da, new BigDecimal(5000));
    }

    public void initCase() {
        CustomerCase cc = new CustomerCase();
        Issue issue = new Issue();
        List<Issue> issues = new ArrayList<Issue>();

        issue.setTitle("Deposit Account Problem");
        issue.setField(EnumUtils.IssueField.DEPOSIT);
        issue.setDetails("My deposit account has some suspicious credit histories. Could you please help me to check?");
        issue.setCustomerCase(cc);

        issues.add(issue);

        cc.setIssues(issues);
        cc.setTitle("My Deposit Account has Some problems");
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
        cc.setMainAccount(demoMainAccount);
        cc.setStaffAccount(staffAccountSessionBean.getAccountByUsername(ConstantUtils.SUPER_ADMIN_USERNAME));
        cc.setCaseStatus(CaseStatus.ONHOLD);

        customerCaseSessionBean.saveCase(cc);
    }
}
