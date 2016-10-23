/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import ejb.session.dams.InterestSessionBeanLocal;
import entity.dams.rules.TimeRangeInterest;
import entity.loan.LoanAccount;
import entity.loan.LoanExternalInterest;
import entity.loan.LoanInterest;
import entity.loan.LoanPaymentBreakdown;
import entity.loan.LoanRepaymentRecord;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.time.DateUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Stateless
public class LoanPaymentSessionBean implements LoanPaymentSessionBeanLocal {

    @EJB
    private LoanCalculationSessionBeanLocal loanCalculationSessionBean;
    @EJB
    private InterestSessionBeanLocal interestBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public LoanPaymentBreakdown createLoanPaymentBreakdown(LoanPaymentBreakdown loanPaymentBreakdown) {
        em.persist(loanPaymentBreakdown);
        return loanPaymentBreakdown;
    }

    @Override
    public LoanRepaymentRecord createLoanRepaymentRecord(LoanRepaymentRecord loanRepaymentRecord) {
        em.persist(loanRepaymentRecord);
        return loanRepaymentRecord;
    }

    @Override
    public Double calculateMonthlyInstallment(LoanAccount loanAccount) {
        EnumUtils.LoanProductType type = loanAccount.getLoanProduct().getProductType();

        if (type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR || type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL) {
            // simple interest
            System.out.println("Calculating monthly installment for simple interest");
            return calculateMonthlyInstallmentForSimpleInterest(loanAccount);
        } else if (type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB || type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PRIVATE_HOUSE) {
            // compound interest
            System.out.println("Calculating monthly installment for compound interest");
            return calculateMonthlyInstallmentForCompoundInterest(loanAccount);
        } else {
            // suspended
            return null;
        }
    }

    private Double calculateMonthlyInstallmentForSimpleInterest(LoanAccount loanAccount) {
        List<LoanInterest> loanInterests = loanAccount.getLoanProduct().getLoanInterestCollection().getLoanInterests();
        // only has one interest
        if (loanInterests.size() == 1) {
            LoanInterest interest = loanInterests.get(0);
            // original calculation
            Double totalPrincipal = loanAccount.getPrincipal();
            Double totalInterest = loanAccount.getPrincipal() * interest.getInterestRate() * loanAccount.getTenure();
            System.out.println("Total principal is: " + totalPrincipal);
            System.out.println("Total Interest is: " + totalInterest);

            return (totalPrincipal + totalInterest) / loanAccount.tenureInMonth();
        } else {
            return null;
        }
    }

    private Double calculateMonthlyInstallmentForCompoundInterest(LoanAccount loanAccount) {
        Integer tenure = loanAccount.tenureInMonth();
        Double monthlyLoanInterest = getAverageInterest(loanAccount);
        Double loanAmt = loanAccount.getOutstandingPrincipal();
        System.out.println("Average monthly interest percentage: " + monthlyLoanInterest);
        return loanCalculationSessionBean.calculateMonthlyInstallment(monthlyLoanInterest, tenure, loanAmt);
    }

    private Double getAverageInterest(LoanAccount loanAccount) {
        System.out.println("getAverageInterest");
        Integer tenure = loanAccount.tenureInMonth();
        System.out.println("Tenure: " + tenure);
        List<LoanInterest> loanInterests = loanAccount.getLoanProduct().getLoanInterestCollection().getLoanInterests();
        System.out.println("Loan interest size: " + loanInterests.size());
        Double totalPercentage = 0.0;
        // calculate average
        // check external interest if needed
        for (LoanInterest li : loanInterests) {
            Integer totalMonth = 0;
            if (li.getEndMonth() == -1) {
                totalMonth = loanAccount.tenureInMonth() - li.getStartMonth() + 1;
            } else {
                totalMonth = 12;
            }
            Double extraInterest = 0.0;
            if (li.getFhr18()) {
                TimeRangeInterest fhr18 = interestBean.getTimeRangeInterestByAmountAndMonth(loanAccount.getPrincipal(), 18);
                System.out.println("Total principal is: " + loanAccount.getPrincipal());
                System.out.println("TimeRangeInterest startMonth: " + fhr18.getStartMonth() + " endMonth: " + fhr18.getEndMonth() + " minAmount: " + fhr18.getMinimum() + " maxAmount: " + fhr18.getMaximum());
                extraInterest = fhr18.getPercentage().doubleValue();
            } else {
                LoanExternalInterest exi = li.getLoanExternalInterest();
                if (exi != null) {
                    System.out.println("External interest is: " + exi.getRate());
                    extraInterest = exi.getRate();
                }
            }
            totalPercentage += (li.getInterestRate() + extraInterest) / 12 * totalMonth;
            System.out.println("Current interest percentage: " + totalPercentage + " with totalMonth: " + totalMonth);
        }
        return totalPercentage / tenure;
    }

    @Override
    public List<LoanPaymentBreakdown> futurePaymentBreakdown(LoanAccount loanAccount) {

        System.out.println("Generating all futurePaymentBreakdown");
        EnumUtils.LoanProductType type = loanAccount.getLoanProduct().getProductType();
        if (type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR || type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL) {
            // simple interest
            System.out.println("Generating for simple interest");
            return futurePaymentBreakdownForSimpleInterest(loanAccount);
        } else if (type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB || type == EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PRIVATE_HOUSE) {
            // compound interest
            return futurePaymentBreakdownForCompoundInterest(loanAccount);
        } else {
            // suspended
            return null;
        }
    }

    private void removePreviousPaymentBreakDown(LoanAccount loanAccount) {
        System.out.println("Remove previous payment breakdown");
        for (LoanPaymentBreakdown lpb : loanAccount.getLoanPaymentBreakdown()) {
            em.remove(lpb);
        }
        em.flush();
    }

    // every repayment will remove the first one and change it to repayment record
    private List<LoanPaymentBreakdown> futurePaymentBreakdownForSimpleInterest(LoanAccount loanAccount) {

        System.out.println(loanAccount);
        
        Double outstandingLoanPrincipal = loanAccount.getOutstandingPrincipal();
        // currentPeriod starting in 0
        Date beginningDate = DateUtils.addMonths(loanAccount.getPaymentStartDate(), loanAccount.getCurrentPeriod());

        List<LoanPaymentBreakdown> futureBreakdown = new ArrayList<>();
        List<LoanInterest> loanInterests = loanAccount.getLoanProduct().getLoanInterestCollection().getLoanInterests();
        // only has one interest
        if (loanInterests.size() == 1) {
            System.out.println("Only 1 loan interest");
            LoanInterest interest = loanInterests.get(0);
            // original calculation
            Double totalPrincipal = loanAccount.getPrincipal();
            Double totalInterest = loanAccount.getPrincipal() * interest.getInterestRate() * loanAccount.getTenure();
            Double monthlyInterest = totalInterest / loanAccount.tenureInMonth();
            Double monthlyPrincipal = totalPrincipal / loanAccount.tenureInMonth();
            // TODO: check if this is working
            removePreviousPaymentBreakDown(loanAccount);
            // insert breakdown
            for (int i = loanAccount.getCurrentPeriod(); i < loanAccount.tenureInMonth(); i++) {
                System.out.println("Creating breakdown for nth month: " + i);
                Date schedulePaymentDate = DateUtils.addMonths(beginningDate, i);
                LoanPaymentBreakdown lpb = new LoanPaymentBreakdown();
                lpb.setOutstandingPrincipalPayment(outstandingLoanPrincipal);
                lpb.setNthMonth(i);
                lpb.setLoanAccount(loanAccount);
                lpb.setSchedulePaymentDate(schedulePaymentDate);
                lpb.setPrincipalPayment(monthlyPrincipal);
                lpb.setInterestPayment(monthlyInterest);
                futureBreakdown.add(lpb);
                em.persist(lpb);
                outstandingLoanPrincipal = outstandingLoanPrincipal - monthlyPrincipal;
            }
            em.flush();
        } else {
            return null;
        }

        return futureBreakdown;
    }

    private List<LoanPaymentBreakdown> futurePaymentBreakdownForCompoundInterest(LoanAccount loanAccount) {

        System.out.println(loanAccount);
        
        Double outstandingLoanPrincipal = loanAccount.getOutstandingPrincipal();
        Date beginningDate = DateUtils.addMonths(loanAccount.getPaymentStartDate(), loanAccount.getCurrentPeriod());
        Double monthlyIntallment = loanAccount.getMonthlyInstallment();

        List<LoanPaymentBreakdown> futureBreakdown = new ArrayList<>();
        removePreviousPaymentBreakDown(loanAccount);
        for (int i = loanAccount.getCurrentPeriod(); i < loanAccount.tenureInMonth(); i++) {
            System.out.println("In loop: " + i);
            Double monthlyInterest = outstandingLoanPrincipal * getAverageInterest(loanAccount);
            Double monthlyPrincipal = monthlyIntallment - monthlyInterest;

            Date schedulePaymentDate = DateUtils.addMonths(beginningDate, i);
            LoanPaymentBreakdown lpb = new LoanPaymentBreakdown();
            lpb.setOutstandingPrincipalPayment(outstandingLoanPrincipal);
            lpb.setNthMonth(i);
            lpb.setLoanAccount(loanAccount);
            lpb.setSchedulePaymentDate(schedulePaymentDate);
            lpb.setPrincipalPayment(monthlyPrincipal);
            lpb.setInterestPayment(monthlyInterest);
            futureBreakdown.add(lpb);
            em.persist(lpb);
            outstandingLoanPrincipal = outstandingLoanPrincipal - monthlyPrincipal;
        }

        return futureBreakdown;
    }

    private LoanInterest getCurrentInterest(Integer currentMonth, LoanAccount loanAccount) {
        List<LoanInterest> loanInterests = loanAccount.getLoanProduct().getLoanInterestCollection().getLoanInterests();
        // only has one interest
        for (LoanInterest i : loanInterests) {
            if (currentMonth >= i.getStartMonth() - 1 && (i.getEndMonth() == -1 || currentMonth < i.getEndMonth())) {
                return i;
            }
        }
        return null;
    }
}
