/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;
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
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;

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
    private LoginSessionBeanLocal loginBean;
    
    private MainAccount demoMainAccount;
    private MainAccount demoMainAccount2;
    private CustomerDepositAccount demoDepositAccount;
    
    public void initLoanAccount(CustomerDepositAccount da) {
        // TODO: Calculator testing methods
        demoDepositAccount = da;
        demoMainAccount = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        demoMainAccount2 = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_2);
        
        System.out.print("Creating account !!!!================");
        
        
        initDemoPersonalLoanAccount();
        initDemoCarLoanAccount();
        initDemoHDBLoanAccount();
//        initDemoPPLoanAccount();
        initDemoLoanApplications();
    }
    
    private void initDemoLoanApplications() {
        LoanApplication loanApplication1 = new LoanApplication();
        loanApplication1.setAge(21);
        loanApplication1.setIncome(3000.0);
        loanApplication1.setName("Lei Yang");
        loanApplication1.setEmail("raymondlei90s@gmail.com");
        loanApplication1.setPhone("94761895");
        loanApplication1.setIdNumber("S9876543I");
        loanApplication1.setOtherCommitment(0.0);
        loanApplication1.setOtherHousingLoan(null);
        loanApplication1.setRequestedAmount(10000.0);
        loanApplication1.setMarketValue(null);
        loanApplication1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanApplication1.setLoanProduct(loanProductSessionBean.getLoanProductByProductName(ConstantUtils.DEMO_PERSONAL_LOAN_PRODUCT_NAME_12));
        loanApplication1.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanApplication1 = loanAccountSessionBean.createLoanApplication(loanApplication1);
        
        LoanApplication loanApplication2 = new LoanApplication();
        loanApplication2.setAge(21);
        loanApplication2.setIncome(3000.0);
        loanApplication2.setName("Lei Yang");
        loanApplication2.setEmail("raymondlei90s@gmail.com");
        loanApplication2.setPhone("94761895");
        loanApplication2.setIdNumber("S9876543I");
        loanApplication2.setOtherCommitment(0.0);
        loanApplication2.setOtherHousingLoan(null);
        loanApplication2.setRequestedAmount(80000.0);
        loanApplication2.setMarketValue(100000.0);
        loanApplication2.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR);
        loanApplication2.setLoanProduct(loanProductSessionBean.getLoanProductByProductName(ConstantUtils.DEMO_CAR_LOAN_PRODUCT_NAME));
        loanApplication2.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanApplication2 = loanAccountSessionBean.createLoanApplication(loanApplication2);
        
        LoanApplication loanApplication3 = new LoanApplication();
        loanApplication3.setAge(21);
        loanApplication3.setIncome(3000.0);
        loanApplication3.setName("Lei Yang");
        loanApplication3.setEmail("raymondlei90s@gmail.com");
        loanApplication3.setPhone("94761895");
        loanApplication3.setOtherCommitment(0.0);
        loanApplication3.setOtherHousingLoan(0);
        loanApplication3.setRequestedAmount(220000.0);
        loanApplication3.setMarketValue(300000.0);
        loanApplication3.setIdNumber("S9876543I");
        loanApplication3.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanApplication3.setLoanProduct(loanProductSessionBean.getLoanProductByProductName(ConstantUtils.DEMO_HDB_FIXED_LOAN_PRODUCT_NAME));
        loanApplication3.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanApplication3 = loanAccountSessionBean.createLoanApplication(loanApplication3);
    }
    
    private void initDemoPersonalLoanAccount() {
        // start of personal loan
        initDemoPersonalLoanAccount12();
        initDemoPersonalLoanAccount24();
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
        loanProduct.setPenaltyInterestRate(0.2); // TODO: this need to be calculated
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
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(10000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(23);
        
        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());
        
        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);
        
        // end of personal loan
    }
    
    private void initDemoPersonalLoanAccount24() {
        // start of personal loan
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME_24);
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(12); // need to differetiate
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
        loanProduct.setPenaltyInterestRate(0.2); // TODO: this need to be calculated
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
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(10000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(23);
        
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
        loanInterest1.setInterestRate(0.085);
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
        loanProduct.setPenaltyInterestRate(0.2); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder CAR LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(100000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(DateUtils.getDayNumber(loanAccount.getPaymentStartDate()));
        
        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());
        
        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder CAR LOAN========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);
        
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
        loanProduct.setPenaltyInterestRate(0.2); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(300000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(23);
        
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
        loanInterest1.setInterestRate(0.008);
        loanInterest1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest1.setLoanExternalInterest(loanExternalInterest);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);
        
        LoanInterest loanInterest2 = new LoanInterest();
        loanInterest2.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME + "12-24");
        loanInterest2.setStartMonth(13);
        loanInterest2.setEndMonth(24); // no end date
        loanInterest2.setInterestRate(0.008);
        loanInterest2.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest2.setLoanExternalInterest(loanExternalInterest);
        loanInterest2 = loanProductSessionBean.createLoanInterest(loanInterest2);
        
        LoanInterest loanInterest3 = new LoanInterest();
        loanInterest3.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME + "25-36");
        loanInterest3.setStartMonth(25);
        loanInterest3.setEndMonth(36); // no end date
        loanInterest3.setInterestRate(0.008);
        loanInterest3.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanInterest3.setLoanExternalInterest(loanExternalInterest);
        loanInterest3 = loanProductSessionBean.createLoanInterest(loanInterest3);
                
        LoanInterest loanInterest4 = new LoanInterest();
        loanInterest4.setName(ConstantUtils.DEMO_HDB_SIBOR_INTEREST_NAME + "37--1");
        loanInterest4.setStartMonth(37);
        loanInterest4.setEndMonth(-1); // no end date
        loanInterest4.setInterestRate(0.0125);
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
        loanProduct.setPenaltyInterestRate(0.5); // TODO: this need to be calculated
        loanProduct = loanProductSessionBean.createLoanProduct(loanProduct);
        lic.setLoanProduct(loanProduct);
        lic = loanProductSessionBean.updateInterestCollection(lic);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount2);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME)); // assignment
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYearsToDate(new Date(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(300000.0); // need to be calculated
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(23);
        
        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());
        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);

    }
    
    private void initDemoPPLoanAccount() {
        
    }
}
