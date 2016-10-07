/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanPaymentBreakdown;
import entity.loan.LoanRealPayment;
import java.util.*;
import javax.ejb.Stateless;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author litong
 */
@Stateless
public class LoanCalculationSessionBean implements LoanCalculationSessionBeanLocal {
    public final static Integer MAX_LOAN_AGE=65;
    public final static Integer MIN_LOAN_AGE=21;
    public final static Integer MIDDLE_HDB_LOAN_AGE=40;
    public final static Integer MIDDLE_PP_LOAN_AGE=35;
    public final static Integer MAX_HDB_LOAN_TENURE=25;
    public final static Integer MAX_PP_LOAN_TENURE=25;
    public final static Double MSR=0.3;
    public final static Double TDSR=0.6;
    public final static Double MEDIUM_INTEREST=0.035;
    
    @Override
    public Double calculateMonthlyInstallment(Double monthlyLoanInterest, Integer tenure, Double loanAmt){
       Double monthlyInstallment;
       monthlyInstallment=loanAmt*monthlyLoanInterest/(1-Math.pow((1+monthlyLoanInterest),-tenure));
       return monthlyInstallment;
    }
    
    @Override
    public Integer calculateMaxHDBTenure(Integer age, Double monthlyIncome){
        if(age<MIN_LOAN_AGE)
            return 0;
        else if(age>MAX_LOAN_AGE)
            return 0;
        else if (age >=MIN_LOAN_AGE && age<=MIDDLE_HDB_LOAN_AGE)
            return MAX_HDB_LOAN_TENURE;
        else
            return MAX_HDB_LOAN_TENURE-(age-MIDDLE_HDB_LOAN_AGE);
        }
    
    @Override
    public Integer calculateMaxPPTenure(Integer age, Double monthlyIncome){
        if(age<MIN_LOAN_AGE)
            return 0;
        else if(age>MAX_LOAN_AGE)
            return 0;
        else if (age >=MIN_LOAN_AGE && age<=MIDDLE_PP_LOAN_AGE)
            return MAX_PP_LOAN_TENURE;
        else
            return MAX_PP_LOAN_TENURE-(age-MIDDLE_PP_LOAN_AGE);
        }
    
    @Override
    public Double calculateMaxHDBLoanAmt(Double monthlyIncome,Integer age,Double otherHDBLoan, Double otherLoan){
        Integer tenure=this.calculateMaxHDBTenure(age, monthlyIncome);
        Double maxHDBMonthlyPayment=MSR*monthlyIncome;
        Double maxTotalMonthlyPayment=TDSR*monthlyIncome;
        Double monthlyInstallment;
        Double maxHDBLoanAmt;
        if(monthlyIncome<1500)
            return 0.0;
        else{
            if(maxHDBMonthlyPayment<=otherHDBLoan || maxTotalMonthlyPayment<=(otherHDBLoan+otherLoan))
                return 0.0;
            else{
                monthlyInstallment=maxHDBMonthlyPayment-otherHDBLoan;
                maxHDBLoanAmt=monthlyInstallment*MEDIUM_INTEREST/(1-Math.pow((1+MEDIUM_INTEREST),-tenure));    
                return maxHDBLoanAmt;
            }     
        }
    }
    
    @Override
    public Double calculateMaxPPLoanAmt(Double monthlyIncome,Integer age,Double otherHomeLoan, Double otherLoan){
        Integer tenure=this.calculateMaxPPTenure(age, monthlyIncome);
        Double maxTotalMonthlyPayment=TDSR*monthlyIncome;
        Double monthlyInstallment;
        Double maxPPLoanAmt;
        if(monthlyIncome<1500)
            return 0.0;
        else{
            if(maxTotalMonthlyPayment<=(otherHomeLoan+otherLoan))
                return 0.0;
            else{
                monthlyInstallment=maxTotalMonthlyPayment-otherHomeLoan-otherLoan;
                maxPPLoanAmt=monthlyInstallment*MEDIUM_INTEREST/(1-Math.pow((1+MEDIUM_INTEREST),-tenure));    
                return maxPPLoanAmt;
            }     
        }
    }
    
    @Override
    public Double calculateCarLoanAmt(Integer tenure, Double monthlyInterest,Double monthlyIncome,Integer age,Double otherLoan){
        Double maxTotalMonthlyPayment=TDSR*monthlyIncome;
        Double monthlyInstallment;
        Double maxCarLoanAmt;
        if(monthlyIncome<2000)
            return 0.0;
        else{
            if(maxTotalMonthlyPayment<=otherLoan)
                return 0.0;
            else{
                monthlyInstallment=maxTotalMonthlyPayment-otherLoan;
                maxCarLoanAmt=monthlyInstallment*monthlyInterest/(1-Math.pow((1+monthlyInterest),-tenure));    
                return maxCarLoanAmt;
            }
                
            }
    }
    
    @Override
    public List calculateRepaymentBreakdown(Double loanAmt, Integer tenure, Double loanInterest,Date loanDate){
        Double monthlyInstallment=this.calculateMonthlyInstallment(loanInterest, tenure, loanAmt);
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
            LoanPaymentBreakdown lpb = new LoanPaymentBreakdown(schedulePaymentDate,period,principalPayment,interestPayment,loanAmt);
            paymentBreakdown.add(lpb);
       }
        
       return paymentBreakdown;
        
    }
    
    @Override
    public List lumSumPayAdjustment(Integer lumSumPayment,Double outstandingLoanAmt,Integer residualTenure, Double loanInterest,Date lumSumPayDate){
        if (lumSumPayment<10000){
            System.out.println("Not enough to be considered as lum sum payment");
            return null;
        }
        else{
            List<LoanPaymentBreakdown> newPaymentBreakdown=new ArrayList<>();
        
            newPaymentBreakdown=this.calculateRepaymentBreakdown(outstandingLoanAmt-lumSumPayment, residualTenure, loanInterest, lumSumPayDate);
            return newPaymentBreakdown;
        }
    }
        
    
    public Integer transactionPeriod(Date paymentDate,Integer tenure,List<LoanPaymentBreakdown> paymentBreakdown){
        for (int i=0;i<tenure;i++){
            if (paymentBreakdown.get(i).getSchedulePaymentDate().compareTo(paymentDate)>0){
                return i+1;
            }
            else if (paymentBreakdown.get(i).getSchedulePaymentDate().compareTo(paymentDate)>0)
                return i+1;
        }
        return -1;
    }
    
    public void realPayment(Integer period, Double payment,Date transactionDate,List<LoanRealPayment> realPayment){
        Double cummulatedPayment=realPayment.get(period-1).getPayment()+payment;
        realPayment.get(period-1).setPayment(cummulatedPayment);
        realPayment.get(period-1).setTransactionDate(transactionDate);   
    }
    
    public Double penaltyCharge(Integer lateDays,Double lateAmount, Double penaltyInterest){
        Double penalty=lateAmount*penaltyInterest/365*lateDays;
        return penalty;
    }
    
}
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

