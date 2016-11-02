/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface LoanCalculationSessionBeanLocal {
    public Double calculateMonthlyInstallment(Double monthlyLoanInterest, Integer tenure, Double loanAmt);
    public Integer calculateMaxHDBTenure(Integer age, Double monthlyIncome);
    public Integer calculateMaxPPTenure(Integer age, Double monthlyIncome);
    public Double calculateMaxHDBLoanAmt(Double monthlyIncome,Integer age,Double otherHDBLoan, Double otherLoan);
    public Double calculateMaxPPLoanAmt(Double monthlyIncome,Integer age,Double otherHomeLoan, Double otherLoan);
    public Double calculateCarLoanAmt(Integer tenure, Double monthlyInterest,Double monthlyIncome,Integer age,Double otherLoan);
    
    // new
    public Double calculateMaxHDBMonthlyInstalment(Double monthlyIncome, Double otherLoan);
    public Double calculateMaxPPMonthlyInstalment(Double monthlyIncome, Double otherLoan);
    public Double calculateMaxHomeLoanAmt(Double monthlyInstalment, Integer tenure);
    public Double homeMarketValue(Double loanAmt, Integer numberOfHousingLoan);
    public Double calculateMaxCarLoanAmt(Double monthlyInstalment, Integer tenureYear);
    public Double calculateMaxCarPrice(Double loanAmt);
    public Double calculateCarMonthlyInstalment(Double annualInterest,Integer tenure,Double loanAmt);
    public Double calculateMaxPersonalLoanAmt(Double monthlyIncome, Double otherMonthlyCommitment);
    public Double calculatePersonalMonthlyInstalment(Double annualInterest, Integer tenure, Double loanAmt);
}
