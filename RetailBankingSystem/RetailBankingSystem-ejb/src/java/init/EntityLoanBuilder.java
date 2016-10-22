/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanExternalInterest;
import entity.loan.LoanInterest;
import entity.loan.LoanInterestCollection;
import entity.loan.LoanProduct;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.ConstantUtils;

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
    private LoginSessionBeanLocal loginBean;
    
    private final Calendar cal = Calendar.getInstance();

    public void initLoanAccount(CustomerDepositAccount demoDepositAccount) {
        MainAccount demoMainAccount = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        System.out.print("Creating account !!!!================");
        
        // this is not for car and personal loan
//        LoanExternalInterest lci = new LoanExternalInterest();
//        lci.setName(ConstantUtils.DEMO_LOAN_COMMON_INTEREST_NAME);
//        lci.setRate(0.15);
//        lci = loanProductSessionBean.createCommonInterest(lci);
        
        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setName(ConstantUtils.DEMO_LOAN_INTEREST_NAME + "0-3");
        loanInterest1.setStartMonth(0);
        loanInterest1.setEndMonth(3);
        loanInterest1.setInterestRate(0.2);
        loanInterest1 = loanProductSessionBean.createLoanInterest(loanInterest1);

        LoanInterest loanInterest2 = new LoanInterest();
        loanInterest2.setName(ConstantUtils.DEMO_LOAN_INTEREST_NAME + "3-6");
        loanInterest2.setStartMonth(3);
        loanInterest2.setEndMonth(6);
        loanInterest2.setInterestRate(0.2);
        loanInterest2 = loanProductSessionBean.createLoanInterest(loanInterest2);
        
        LoanInterestCollection lic = new LoanInterestCollection();
        lic.setName(ConstantUtils.DEMO_LOAN_INTEREST_NAME);
        lic.addLoanInterest(loanInterest1);
        lic.addLoanInterest(loanInterest2);
        lic = loanProductSessionBean.createInterestCollection(lic);
        
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductName(ConstantUtils.DEMO_LOAN_PRODUCT_NAME);
        loanProduct.setLoanInterestCollection(lic);
//        loanProduct.setLoanExternalInterest(lci);
        loanProduct.setTenure(6);
        loanProduct.setLockInDuration(2);
        loanProduct.setPenaltyInterestRate(0.5);
        loanProduct = loanAccountSessionBean.createLoanProduct(loanProduct);
        System.out.print("EntityLoanBuilder ========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setDepositAccount(demoDepositAccount);
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        cal.add(Calendar.YEAR, 6);
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(cal.getTime()); // TODO: Need to check this date
        loanAccount.setPrincipal(5000.0);
        loanAccount.setPaymentDate(23);
        loanAccount.setMonthlyInstallment(300.0); // need to be calculated

        loanAccount = loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityLoanBuilder ========== " + loanAccount.getAccountNumber());

    }
}
