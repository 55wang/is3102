/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

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
    public double calculateMaxHDBLoanAmt(Double monthlyIncome,Integer age,Double otherHDBLoan, Double otherLoan);
    public double calculateMaxPPLoanAmt(Double monthlyIncome,Integer age,Double otherHomeLoan, Double otherLoan);
    public double calculateCarLoanAmt(Integer tenure, Double monthlyInterest,Double monthlyIncome,Integer age,Double otherLoan);
}
