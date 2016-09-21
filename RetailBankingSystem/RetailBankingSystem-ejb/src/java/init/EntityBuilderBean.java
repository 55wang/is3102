/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import BatchProcess.InterestAccrualSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import entity.customer.Customer;
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
import utils.ConstantUtils;
import utils.EnumUtils;
import utils.EnumUtils.DepositAccountType;
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
    private InterestSessionBeanLocal interestSessionBean;
    @EJB
    private CustomerDepositSessionBeanLocal customerDepositSessionBean;
    @EJB
    private InterestAccrualSessionBeanLocal interestAccrualSessionBean;
    @EJB
    private DepositProductSessionBeanLocal depositProductSessionBean;
    
    private List<Interest> demoConditionalInterestData = new ArrayList<>();

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
            // Get Product
            DepositAccount da = customerDepositSessionBean.getAccountFromId(1L);
            // Get Interest
            List<Interest> interests = ((DepositAccountProduct)da.getProduct()).getInterestRules();
            for (Interest i : interests) {
                if (i instanceof ConditionInterest) {
                    System.out.print(interestAccrualSessionBean.isAccountMeetCondition(da, (ConditionInterest)i));
                }
            }
        }
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
        initDepositProducts();
        initDepositAccounts();
    }

    private void initCustomer() {
        String u = "c1234567";
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
        c.getMainAccount().setUserID(u);
        c.getMainAccount().setPassword(p);
        c.getMainAccount().setStatus(EnumUtils.StatusType.ACTIVE);
        c.getMainAccount().setCustomer(c);

        newCustomerSessionBean.createCustomer(c);
    }

    private void initInterest() {
        Interest i = new Interest();
        i.setName("Normal Interest");
        i.setVersion(0);
        i.setPercentage(new BigDecimal(0.0001));// 0.01%
        interestSessionBean.addInterest(i);

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
        initTransactions(customerDepositSessionBean.createAccount(cda));
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
