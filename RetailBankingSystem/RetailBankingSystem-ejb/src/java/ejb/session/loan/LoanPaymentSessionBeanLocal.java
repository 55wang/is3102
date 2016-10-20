/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
import entity.loan.LoanPaymentBreakdown;
import entity.loan.LoanRepaymentRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface LoanPaymentSessionBeanLocal {
    public LoanPaymentBreakdown createLoanPaymentBreakdown(LoanPaymentBreakdown loanPaymentBreakdown);
    public LoanRepaymentRecord createLoanRepaymentRecord(LoanRepaymentRecord loanRepaymentRecord);
    public List<LoanPaymentBreakdown> calculatePaymentBreakdown(List<LoanPaymentBreakdown> paymentBreakdown, Double loanAmt,
            Integer tenure, Double loanInterest, Date loanDate);
    public List futurePaymentBreakdown(LoanAccount loanAccount,Integer currentPeriod);
//    public List lumSumPayAdjustment(Integer lumSumPayment,Double outstandingLoanAmt,Integer residualTenure, Double loanInterest,Date lumSumPayDate);
//    public Integer transactionPeriod(Date paymentDate,Integer tenure,List<LoanPaymentBreakdown> paymentBreakdown);
//    public void realPayment(Integer period, Double payment,Date transactionDate,List<LoanRealPayment> realPayment);
//    public Double penaltyCharge(Integer lateDays,Double lateAmount, Double penaltyInterest);
}
