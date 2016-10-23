/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanExternalInterest;
import entity.loan.LoanInterest;
import entity.loan.LoanInterestCollection;
import entity.loan.LoanProduct;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface LoanProductSessionBeanLocal {
    public LoanProduct createLoanProduct(LoanProduct loanProduct);
    public LoanProduct getLoanProductByProductName(String productName);
    public LoanProduct updateLoanProduct(LoanProduct loanProduct);
    public List<LoanProduct> getAllLoanProduct();
    public LoanExternalInterest getCommonInterestByName(String name);
    public LoanExternalInterest createCommonInterest(LoanExternalInterest lci);
    public LoanExternalInterest updateCommonInterest(LoanExternalInterest lci);
    public LoanInterest createLoanInterest(LoanInterest li);
    public LoanInterest updateLoanInterest(LoanInterest li);
    public LoanInterestCollection createInterestCollection(LoanInterestCollection lic);
    public LoanInterestCollection updateInterestCollection(LoanInterestCollection lic);
    public LoanInterestCollection getInterestCollectionById(Long id);
    public LoanExternalInterest getSIBORInterest();
    public List<LoanInterest> getAllLoanInterest();
    public List<LoanInterestCollection> getAllPersonalLoanInterestCollection();
    public List<LoanInterestCollection> getAllCarLoanInterestCollection();
    public List<LoanInterestCollection> getAllHDBLoanInterestCollection();
    public List<LoanInterestCollection> getAllPPLoanInterestCollection();
}
