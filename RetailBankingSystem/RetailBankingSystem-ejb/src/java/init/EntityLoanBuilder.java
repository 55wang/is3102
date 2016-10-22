/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.rules.TimeRangeInterest;
import entity.loan.LoanAccount;
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
import org.apache.commons.lang.time.DateUtils;
import server.utilities.ConstantUtils;
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
    private InterestSessionBeanLocal interestBean;
    @EJB
    private LoginSessionBeanLocal loginBean;
    
    private MainAccount demoMainAccount;
    private CustomerDepositAccount demoDepositAccount;
    
    public void initLoanAccount(CustomerDepositAccount da) {
        // TODO: Calculator testing methods
        demoDepositAccount = da;
        demoMainAccount = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        System.out.print("Creating account !!!!================");
        
//        initDemoPersonalLoanAccount();
//        initDemoCarLoanAccount();
        initDemoHDBLoanAccount();
//        initDemoPPLoanAccount();
    }
    
    private void initDemoPersonalLoanAccount() {
        // start of personal loan
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME);
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(12); // need to differetiate
        loanInterest1.setInterestRate(0.2);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);
        
        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_PERSONAL_LOAN_INTEREST_NAME);
        lic.addLoanInterest(loanInterest1);
        lic = loanProductSessionBean.createInterestCollection(lic);
        
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_PERSONAL_LOAN_PRODUCT_NAME);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(1);
        loanProduct.setLockInDuration(0);
        loanProduct.setPenaltyInterestRate(0.5); // TODO: this need to be calculated
        loanProduct = loanAccountSessionBean.createLoanProduct(loanProduct);
        System.out.print("EntityLoanBuilder PERSONAL LOAN ========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYears(new Date(), loanAccount.getTenure()));// calculated 
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
        for (LoanPaymentBreakdown r : result) {
            System.out.println(r.toString());
        }
        
        // end of personal loan
    }
    
    private void initDemoCarLoanAccount() {
        // start of car loan
        // follow
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_CAR_LOAN_INTEREST_NAME);
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(-1); // no end date
        loanInterest1.setInterestRate(0.2);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);
        
        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_CAR_LOAN_INTEREST_NAME);
        lic.addLoanInterest(loanInterest1);
        lic = loanProductSessionBean.createInterestCollection(lic);
        
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_CAR_LOAN_PRODUCT_NAME);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(7);
        loanProduct.setLockInDuration(0);
        loanProduct.setPenaltyInterestRate(0.5); // TODO: this need to be calculated
        loanProduct = loanAccountSessionBean.createLoanProduct(loanProduct);
        System.out.print("EntityLoanBuilder CAR LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYears(new Date(), loanAccount.getTenure()));// calculated 
        loanAccount.setPrincipal(100000.0);
        loanAccount.setOutstandingPrincipal(loanAccount.getPrincipal());
        loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        loanAccount.setCurrentPeriod(0);
        loanAccount.setPaymentDate(23);
        
        loanAccount.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(loanAccount));
        System.out.println("Monthly installment is: $" + loanAccount.getMonthlyInstallment());
        
        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder CAR LOAN========== " + loanAccount.getAccountNumber());
        List<LoanPaymentBreakdown> result = loanPaymentSessionBean.futurePaymentBreakdown(loanAccount);
        for (LoanPaymentBreakdown r : result) {
            System.out.println(r.toString());
        }
        
        // end of car loan
    }
    
    private void initDemoHDBLoanAccount() {
        initDemoHDBLoanFixed();
//        initDemoHDBLoanSIBOR();
//        initDemoHDBLoanFHR();
    }
    
    private void initDemoHDBLoanFixed() {
//        LoanInterest loanInterest1 = new LoanInterest();
//        loanInterest1.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "0--1");
//        loanInterest1.setStartMonth(0);
//        loanInterest1.setEndMonth(-1); // no end date
//        loanInterest1.setInterestRate(0.018);
//        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);
//        
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "0-12");
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(12); // no end date
        loanInterest1.setInterestRate(0.018);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);
        
        LoanInterest loanInterest2 = new LoanInterest();
        loanInterest2.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "12-24");
        loanInterest2.setStartMonth(13);
        loanInterest2.setEndMonth(24); // no end date
        loanInterest2.setInterestRate(0.018);
        loanInterest2 = loanProductSessionBean.createLoanInterest(loanInterest2);
        
        LoanInterest loanInterest3 = new LoanInterest();
        loanInterest3.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "25-36");
        loanInterest3.setStartMonth(25);
        loanInterest3.setEndMonth(36); // no end date
        loanInterest3.setInterestRate(0.018);
        loanInterest3 = loanProductSessionBean.createLoanInterest(loanInterest3);
                
        LoanInterest loanInterest4 = new LoanInterest();
        loanInterest4.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME + "37--1");
        loanInterest4.setStartMonth(37);
        loanInterest4.setEndMonth(-1); // no end date
        loanInterest4.setInterestRate(0.012);
        loanInterest4 = loanProductSessionBean.createLoanInterest(loanInterest4);
        
        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_HDB_FIXED_INTEREST_NAME);
        lic.addLoanInterest(loanInterest1);
        lic.addLoanInterest(loanInterest2);
        lic.addLoanInterest(loanInterest3);
        lic.addLoanInterest(loanInterest4);
        lic = loanProductSessionBean.createInterestCollection(lic);
        
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_HDB_FIXED_LOAN_PRODUCT_NAME);
        loanProduct.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        loanProduct.setLoanInterestCollection(lic);
        loanProduct.setTenure(25);
        loanProduct.setLockInDuration(3);
        loanProduct.setPenaltyInterestRate(0.5); // TODO: this need to be calculated
        loanProduct = loanAccountSessionBean.createLoanProduct(loanProduct);
        System.out.print("EntityLoanBuilder HDB LOAN========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setTenure(loanProduct.getTenure());
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(DateUtils.addYears(new Date(), loanAccount.getTenure()));// calculated 
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
        for (LoanPaymentBreakdown r : result) {
            System.out.println(r.toString());
        }
    }
    
    private void initDemoPPLoanAccount() {
        
    }
}
