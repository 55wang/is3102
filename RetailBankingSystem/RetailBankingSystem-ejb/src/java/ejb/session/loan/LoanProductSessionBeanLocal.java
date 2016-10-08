/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanCommonInterest;
import entity.loan.LoanProduct;
import javax.ejb.Local;

/**
 *
 * @author qiuxiaqing
 */
@Local
public interface LoanProductSessionBeanLocal {

    public LoanProduct createLoanProduct(LoanProduct loanProduct);

    public LoanProduct getLoanProductByProductName(String productName);

    public LoanProduct updateLoanProduct(LoanProduct loanProduct);

    public LoanCommonInterest getCommonInterestByName(String name);

    public LoanCommonInterest createCommonInterest(LoanCommonInterest lci);

    public LoanCommonInterest updateCommonInterest(LoanCommonInterest lci);

}
