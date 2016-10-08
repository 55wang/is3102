/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.customer.Customer;
import entity.loan.LoanAccount;
import entity.loan.LoanCommonInterest;
import entity.loan.LoanInterest;
import entity.loan.LoanProduct;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public LoanAccount getLoanAccountByAccountNumber(String accountNumber) {
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.accountNumber = :accountNumber");

        q.setParameter("accountNumber", accountNumber);

        try {
            return (LoanAccount) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<LoanAccount> getLoanAccountListByCustomerIndentityNumber(String identityNumber) {
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.mainAccount.customer.identityNumber = :identityNumber");
        q.setParameter("identityNumber", identityNumber);

        try {
            return q.getResultList();
        } catch (Exception ex) {
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
