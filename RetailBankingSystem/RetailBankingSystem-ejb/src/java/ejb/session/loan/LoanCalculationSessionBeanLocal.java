/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import java.util.*;
import javax.ejb.Local;

/**
 *
 * @author litong
 */
@Local
public interface LoanCalculationSessionBeanLocal {
    
    public Double calculateMonthlyInstallment(Double monthlyLoanInterest, Integer tenure, Double loanAmt);
    public Integer calculateMaxHDBTenure(Integer age, Double monthlyIncome);
    public Integer calculateMaxPPTenure(Integer age, Double monthlyIncome);
    public Double calculateMaxHDBLoanAmt(Double monthlyIncome,Integer age,Double otherHDBLoan, Double otherLoan);
    public Double calculateMaxPPLoanAmt(Double monthlyIncome,Integer age,Double otherHomeLoan, Double otherLoan);
    public Double calculateCarLoanAmt(Integer tenure, Double monthlyInterest,Double monthlyIncome,Integer age,Double otherLoan);
    public List calculateRepaymentBreakdown(Double loanAmt, Integer tenure, Double loanInterest,Date loanDate);
    public List lumSumPayAdjustment(Integer lumSumPayment,Double outstandingLoanAmt,Integer residualTenure, Double loanInterest,Date lumSumPayDate);
}
