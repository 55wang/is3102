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
import entity.dams.account.DepositAccount;
import entity.dams.account.DepositAccountProduct;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
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
    private List<Interest> demoConditionalInterestData = new ArrayList<>();
    private MainAccount demoMainAccount;

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
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
    }

    // Use Super Admin Account as a flag
    private Boolean needInit() {
        StaffAccount sa = staffAccountSessionBean.getAccountByUsername(ConstantUtils.SUPER_ADMIN_USERNAME);
        return sa == null;
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
        financialOfficerRole.setDescription("Offer wealth management services to the customers\n" +
"(Offering wealth services but do not design wealth products)");
        financialOfficerRole = staffRoleSessionBean.addRole(financialOfficerRole); 
        Role generalTellerRole = new Role(EnumUtils.UserRole.GENERAL_TELLER.toString());
        generalTellerRole = staffRoleSessionBean.addRole(generalTellerRole); 
        Role loanOfficerRole = new Role(EnumUtils.UserRole.LOAN_OFFICIER.toString());
        loanOfficerRole.setDescription("Provide loan-related services to the customers\n" +
"(Provide service but do not design loan products)");
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
        c.setGender("MALE"); // pls modify gender to enum type
        c.setIdentityNumber("S1234567Z");
        c.setIdentityType("CITIZEN"); // same for this to enum type
        c.setIncome(5000);
        c.setLastname("Chen");
        c.setNationality("Singapore"); //enum type if possible
        c.setOccupation("programmer");
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
        c2.setGender("MALE"); // pls modify gender to enum type
        c2.setIdentityNumber("S1234223Z");
        c2.setIdentityType("CITIZEN"); // same for this to enum type
        c2.setIncome(5000);
        c2.setLastname("Wang");
        c2.setNationality("Singaporean"); //enum type if possible
        c2.setOccupation("programmer");
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
        c3.setGender("MALE"); // pls modify gender to enum type
        c3.setIdentityNumber("S1234902Z");
        c3.setIdentityType("CITIZEN"); // same for this to enum type
        c3.setIncome(5000);
        c3.setLastname("Lei");
        c3.setNationality("Singaporean"); //enum type if possible
        c3.setOccupation("programmer");
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
        c4.setGender("MALE"); // pls modify gender to enum type
        c4.setIdentityNumber("S1243267Z");
        c4.setIdentityType("CITIZEN"); // same for this to enum type
        c4.setIncome(5000);
        c4.setLastname("Sun");
        c4.setNationality("Singaporean"); //enum type if possible
        c4.setOccupation("programmer");
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
        c5.setGender("FEMALE"); // pls modify gender to enum type
        c5.setIdentityNumber("S1289812Z");
        c5.setIdentityType("CITIZEN"); // same for this to enum type
        c5.setIncome(5000);
        c5.setLastname("Chen");
        c5.setNationality("Singaporean"); //enum type if possible
        c5.setOccupation("programmer");
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
        c6.setGender("FEMALE"); // pls modify gender to enum type
        c6.setIdentityNumber("S1209183Z");
        c6.setIdentityType("CITIZEN"); // same for this to enum type
        c6.setIncome(5000);
        c6.setLastname("Qiu");
        c6.setNationality("Singaporean"); //enum type if possible
        c6.setOccupation("programmer");
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
        c7.setGender("FEMALE"); // pls modify gender to enum type
        c7.setIdentityNumber("S1290528Z");
        c7.setIdentityType("CITIZEN"); // same for this to enum type
        c7.setIncome(5000);
        c7.setLastname("Koo");
        c7.setNationality("Singaporean"); //enum type if possible
        c7.setOccupation("programmer");
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
        c8.setGender("MALE"); // pls modify gender to enum type
        c8.setIdentityNumber("S12091235Z");
        c8.setIdentityType("CITIZEN"); // same for this to enum type
        c8.setIncome(5000);
        c8.setLastname("Lee");
        c8.setNationality("Singaporean"); //enum type if possible
        c8.setOccupation("programmer");
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
        c9.setGender("FEMALE"); // pls modify gender to enum type
        c9.setIdentityNumber("S1234567Z");
        c9.setIdentityType("CITIZEN"); // same for this to enum type
        c9.setIncome(5000);
        c9.setLastname("Choi");
        c9.setNationality("Singaporean"); //enum type if possible
        c9.setOccupation("programmer");
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
        i.setName("Normal Interest");
        i.setVersion(0);
        i.setPercentage(new BigDecimal(0.0001));// 0.01%
        demoNormalInterestData = interestSessionBean.addInterest(i);

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
        i.setStartMonth(1);
        i.setEndMonth(2);
        i.setMinimum(new BigDecimal(5000));
        i.setMaximum(new BigDecimal(20000));
        i.setPercentage(new BigDecimal(0.0005));
        interestSessionBean.addInterest(i);
    }

    private void initConditionalInterest() {
        // Credit your salary of at least $2,000 through GIRO
        ConditionInterest ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Credit Salary");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.SALARY);
        ci.setAmount(new BigDecimal(2000));
        ci.setPercentage(new BigDecimal(0.012));
        demoConditionalInterestData.add(interestSessionBean.addInterest(ci));
        // Pay any 3 bills online or through GIRO
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Pay Bill");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.BILL);
        ci.setAmount(new BigDecimal(3));
        ci.setPercentage(new BigDecimal(0.005));
        demoConditionalInterestData.add(interestSessionBean.addInterest(ci));
        // Spend at least $500 on OCBC Credit Cards
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Credit Card Spend");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.CCSPENDING);
        ci.setAmount(new BigDecimal(500));
        ci.setPercentage(new BigDecimal(0.005));
        demoConditionalInterestData.add(interestSessionBean.addInterest(ci));
        // Insure or Invest and get this bonus for 12 months
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Invest");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.INVEST);
        ci.setPercentage(new BigDecimal(0.01));
        ci.setBenefitMonths(12);
        demoConditionalInterestData.add(interestSessionBean.addInterest(ci));
        // Increase your account balance from the previous month's balance
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " Increase Balance");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.INCREASE);
        ci.setPercentage(new BigDecimal(0.01));
        demoConditionalInterestData.add(interestSessionBean.addInterest(ci));
        ci = new ConditionInterest();
        ci.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME + " No Withdraw");
        ci.setVersion(0);
        ci.setConditionType(EnumUtils.InterestConditionType.NOWITHDRAW);
        ci.setPercentage(new BigDecimal(0.01));
        demoConditionalInterestData.add(interestSessionBean.addInterest(ci));
    }

    private void initDepositProducts() {
        DepositAccountProduct dr = new DepositAccountProduct();
        dr.setType(DepositAccountType.CUSTOM);
        dr.setName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME);
        dr.setVersion(0);
        dr.setInitialDeposit(new BigDecimal(1000));
        dr.setMinBalance(new BigDecimal(3000));
        dr.setCharges(new BigDecimal(2));
        dr.setAnnualFees(BigDecimal.ZERO);
        dr.setWaivedMonths(12);
        dr.setInterestRules(demoConditionalInterestData);
        dr.addInterest(demoNormalInterestData);
        depositProductSessionBean.createDepositProduct(dr);
    }

    private void initDepositAccounts() {
        initDepositAccount();
    }

    // custom account for demo
    private void initDepositAccount() {
        CustomerDepositAccount cda = new CustomerDepositAccount();
        cda.setType(DepositAccountType.CUSTOM);
        cda.setProduct(depositProductSessionBean.getDepositProductByName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME));
        cda.setBalance(new BigDecimal(1000));
        cda.setMainAccount(demoMainAccount);
        DepositAccount dp = customerDepositSessionBean.createAccount(cda);
        initTransactions(dp);
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

    public void initCase(){
        CustomerCase cc = new CustomerCase();
        Issue issue = new Issue();
        List<Issue> issues = new ArrayList<Issue>();
        
        issue.setTitle("Deposit Account Problem");
        issue.setField("Deposit Account issue");
        issue.setDetails("My deposit account has some suspicious credit histories. Could you please help me to check?");
        issue.setCustomerCase(cc);
        
        issues.add(issue);
        
        cc.setIssues(issues);
        cc.setTitle("My Deposit Account has Some problems");
        cc.setMainAccount(demoMainAccount);
        cc.setStaffAccount(staffAccountSessionBean.getAccountByUsername(ConstantUtils.SUPER_ADMIN_USERNAME));
        cc.setCaseStatus(CaseStatus.ONHOLD);
        
        customerCaseSessionBean.saveCase(cc);
    }
    /**
     * @return the demoConditionalInterestData
     */
    public List<Interest> getDemoConditionalInterestData() {
        return demoConditionalInterestData;
    }

    /**
     * @param demoConditionalInterestData the demoConditionalInterestData to set
     */
    public void setDemoConditionalInterestData(List<Interest> demoConditionalInterestData) {
        this.demoConditionalInterestData = demoConditionalInterestData;
    }
}
