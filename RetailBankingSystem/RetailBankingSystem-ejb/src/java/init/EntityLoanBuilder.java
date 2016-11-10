/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import entity.loan.LoanExternalInterest;
import entity.loan.LoanInterest;
import entity.loan.LoanInterestCollection;
import entity.loan.LoanPaymentBreakdown;
import entity.loan.LoanProduct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import util.exception.common.MainAccountNotExistException;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class EntityLoanBuilder {

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    @EJB
    private LoanAccountSessionBeanLocal loanAccountSessionBean;
    @EJB
    private LoanProductSessionBeanLocal loanProductSessionBean;
    @EJB
    private LoanPaymentSessionBeanLocal loanPaymentSessionBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;

    private MainAccount demoMainAccount;
    private MainAccount demoMainAccount2;
    private MainAccount demoMainAccount3;
    private MainAccount demoMainAccount4;
    private MainAccount demoMainAccount5;

    private CustomerDepositAccount demoDepositAccount;

    public void initLoanAccount(CustomerDepositAccount da) {
        // TODO: Calculator testing methods
        demoDepositAccount = da;
        try {
            demoMainAccount = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_1);
            demoMainAccount2 = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_2);
            demoMainAccount3 = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_3);
            demoMainAccount4 = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_4);
            demoMainAccount5 = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_5);
        } catch (MainAccountNotExistException ex) {
            System.out.println("EntityLoanBuilder.initLoanAccount.MainAccountNotExistException");
        }

        System.out.print("Creating account !!!!================");

        initDemoPersonalLoanAccount();
        initDemoCarLoanAccount();
        initDemoHDBLoanAccount();
//        initDemoPPLoanAccount();
        initDemoLoanApplications();
    }

    private void initDemoLoanApplications() {
        LoanApplication loanApplication1 = new LoanApplication();
        loanApplication1.setIdentityType(EnumUtils.IdentityType.NRIC);
        try {
            loanApplication1.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1993"));
        } catch (Exception ex) {
        }
        loanApplication1.setAge(23);
        loanApplication1.setActualIncome(3000.0);
        loanApplication1.setFirstname("Yifan");
        loanApplication1.setLastname("Chen");
        loanApplication1.setFullName(loanApplication1.getLastname() + loanApplication1.getFirstname());
        loanApplication1.setEmail("wangzhe.lynx@gmail.com");
        loanApplication1.setPhone("81567758");

        loanApplication1.setNationality(EnumUtils.Nationality.SINGAPORE);
        loanApplication1.setMaritalStatus(EnumUtils.MaritalStatus.SINGLE);
        loanApplication1.setAddress("10 Punggol, 08-08");
        loanApplication1.setPostalCode("654321");
        loanApplication1.setIndustry(EnumUtils.Industry.RETAIL);
        loanApplication1.setEducation(EnumUtils.Education.DIPLOMA);
        loanApplication1.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
        loanApplication1.setGender(EnumUtils.Gender.MALE);
        loanApplication1.setCategory("New");

        loanApplication1.setIdentityNumber("S1234567Z");
        loanApplication1.setOtherCommitment(906.17);
        loanApplication1.setOtherHousingLoan(0);
        loanApplication1.setRequestedAmount(10000.0);
        loanApplication1.setMarketValue(30000.0);
        loanApplication1.setTenure(1);
        loanApplication1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanApplication1.setLoanProduct(loanProductSessionBean.getLoanProductByProductName(ConstantUtils.DEMO_PERSONAL_LOAN_PRODUCT_NAME_12));
        loanApplication1.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanApplication1 = loanAccountSessionBean.createLoanApplication(loanApplication1);

        LoanApplication loanApplication2 = new LoanApplication();
        loanApplication2.setIdentityType(EnumUtils.IdentityType.NRIC);
        try {
            loanApplication2.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1993"));
        } catch (Exception ex) {
        }
        loanApplication2.setAge(23);
        loanApplication2.setActualIncome(3000.0);
        loanApplication2.setFirstname("Yifan");
        loanApplication2.setLastname("Chen");
        loanApplication2.setFullName(loanApplication1.getLastname() + " " + loanApplication1.getFirstname());
        loanApplication2.setEmail("wangzh.lynx@gmail.com");
        loanApplication2.setPhone("81567758");

        loanApplication2.setNationality(EnumUtils.Nationality.SINGAPORE);
        loanApplication2.setMaritalStatus(EnumUtils.MaritalStatus.SINGLE);
        loanApplication2.setAddress("10 Punggol, 08-08");
        loanApplication2.setPostalCode("654321");
        loanApplication2.setIndustry(EnumUtils.Industry.RETAIL);
        loanApplication2.setEducation(EnumUtils.Education.DIPLOMA);
        loanApplication2.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
        loanApplication2.setGender(EnumUtils.Gender.MALE);
        loanApplication2.setCategory("Refinance");

        loanApplication2.setIdentityNumber("S1234567Z");
        loanApplication2.setOtherCommitment(906.17);
        loanApplication2.setOtherHousingLoan(0);
        loanApplication2.setRequestedAmount(10000.0);
        loanApplication2.setMarketValue(30000.0);
        loanApplication2.setTenure(6);

        loanApplication2.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR);
        loanApplication2.setLoanProduct(loanProductSessionBean.getLoanProductByProductName(ConstantUtils.DEMO_CAR_LOAN_PRODUCT_NAME));
        loanApplication2.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanApplication2 = loanAccountSessionBean.createLoanApplication(loanApplication2);

        LoanApplication loanApplication3 = new LoanApplication();
        loanApplication3.setIdentityType(EnumUtils.IdentityType.NRIC);
        loanApplication3.setIdentityNumber("S2222222Z");
        try {
            loanApplication2.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1983"));
        } catch (Exception ex) {
        }
        loanApplication3.setAge(33);
        loanApplication3.setActualIncome(6000.0);
        loanApplication3.setFirstname("Wong");
        loanApplication3.setLastname("Mary");
        loanApplication3.setFullName(loanApplication3.getLastname() + " " + loanApplication3.getFirstname());
        loanApplication3.setEmail("marry.wong@gmail.com");
        loanApplication3.setPhone("82222222");
        loanApplication3.setCategory("New");

        loanApplication3.setNationality(EnumUtils.Nationality.SINGAPORE);
        loanApplication3.setMaritalStatus(EnumUtils.MaritalStatus.MARRIED);
        loanApplication3.setAddress("15 Punggol, 08-08");
        loanApplication3.setPostalCode("654341");
        loanApplication3.setIndustry(EnumUtils.Industry.BANKING_FINANCE);
        loanApplication3.setEducation(EnumUtils.Education.UNIVERSITY);
        loanApplication3.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
        loanApplication3.setGender(EnumUtils.Gender.FEMALE);

        loanApplication3.setOtherCommitment(1000.0);
        loanApplication3.setOtherHousingLoan(1);
        loanApplication3.setRequestedAmount(300000.0);;
        loanApplication3.setMarketValue(600000.0);
        loanApplication3.setTenure(20);

        loanApplication3.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanApplication3.setLoanProduct(loanProductSessionBean.getLoanProductByProductName(ConstantUtils.DEMO_HDB_FIXED_LOAN_PRODUCT_NAME));
        loanApplication3.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanApplication3 = loanAccountSessionBean.createLoanApplication(loanApplication3);
    }

    private void initDemoPersonalLoanAccount() {
        // start of personal loan
        initDemoPersonalLoanAccount12();
        initDemoPersonalLoanAccount24();
        initDemoPersonalLoanAccount36();
        // end of personal loan
    }

    private void initDemoPersonalLoanAccount12() {
        // start of personal loan
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME_12);
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(12); // need to differetiate
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanInterest1.setInterestRate(0.085);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);

        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME_12);
        lic.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        lic.addLoanInterest(loanInterest1);
        lic = loanProductSessionBean.createInterestCollection(lic);

        loanInterest1.setLoanInterestCollection(lic);
        loanInterest1 = loanProductSessionBean.updateLoanInterest(loanInterest1);

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_PERSONAL_LOAN_PRODUCT_NAME_12);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(1);
        loanProduct.setLockInDuration(0);
        loanProduct.setPenaltyInterestRate(0.17); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        // calculated 
        loanAccount.setPrincipal(10000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));
        loanAccount.setPaymentDate(12);

        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());

        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);

        // end of personal loan
    }

    private void initDemoPersonalLoanAccount24() {
        // late payment demo account
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME_24);
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(24); // need to differetiate
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanInterest1.setInterestRate(0.085);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);

        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME_24);
        lic.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        lic.addLoanInterest(loanInterest1);
        lic = loanProductSessionBean.createInterestCollection(lic);

        loanInterest1.setLoanInterestCollection(lic);
        loanInterest1 = loanProductSessionBean.updateLoanInterest(loanInterest1);

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_PERSONAL_LOAN_PRODUCT_NAME_24);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(2);
        loanProduct.setLockInDuration(0);
        loanProduct.setPenaltyInterestRate(0.17); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount4);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        try {
            loanAccount.setPaymentStartDate(new SimpleDateFormat("dd-MM-yyyy").parse("9-10-2016"));
        } catch (Exception ex) {
        }
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(loanAccount.getPaymentStartDate(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(10000.0);
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(9);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        Double overdue = loanAccount.getMonthlyInstallment() * (1 + 0.17 * 4 / 365);
        loanAccount.setOverduePayment(overdue);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal() + loanAccount.getOverduePayment());
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());

        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);

        LoanAccount badLoanAccount = new LoanAccount();
        badLoanAccount.setTenure(loanProduct.getTenure());
        badLoanAccount.setDepositAccount(demoDepositAccount);
        badLoanAccount.setLoanProduct(loanProduct);
        badLoanAccount.setMainAccount(demoMainAccount4);
        badLoanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        try {
            badLoanAccount.setPaymentStartDate(new SimpleDateFormat("dd-MM-yyyy").parse("12-7-2016"));
        } catch (Exception ex) {
        }
        badLoanAccount.setMaturityDate(DateUtils.addYearsToDate(loanAccount.getPaymentStartDate(), loanAccount.getTenure()));// calculated 
        badLoanAccount.setPrincipal(10000.0);
        badLoanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.SUSPENDED);
        badLoanAccount.setCurrentPeriod(4);
        badLoanAccount.setPaymentDate(12);
        badLoanAccount.setOutstandingPrincipal(badLoanAccount.getPrincipal());
        badLoanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(badLoanAccount));
        Double overdue2 = badLoanAccount.getMonthlyInstallment() * (4 + 0.17 * 122 / 365 + 0.17 * 91 / 365 + 0.17 * 61 / 365 + 0.17 * 30 / 365);
        badLoanAccount.setOverduePayment(overdue2);
        badLoanAccount.setOutstandingPrincipal(badLoanAccount.getPrincipal() + badLoanAccount.getOverduePayment());
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());

        badLoanAccount = loanAccountSessionBean.createLoanAccount(badLoanAccount);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + badLoanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result2 = loanPaymentSessionBean.futurePaymentBreakdown(badLoanAccount);
        // end of personal loan
    }

    private void initDemoPersonalLoanAccount36() {
        // start of personal loan
        System.out.print("LALALALALA");
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME_36);
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(36); // need to differetiate
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanInterest1.setInterestRate(0.080);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);

        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME_36);
        lic.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        lic.addLoanInterest(loanInterest1);
        lic = loanProductSessionBean.createInterestCollection(lic);

        loanInterest1.setLoanInterestCollection(lic);
        loanInterest1 = loanProductSessionBean.updateLoanInterest(loanInterest1);

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_PERSONAL_LOAN_PRODUCT_NAME_36);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(3);
        loanProduct.setLockInDuration(0);
        loanProduct.setPenaltyInterestRate(0.16); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount2);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));
        // calculated 
        loanAccount.setPrincipal(10000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);

        loanAccount.setPaymentDate(12);

        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());

        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);

        // end of personal loan
    }

    private void initDemoCarLoanAccount() {
        // start of car loan
        // follow
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_CAR_LOAN_INTEREST_NAME);
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(-1); // no end date
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR);
        loanInterest1.setInterestRate(0.025);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);

        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_CAR_LOAN_INTEREST_NAME);
        lic.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR);
        lic.addLoanInterest(loanInterest1);
        lic = loanProductSessionBean.createInterestCollection(lic);

        loanInterest1.setLoanInterestCollection(lic);
        loanInterest1 = loanProductSessionBean.updateLoanInterest(loanInterest1);

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_CAR_LOAN_PRODUCT_NAME);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(7);
        loanProduct.setLockInDuration(0);
        loanProduct.setPenaltyInterestRate(0.05); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder CAR LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount4);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));

        loanAccount.setPrincipal(100000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));
        loanAccount.setPaymentDate(DateUtils.getDayNumber(loanAccount.getPaymentStartDate()));

        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());

        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder CAR LOAN========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);

        LoanAccount badLoanAccount = new LoanAccount();
        badLoanAccount.setTenure(loanProduct.getTenure());
        badLoanAccount.setDepositAccount(demoDepositAccount);
        badLoanAccount.setLoanProduct(loanProduct);
        badLoanAccount.setMainAccount(demoMainAccount5);
        badLoanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        try {
            badLoanAccount.setPaymentStartDate(new SimpleDateFormat("dd-MM-yyyy").parse("9-8-2016"));
        } catch (Exception ex) {
        }
        badLoanAccount.setMaturityDate(DateUtils.addYearsToDate(badLoanAccount.getPaymentStartDate(), loanAccount.getTenure()));// calculated 
        badLoanAccount.setPrincipal(30000.0);
        badLoanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.SUSPENDED);

        badLoanAccount.setPaymentDate(9);
        badLoanAccount.setOutstandingPrincipal(badLoanAccount.getPrincipal());
        badLoanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        Double overdue = loanAccount.getMonthlyInstallment() * (3 + 0.05 * 93 / 365 + 0.05 * 63 / 365 + 0.05 * 33 / 365);
        badLoanAccount.setOverduePayment(overdue);
        badLoanAccount.setOutstandingPrincipal(badLoanAccount.getPrincipal() + badLoanAccount.getOverduePayment());

        System.out.println("Monthly installment is: $" + badLoanAccount.getMonthlyInstallment());

        badLoanAccount = loanAccountSessionBean.createLoanAccount(badLoanAccount);
        System.out.print("EntityLoanBuilder CAR LOAN========== " + badLoanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result2 = loanPaymentSessionBean.futurePaymentBreakdown(badLoanAccount);

        // end of car loan
    }

    private void initDemoHDBLoanAccount() {
        initDemoHDBLoanFixed();
        initDemoHDBLoanSIBOR();
//        initDemoHDBLoanFHR();
    }

    private void initDemoHDBLoanFixed() {
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "0-12");
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(12); // no end date
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest1.setInterestRate(0.018);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);

        LoanInterest loanInterest2 = new LoanInterest();
        loanInterest2.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "12-24");
        loanInterest2.setStartMonth(13);
        loanInterest2.setEndMonth(24); // no end date
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest2.setInterestRate(0.018);
        loanInterest2 = loanProductSessionBean.createLoanInterest(loanInterest2);

        LoanInterest loanInterest3 = new LoanInterest();
        loanInterest3.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "25-36");
        loanInterest3.setStartMonth(25);
        loanInterest3.setEndMonth(36); // no end date
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest3.setInterestRate(0.018);
        loanInterest3 = loanProductSessionBean.createLoanInterest(loanInterest3);

        LoanInterest loanInterest4 = new LoanInterest();
        loanInterest4.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "37--1");
        loanInterest4.setStartMonth(37);
        loanInterest4.setEndMonth(-1); // no end date
        loanInterest4.setFhr18(Boolean.TRUE);
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest4.setInterestRate(0.012);
        loanInterest4 = loanProductSessionBean.createLoanInterest(loanInterest4);

        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME);
        lic.addLoanInterest(loanInterest1);
        lic.addLoanInterest(loanInterest2);
        lic.addLoanInterest(loanInterest3);
        lic.addLoanInterest(loanInterest4);
        lic.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        lic = loanProductSessionBean.createInterestCollection(lic);

        loanInterest1.setLoanInterestCollection(lic);
        loanInterest1 = loanProductSessionBean.updateLoanInterest(loanInterest1);
        loanInterest2.setLoanInterestCollection(lic);
        loanInterest2 = loanProductSessionBean.updateLoanInterest(loanInterest2);
        loanInterest3.setLoanInterestCollection(lic);
        loanInterest3 = loanProductSessionBean.updateLoanInterest(loanInterest3);
        loanInterest4.setLoanInterestCollection(lic);
        loanInterest4 = loanProductSessionBean.updateLoanInterest(loanInterest4);

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_HDB_FIXED_LOAN_PRODUCT_NAME);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(25);
        loanProduct.setLockInDuration(3);
        loanProduct.setPenaltyInterestRate(0.06); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount4);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));

        loanAccount.setPrincipal(300000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));
        loanAccount.setPaymentDate(12);

        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());
        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);

    }

    private void initDemoHDBLoanSIBOR() {

        LoanExternalInterest loanExternalInterest = new LoanExternalInterest();
        loanExternalInterest.setName(ConstantUtils.DEMO_LOAN_COMMON_INTEREST_NAME);
        loanExternalInterest.setRate(0.01);
        loanExternalInterest = loanProductSessionBean.createCommonInterest(loanExternalInterest);

        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME + "0-12");
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(12); // no end date
        loanInterest1.setInterestRate(0.018);
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest1.setLoanExternalInterest(loanExternalInterest);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);

        LoanInterest loanInterest2 = new LoanInterest();
        loanInterest2.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME + "12-24");
        loanInterest2.setStartMonth(13);
        loanInterest2.setEndMonth(24); // no end date
        loanInterest2.setInterestRate(0.018);
        loanInterest2.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest2.setLoanExternalInterest(loanExternalInterest);
        loanInterest2 = loanProductSessionBean.createLoanInterest(loanInterest2);

        LoanInterest loanInterest3 = new LoanInterest();
        loanInterest3.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME + "25-36");
        loanInterest3.setStartMonth(25);
        loanInterest3.setEndMonth(36); // no end date
        loanInterest3.setInterestRate(0.018);
        loanInterest3.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest3.setLoanExternalInterest(loanExternalInterest);
        loanInterest3 = loanProductSessionBean.createLoanInterest(loanInterest3);

        LoanInterest loanInterest4 = new LoanInterest();
        loanInterest4.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME + "37--1");
        loanInterest4.setStartMonth(37);
        loanInterest4.setEndMonth(-1); // no end date
        loanInterest4.setInterestRate(0.015);
        loanInterest4.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest4.setLoanExternalInterest(loanExternalInterest);
        loanInterest4 = loanProductSessionBean.createLoanInterest(loanInterest4);

        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME);
        lic.addLoanInterest(loanInterest1);
        lic.addLoanInterest(loanInterest2);
        lic.addLoanInterest(loanInterest3);
        lic.addLoanInterest(loanInterest4);
        lic.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        lic = loanProductSessionBean.createInterestCollection(lic);

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_HDB_SIBOR_LOAN_PRODUCT_NAME);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(25);
        loanProduct.setLockInDuration(0);
        loanProduct.setPenaltyInterestRate(0.06); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount4);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME)); // assignment
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(300000.0); // need to be calculated
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(12);

        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());
        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);

    }

    private void initDemoPPLoanAccount() {

    }
}
