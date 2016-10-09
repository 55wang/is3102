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
 * @author litong
 */
@Stateless
public class LoanPaymentSessionBean implements LoanPaymentSessionBeanLocal {
    
    @EJB
    private LoanCalculationSessionBean loanCalculationBean;
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public LoanPaymentBreakdown createLoanPaymentBreakdown(LoanPaymentBreakdown loanPaymentBreakdown){
        try{
            em.persist(loanPaymentBreakdown);
            
            return loanPaymentBreakdown;
        }catch (Exception e) {
            System.out.println("ACACACA _ERROR");
           e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public LoanRepaymentRecord createLoanRepaymentRecord(LoanRepaymentRecord loanRepaymentRecord){
        try{
            em.persist(loanRepaymentRecord);
            
            return loanRepaymentRecord;
        }catch (Exception e) {
            System.out.println("ACACACA _ERROR");
           e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List futurePaymentBreakdown(LoanAccount loanAccount,Integer currentPeriod){
        Double outstandingLoanAmt=loanAccount.getOutstandingPrincipal();
        Double monthlyInstallment;
        Integer tenure;
        Date beginingDate;
        List<LoanPaymentBreakdown> futureBreakdown=new ArrayList<>();
        List <LoanInterest> loanInterests=loanAccount.getLoanProduct().getLoanInterests();
        for (LoanInterest i:loanInterests){
            if(i.getStartTime()<currentPeriod && i.getEndTime()>currentPeriod){
                tenure=i.getEndTime()-currentPeriod;
                beginingDate=DateUtils.addMonths(loanAccount.getPaymentDate(),currentPeriod);
                monthlyInstallment=loanCalculationBean.calculateMonthlyInstallment(i.getInterestRate(), tenure, outstandingLoanAmt);
                outstandingLoanAmt=outstandingLoanAmt-monthlyInstallment*tenure;
                List<LoanPaymentBreakdown> lpb=calculatePaymentBreakdown(outstandingLoanAmt,monthlyInstallment,tenure,i.getInterestRate(),beginingDate);
                futureBreakdown.addAll(lpb);
            }
            
            else if(currentPeriod<i.getStartTime()){
                tenure=i.getEndTime()-i.getStartTime();
                beginingDate=DateUtils.addMonths(loanAccount.getPaymentDate(),i.getStartTime());
                monthlyInstallment=loanCalculationBean.calculateMonthlyInstallment(i.getInterestRate(), tenure, outstandingLoanAmt);
                outstandingLoanAmt=outstandingLoanAmt-monthlyInstallment*tenure;
                List<LoanPaymentBreakdown> lpb=calculatePaymentBreakdown(outstandingLoanAmt,monthlyInstallment,tenure,i.getInterestRate(),beginingDate);
                futureBreakdown.addAll(lpb);
            }
        }
        
        return futureBreakdown;
        
    }

    @Override
    public List calculatePaymentBreakdown(Double loanAmt,Double monthlyInstallment,Integer tenure, Double loanInterest,Date loanDate){
        
        List<LoanPaymentBreakdown> paymentBreakdown=new ArrayList<>();
        Date schedulePaymentDate;
        Integer period;
        Double principalPayment;
        Double interestPayment;
       
        for (int i=0;i<tenure;i++){
            schedulePaymentDate=DateUtils.addMonths(loanDate, i+1);
            principalPayment=monthlyInstallment-loanAmt*loanInterest;
            loanAmt=loanAmt-principalPayment;
            interestPayment=loanAmt*loanInterest;
            period=i+1;
            LoanPaymentBreakdown lpb = new LoanPaymentBreakdown();
            lpb.setInterestPayment(interestPayment);
            lpb.setOutstandingPrincipalPayment(interestPayment);
            lpb.setPeriod(period);
            lpb.setSchedulePaymentDate(schedulePaymentDate);
            lpb.setPrincipalPayment(principalPayment);
            paymentBreakdown.add(lpb);
            em.persist(paymentBreakdown);
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
