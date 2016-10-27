/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
import entity.loan.LoanPaymentBreakdown;
import entity.loan.LoanRepaymentRecord;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface LoanPaymentSessionBeanLocal {
    public Double calculateMonthlyInstallment(LoanAccount loanAccount);
    public LoanPaymentBreakdown createLoanPaymentBreakdown(LoanPaymentBreakdown loanPaymentBreakdown);
    public LoanRepaymentRecord createLoanRepaymentRecord(LoanRepaymentRecord loanRepaymentRecord);
    public List<LoanPaymentBreakdown> futurePaymentBreakdown(LoanAccount loanAccount);
    public String loanRepaymentFromAccount(String loanAccountNumber, String depositAccountNumber, BigDecimal amount);
    public String loanLumsumPaymentFromAccount(String loanAccountNumber, String depositAccountNumber, BigDecimal amount);
    public Date getNextPaymentDateByLoanAccountNumber (String loanAccountNumber);
    public Date getPreviousPaymentDateByLoanAccountNumber (String loanAccountNumber);

}
