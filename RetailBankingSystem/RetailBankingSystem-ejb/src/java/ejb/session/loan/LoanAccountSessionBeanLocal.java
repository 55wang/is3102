/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import entity.loan.LoanPaymentBreakdown;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface LoanAccountSessionBeanLocal {
    public LoanAccount createLoanAccount(LoanAccount loanAccount);
    public LoanAccount updateLoanAccount(LoanAccount loanAccount);
    public LoanAccount getLoanAccountByAccountNumber(String accountNumber);
    public List<LoanAccount> getLoanAccountByStaffUsername(String username);
    public List<LoanAccount> getLoanAccountListByCustomerIndentityNumber(String identityNumber);
    public List<LoanAccount> getActiveLoanAccountListByMainAccountId(Long id);
    public LoanApplication createLoanApplication(LoanApplication loanApplication);
    public LoanApplication updateLoanApplication(LoanApplication loanApplication);
    public LoanApplication getLoanApplicationById(Long id);
    public List<LoanApplication> getLoanApplicationByStaffUsername(String username);
    public List<LoanPaymentBreakdown> getFuturePaymentBreakdownsByLoanAcountNumber(String accountNumber);
    public LoanPaymentBreakdown getFutureNearestPaymentBreakdownsByLoanAcountNumber(String accountNumber);
}
