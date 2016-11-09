/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
import entity.loan.LoanAdjustmentApplication;
import entity.loan.LoanApplication;
import entity.loan.LoanPaymentBreakdown;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Local
public interface LoanAccountSessionBeanLocal {
    public LoanAccount createLoanAccount(LoanAccount loanAccount);
    public LoanAccount updateLoanAccount(LoanAccount loanAccount);
    public LoanAccount getLoanAccountByAccountNumber(String accountNumber);
    public List<LoanAccount> getLoanAccountByStaffUsernameAndStatus(String username, EnumUtils.LoanAccountStatus status);
    public List<LoanAccount> getLoanAccountListByCustomerIndentityNumber(String identityNumber);
    public List<LoanAccount> getActiveLoanAccountListByMainAccountId(String id);
    public String closeLoanAccountByAccountNumber(String accountNumber);
    // loan application
    public LoanApplication createLoanApplication(LoanApplication loanApplication);
    public LoanApplication updateLoanApplication(LoanApplication loanApplication);
    public LoanApplication getLoanApplicationById(Long id);
    public List<LoanApplication> getLoanApplicationByStaffUsername(String username, EnumUtils.LoanAccountStatus inStatus);
    public List<LoanAdjustmentApplication> getLoanAdjustmentApplicationByStaffUsername(String username, EnumUtils.LoanAccountStatus inStatus);
    // loan adjustment
    public LoanAdjustmentApplication createLoanAdjustmentApplication(LoanAdjustmentApplication loanApplication);
    public LoanAdjustmentApplication updateLoanAdjustmentApplication(LoanAdjustmentApplication loanApplication);
    public LoanAdjustmentApplication getLoanAdjustmentApplicationById(Long id);
    
    public List<LoanPaymentBreakdown> getFuturePaymentBreakdownsByLoanAcountNumber(String accountNumber);
    public LoanPaymentBreakdown getFutureNearestPaymentBreakdownsByLoanAcountNumber(String accountNumber);
}
