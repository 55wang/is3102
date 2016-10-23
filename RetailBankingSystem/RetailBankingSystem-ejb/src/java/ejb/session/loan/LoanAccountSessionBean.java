/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author leiyang
 */
@Stateless
public class LoanAccountSessionBean implements LoanAccountSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public LoanAccount createLoanAccount(LoanAccount loanAccount) {
        try {
            loanAccount.setAccountNumber(generateLoanAccountNumber());
            em.persist(loanAccount);
            return loanAccount;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public LoanAccount updateLoanAccount(LoanAccount loanAccount) {
        em.merge(loanAccount);
        return loanAccount;
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
    public List<LoanAccount> getLoanAccountByStaffUsername(String username) {
       Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.loanOfficer.username = :username");
        q.setParameter("username", username); 
        return q.getResultList();
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

    @Override
    public LoanApplication createLoanApplication(LoanApplication loanApplication) {
        em.persist(loanApplication);
        return loanApplication;
    }
    
    @Override
    public List<LoanApplication> getLoanApplicationByStaffUsername(String username) {
       Query q = em.createQuery("SELECT l FROM LoanApplication l WHERE l.loanOfficer.username = :username");
        q.setParameter("username", username); 
        return q.getResultList();
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
