/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
import entity.loan.LoanInterest;
import entity.loan.LoanProduct;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author qiuxiaqing
 */
@Stateless
public class LoanAccountSessionBean implements LoanAccountSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public LoanAccount createLoanAccount(LoanAccount loanAccount) {
        try {
            loanAccount.setLoanAccountStatus(EnumUtils.LoanAccountStatus.NEW);
            loanAccount.setCreationDate(new Date());
            loanAccount.setAccountNumber(generateLoanAccountNumber());
            loanAccount.setLoanProduct(loanAccount.getLoanProduct());
            em.persist(loanAccount);
            return loanAccount;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public LoanProduct createLoanProduct(LoanProduct loanProduct) {
        try {
            
            for (LoanInterest interest : loanProduct.getLoanInterests()) {
                interest.setLoanProduct(loanProduct);
                em.persist(interest);
            }
            em.persist(loanProduct);

            return loanProduct;
        } catch (Exception e) {
            return null;
        }
    }
    
    private String generateLoanAccountNumber() {
        for (;;) {
            String accountNumber = GenerateAccountAndCCNumber.generateLoanAccountNumber();
            LoanAccount la = em.find(LoanAccount.class, accountNumber);
            if (la == null) {
                return accountNumber;
            }
        }
    }
    
}
