/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanPaymentBreakdown;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.LoanAccountStatus;
import server.utilities.PincodeGenerationUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "approveLoanAccountManagedBean")
@ViewScoped
public class ApproveLoanAccountManagedBean implements Serializable {
    
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;
    @EJB
    private LoanPaymentSessionBeanLocal loanPaymentBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountBean;
    @EJB
    private EmailServiceSessionBeanLocal emailBean;

    private String bureauCreditScore;
    private List<LoanAccount> myLoanAccounts = new ArrayList<>();
    
    public ApproveLoanAccountManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        myLoanAccounts = loanAccountBean.getLoanAccountByStaffUsernameAndStatus(SessionUtils.getStaffUsername(), LoanAccountStatus.PENDING);
    }

    public void approveLoanAccount(LoanAccount la) {
        // change status
        la.setLoanAccountStatus(EnumUtils.LoanAccountStatus.APPROVED);
        la = loanAccountBean.updateLoanAccount(la);
                
        // generate breakdown
        List<LoanPaymentBreakdown> result = loanPaymentBean.futurePaymentBreakdown(la);
        for (LoanPaymentBreakdown r : result) {
            System.out.println(r.toString());
        }
        // inform customer by email
        System.out.println(la.getMainAccount().getCustomer().getEmail());
        String randomPwd = PincodeGenerationUtils.generatePwd();
        MainAccount mainAccount = la.getMainAccount();
        mainAccount.setPassword(randomPwd);
        mainAccount = mainAccountBean.updateMainAccount(mainAccount);
        emailBean.sendActivationGmailForCustomer(mainAccount.getCustomer().getEmail(), randomPwd);
        emailBean.sendLoanApplicationApprovalNotice(la.getMainAccount().getCustomer().getEmail());
        
        MessageUtils.displayInfo("Application Approved!");
        myLoanAccounts.remove(la);
    }
    
    public void rejectLoanAccount(LoanAccount la) {
        // change status
        la.setLoanAccountStatus(EnumUtils.LoanAccountStatus.REJECTED);
        la = loanAccountBean.updateLoanAccount(la);
        // inform customer by email
        emailBean.sendLoanApplicationRejectNotice(la.getMainAccount().getCustomer().getEmail());
        
        MessageUtils.displayInfo("Application Rejected!");
        myLoanAccounts.remove(la);
    }
    
    public void calculateCreditScore(LoanAccount la) {
        try {
            la.getMainAccount().getCustomer().setBureaCreditScore(bureauCreditScore);
            double creditScore = calculateCreditLvl(la, bureauCreditScore);
            la.getMainAccount().getCustomer().setCreditScore(creditScore);
            MessageUtils.displayInfo("Credit Score Updated!");
        } catch (Exception ex) {
            System.out.println("error");
            MessageUtils.displayError("Error! Not updated!");
        }
        
    }
    
    public double calculateCreditLvl(LoanAccount la, String creditBureauScore) {
        try {
            
            System.out.println("inside calculateCreditLvl");
            System.out.println(creditBureauScore);
            double creditScore = 0;
            //check annual gross income
            EnumUtils.Income income = la.getMainAccount().getCustomer().getIncome();
            if (income.equals(EnumUtils.Income.BELOW_2000)) {
                creditScore += 5;
            } else if (income.equals(EnumUtils.Income.FROM_2000_TO_4000)) {
                creditScore += 10;
            } else if (income.equals(EnumUtils.Income.FROM_4000_TO_6000)) {
                creditScore += 20;
            } else if (income.equals(EnumUtils.Income.FROM_6000_TO_8000)) {
                creditScore += 40;
            } else if (income.equals(EnumUtils.Income.FROM_8000_TO_10000)) {
                creditScore += 60;
            } else if (income.equals(EnumUtils.Income.OVER_10000)) {
                creditScore += 80;
            }

            System.out.println("calculated income");
            Date birthday = la.getMainAccount().getCustomer().getBirthDay();
            Date now = new Date();
            Long age = getAge(birthday, now);
            if (age < 50) {
                creditScore += 25;
            } else if (age >= 50 && age < 60) {
                creditScore += 10;
            } else if (age >= 60) {
                creditScore += 0;
            }
            System.out.println("calculated age");
            
            EnumUtils.Education education = la.getMainAccount().getCustomer().getEducation();
            if (education.equals(EnumUtils.Education.POSTGRAD)) {
                creditScore += 50;
            } else if (education.equals(EnumUtils.Education.UNIVERSITY)) {
                creditScore += 40;
            } else if (education.equals(EnumUtils.Education.DIPLOMA)) {
                creditScore += 30;
            } else if (education.equals(EnumUtils.Education.A_LEVEL)) {
                creditScore += 25;
            } else if (education.equals(EnumUtils.Education.TECHNICAL)) {
                creditScore += 10;
            } else if (education.equals(EnumUtils.Education.SECONDARY)) {
                creditScore += 5;
            } else if (education.equals(EnumUtils.Education.OTHERS)) {
                creditScore += 0;
            }
            System.out.println("calculated education");
//        AA, BB, CC, DD, EE, FF, GG, HH, HX, HZ, GX, BX, CX
            if (creditBureauScore.equals("AA")) {
                creditScore += 100;
            } else if (creditBureauScore.equals("BB")) {
                creditScore += 90;
            } else if (creditBureauScore.equals("CC")) {
                creditScore += 80;
            } else if (creditBureauScore.equals("DD")) {
                creditScore += 70;
            } else if (creditBureauScore.equals("EE")) {
                creditScore += 50;
            } else if (creditBureauScore.equals("FF")) {
                creditScore += 30;
            } else if (creditBureauScore.equals("GG")) {
                creditScore += 20;
            } else if (creditBureauScore.equals("HH")) {
                creditScore += 10;
            } else {
                creditScore += 0;
            }
            System.out.println("calculated bureau credit score");
            return creditScore;
        } catch (Exception ex) {
            System.out.println("error in credit score");
        }
        return 0;
        
    }
    
    public long getAge(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
        
        delta = delta / 365;
        return delta;
    }
    
    /**
     * @return the myLoanAccounts
     */
    public List<LoanAccount> getMyLoanAccounts() {
        return myLoanAccounts;
    }

    /**
     * @param myLoanAccounts the myLoanAccounts to set
     */
    public void setMyLoanAccounts(List<LoanAccount> myLoanAccounts) {
        this.myLoanAccounts = myLoanAccounts;
    }

    /**
     * @return the bureauCreditScore
     */
    public String getBureauCreditScore() {
        return bureauCreditScore;
    }

    /**
     * @param bureauCreditScore the bureauCreditScore to set
     */
    public void setBureauCreditScore(String bureauCreditScore) {
        this.bureauCreditScore = bureauCreditScore;
    }
    
}
