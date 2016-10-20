/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanInterest;
import entity.loan.LoanProduct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private LoginSessionBeanLocal loginBean;
    
    private final Calendar cal = Calendar.getInstance();

    public void initLoanAccount(CustomerDepositAccount demoDepositAccount) {
        MainAccount demoMainAccount = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        System.out.print("Creating account !!!!================");
        LoanAccount loanAccount = new LoanAccount();
        List<LoanInterest> loanInterests = new ArrayList<>();

        LoanInterest loanInterest1 = new LoanInterest();
        loanInterest1.setId(Long.getLong(Integer.toString(1)));
        loanInterest1.setStartTime(0);
        loanInterest1.setEndTime(3);
        loanInterest1.setInterestRate(0.2);
        loanInterests.add(loanInterest1);

        LoanInterest loanInterest2 = new LoanInterest();
        loanInterest2.setId(Long.getLong(Integer.toString(2)));
        loanInterest2.setStartTime(3);
        loanInterest2.setEndTime(6);
        loanInterest2.setInterestRate(0.2);
        loanInterests.add(loanInterest2);

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setId(Long.getLong(Integer.toString(1)));
        loanProduct.setProductName("MBS Personal Loan");
        loanProduct.setLoanInterests(loanInterests);
        loanProduct.setTenure(6);
        loanProduct.setLockInDuration(2);
        loanProduct.setPenaltyInterestRate(0.5);
        loanAccountSessionBean.createLoanProduct(loanProduct);
        System.out.print("EntityBuilder ========== " + loanProduct.getProductName());
        System.out.print(demoMainAccount.getBankAcounts().size());
        loanAccount.setDepositAccount(demoDepositAccount);

        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setMainAccount(demoMainAccount);
        loanAccount.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        cal.add(Calendar.YEAR, 6);
        loanAccount.setPaymentStartDate(new Date());
        loanAccount.setMaturityDate(cal.getTime());
        loanAccount.setPrincipal(5000.0);
        loanAccount.setPaymentDate(23);
        loanAccount.setMonthlyInstallment(300.0);

        loanAccountSessionBean.createLoanAccount(loanAccount);
        System.out.print("EntityBuilder ========== " + loanAccount.getAccountNumber());

    }
}
