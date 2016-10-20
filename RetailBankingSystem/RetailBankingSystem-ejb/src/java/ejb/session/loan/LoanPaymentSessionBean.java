/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
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

/**
 *
 * @author leiyang
 */
@Stateless
public class LoanPaymentSessionBean implements LoanPaymentSessionBeanLocal {

    @EJB
    private LoanCalculationSessionBeanLocal loanCalculationSessionBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public LoanPaymentBreakdown createLoanPaymentBreakdown(LoanPaymentBreakdown loanPaymentBreakdown) {
        try {
            em.persist(loanPaymentBreakdown);

            return loanPaymentBreakdown;
        } catch (Exception e) {
            System.out.println("ACACACA _ERROR");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LoanRepaymentRecord createLoanRepaymentRecord(LoanRepaymentRecord loanRepaymentRecord) {
        try {
            em.persist(loanRepaymentRecord);

            return loanRepaymentRecord;
        } catch (Exception e) {
            System.out.println("ACACACA _ERROR");
            e.printStackTrace();
            return null;
        }
    }

// current period is month unit, tenure is year unit
    @Override
    public List<LoanPaymentBreakdown> futurePaymentBreakdown(LoanAccount loanAccount, Integer currentPeriod) {
        Double outstandingLoanAmt = loanAccount.getOutstandingPrincipal();
        Double monthlyInstallment;
        Integer tenure;
        Date beginningDate = DateUtils.addMonths(loanAccount.getPaymentStartDate(), currentPeriod - 1);
        List<LoanPaymentBreakdown> futureBreakdown = new ArrayList<>();
        List<LoanInterest> loanInterests = loanAccount.getLoanProduct().getLoanInterests();
        for (LoanInterest i : loanInterests) {
            System.out.print("In loan interest loop: " + i);
            Integer endtime;
            if (i.getEndTime() == -1) {
                endtime = loanAccount.getLoanProduct().getTenure();
            } else {
                endtime = i.getEndTime();
            }
            System.out.print("Endtime:  " + endtime);

            if (i.getStartTime() < currentPeriod && endtime >= currentPeriod && i.getEndTime() != -1) {

                tenure = endtime - currentPeriod + 1;
                System.out.print("Condition1:  " + tenure);
                System.out.print("Outstanding loan amt: " + outstandingLoanAmt);
                beginningDate = DateUtils.addMonths(beginningDate, currentPeriod);
//                monthlyInstallment = loanCalculationSessionBean.calculateMonthlyInstallment(i.getInterestRate(), tenure, outstandingLoanAmt);
//                outstandingLoanAmt = outstandingLoanAmt - monthlyInstallment * tenure;
                futureBreakdown = calculatePaymentBreakdown(futureBreakdown, outstandingLoanAmt, tenure, i.getInterestRate() / 12, beginningDate);
                beginningDate = DateUtils.addMonths(beginningDate, tenure);

            } else if (i.getStartTime() < currentPeriod && i.getEndTime() == -1) {

                tenure = endtime - i.getStartTime();
                System.out.print("Condition2:  " + tenure);
                System.out.print("Outstanding loan amt: " + outstandingLoanAmt);

//                monthlyInstallment = loanCalculationSessionBean.calculateMonthlyInstallment(i.getInterestRate(), tenure, outstandingLoanAmt);
//                outstandingLoanAmt = outstandingLoanAmt - monthlyInstallment * tenure;
                futureBreakdown = calculatePaymentBreakdown(futureBreakdown, outstandingLoanAmt, tenure, i.getInterestRate() / 12, beginningDate);
                beginningDate = DateUtils.addMonths(beginningDate, tenure);

            } else {
            }
        }

        return futureBreakdown;

    }

    @Override
    public List<LoanPaymentBreakdown> calculatePaymentBreakdown(List<LoanPaymentBreakdown> paymentBreakdown, Double loanAmt,
            Integer tenure, Double loanInterest, Date loanDate) {
        Double monthlyInstallment = loanCalculationSessionBean.calculateMonthlyInstallment(loanInterest, tenure, loanAmt);
        Date schedulePaymentDate;
        Integer period;
        Double principalPayment;
        Double interestPayment;
        Integer beginningPeriod;

        if (paymentBreakdown.isEmpty()) {
            beginningPeriod = 1;
        } else {
            beginningPeriod = paymentBreakdown.get(paymentBreakdown.size() - 1).getPeriod() + 1;
        }

        System.out.print(beginningPeriod);
        for (int i = beginningPeriod; i <= tenure + beginningPeriod - 1; i++) {
            schedulePaymentDate = DateUtils.addMonths(loanDate, i - 1);

            interestPayment = loanAmt * loanInterest;
            principalPayment = monthlyInstallment - loanAmt * loanInterest;

            if (loanAmt >= principalPayment) {
                loanAmt = loanAmt - principalPayment;
            } else {
                principalPayment = loanAmt;
                loanAmt = 0.0;
            }

            period = i;
            LoanPaymentBreakdown lpb = new LoanPaymentBreakdown();
            lpb.setInterestPayment(interestPayment);
            lpb.setOutstandingPrincipalPayment(loanAmt);
            lpb.setPeriod(period);
            lpb.setSchedulePaymentDate(schedulePaymentDate);
            lpb.setPrincipalPayment(principalPayment);
            paymentBreakdown.add(lpb);
            em.persist(lpb);

        }

        return paymentBreakdown;

    }
//    
//    @Override
//    public List lumSumPayAdjustment(Integer lumSumPayment,Double outstandingLoanAmt,Integer residualTenure, Double loanInterest,Date lumSumPayDate){
//        if (lumSumPayment<10000){
//            System.out.println("Not enough to be considered as lum sum payment");
//            return null;
//        }
//        else{
//            List<LoanPaymentBreakdown> newPaymentBreakdown=new ArrayList<>();
//        
//            newPaymentBreakdown=this.calculateRepaymentBreakdown(outstandingLoanAmt-lumSumPayment, residualTenure, loanInterest, lumSumPayDate);
//            return newPaymentBreakdown;
//        }
//    }
//        
//    @Override
//    public Integer transactionPeriod(Date paymentDate,Integer tenure,List<LoanPaymentBreakdown> paymentBreakdown){
//        for (int i=0;i<tenure;i++){
//            if (paymentBreakdown.get(i).getSchedulePaymentDate().compareTo(paymentDate)>0){
//                return i+1;
//            }
//            else if (paymentBreakdown.get(i).getSchedulePaymentDate().compareTo(paymentDate)>0)
//                return i+1;
//        }
//        return -1;
//    }
//    
//    @Override
//    public void realPayment(Integer period, Double payment,Date transactionDate,List<LoanRealPayment> realPayment){
//        Double cummulatedPayment=realPayment.get(period-1).getPayment()+payment;
//        realPayment.get(period-1).setPayment(cummulatedPayment);
//        realPayment.get(period-1).setTransactionDate(transactionDate);   
//    }
//    
//    @Override
//    public Double penaltyCharge(Integer lateDays,Double lateAmount, Double penaltyInterest){
//        Double penalty=lateAmount*penaltyInterest/365*lateDays;
//        return penalty;
//    }// Add business logic below. (Right-click in editor and choose
//    // "Insert Code > Add Business Method")
}
