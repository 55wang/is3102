/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
import entity.loan.LoanProduct;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuxiaqing
 */
@Local
public interface LoanAccountSessionBeanLocal {
    public LoanAccount createLoanAccount(LoanAccount loanAccount);

    public LoanAccount getLoanAccountByAccountNumber(String accountNumber);

    public List<LoanAccount> getLoanAccountListByCustomerIndentityNumber(String identityNumber);

}
