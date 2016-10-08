/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.loan.LoanCalculationSessionBeanLocal;
import entity.loan.LoanPaymentBreakdown;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.util.Date;
import java.util.List;

/**
 *
 * @author litong
 */
@Named(value = "loanCalculationManagedBean")
@ViewScoped
public class LoanCalculationManagedBean implements Serializable{

    @EJB
    private LoanCalculationSessionBeanLocal localCalculationBean;
    
    /**
     * Creates a new instance of LoanCalculationManagedBean
     */
    public LoanCalculationManagedBean() {
    }
    
    public Integer maxHDBTenure(Integer age, Double monthlyIncome){
        Integer maxHDBTenure=localCalculationBean.calculateMaxHDBTenure(age, monthlyIncome);
        return maxHDBTenure;
    }
    
    public Integer maxPPTenure(Integer age, Double monthlyIncome){
        Integer maxPPTenure=localCalculationBean.calculateMaxPPTenure(age, monthlyIncome);
        return maxPPTenure;
    }
    
    public Double maxHDBLoan(Double monthlyIncome,Integer age,Double otherHDBLoan, Double otherLoan){
        Double maxHDBLoan= localCalculationBean.calculateMaxHDBLoanAmt(monthlyIncome,age,otherHDBLoan, otherLoan);
        return maxHDBLoan;
    }
    
    public Double maxPPLoan(Double monthlyIncome,Integer age,Double otherHomeLoan, Double otherLoan){
        Double maxPPLoan=localCalculationBean.calculateMaxPPLoanAmt(monthlyIncome, age, otherHomeLoan, otherLoan);
        return maxPPLoan;
    }
    
    public Double carLoanAmt(Integer tenure, Double monthlyInterest,Double monthlyIncome,Integer age,Double otherLoan){
        Double carLoanAmt=localCalculationBean.calculateCarLoanAmt(tenure, monthlyInterest, monthlyIncome, age, otherLoan);
        return carLoanAmt;
    }
    
    public Double monthlyInstallment(Double monthlyLoanInterest, Integer tenure, Double loanAmt){
        Double monthlyInstallment=localCalculationBean.calculateMonthlyInstallment(monthlyLoanInterest, tenure, loanAmt);
        return monthlyInstallment;
    }
    
//    public List repaymentBreakdown(Double loanAmt, Integer tenure, Double loanInterest,Date loanDate){
//        List<LoanPaymentBreakdown> paymentBreakdown=localCalculationBean.calculateRepaymentBreakdown(loanAmt, tenure, loanInterest, loanDate);
//        return paymentBreakdown;
//    }
//    
//    public List lumSumAdjustment(Integer lumSumPayment,Double outstandingLoanAmt,Integer residualTenure, Double loanInterest,Date lumSumPayDate){
//        List<LoanPaymentBreakdown> paymentBreakdown=localCalculationBean.lumSumPayAdjustment(lumSumPayment, outstandingLoanAmt, residualTenure, loanInterest, lumSumPayDate);
//        return paymentBreakdown;
//    }
//        
        
    }

