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
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.time.DateUtils;
import server.utilities.EnumUtils.LoanAccountStatus;
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
        return em.find(LoanAccount.class, accountNumber);
    }
    
    @Override
    public List<LoanAccount> getLoanAccountByStaffUsernameAndStatus(String username, LoanAccountStatus status) {
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.loanOfficer.username = :username AND l.loanAccountStatus =:inStatus");
        q.setParameter("username", username);
        q.setParameter("inStatus", status);
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
    public List<LoanAccount> getActiveLoanAccountListByMainAccountId(Long id) {
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.mainAccount.id = :mainAccountId AND l.loanAccountStatus =:inStatus");
        q.setParameter("mainAccountId", id);
        q.setParameter("inStatus", LoanAccountStatus.APPROVED);

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
    public LoanApplication updateLoanApplication(LoanApplication loanApplication) {
        em.merge(loanApplication);
        return loanApplication;
    }
    
    @Override
    public LoanApplication getLoanApplicationById(Long id) {
        return em.find(LoanApplication.class, id);
    }
    
    @Override
    public List<LoanApplication> getLoanApplicationByStaffUsername(String username, LoanAccountStatus inStatus) {
        Query q = em.createQuery("SELECT l FROM LoanApplication l WHERE l.loanOfficer.username = :username AND l.status =:inStatus");
        q.setParameter("username", username); 
        q.setParameter("inStatus", inStatus);
        return q.getResultList();
    }
    
    @Override
    public List<LoanAdjustmentApplication> getLoanAdjustmentApplicationByStaffUsername(String username, LoanAccountStatus inStatus) {
        Query q = em.createQuery("SELECT l FROM LoanAdjustmentApplication l WHERE l.loanAccount.loanOfficer.username = :username AND l.status =:inStatus");
        q.setParameter("username", username); 
        q.setParameter("inStatus", inStatus);
        return q.getResultList();
    }
    
    @Override
    public LoanAdjustmentApplication createLoanAdjustmentApplication(LoanAdjustmentApplication loanApplication) {
        em.persist(loanApplication);
        return loanApplication;
    }
    
    @Override
    public LoanAdjustmentApplication updateLoanAdjustmentApplication(LoanAdjustmentApplication loanApplication) {
        em.merge(loanApplication);
        return loanApplication;
    }
    
    @Override
    public LoanAdjustmentApplication getLoanAdjustmentApplicationById(Long id) {
        return em.find(LoanAdjustmentApplication.class, id);
    }
    
    @Override
    public List<LoanPaymentBreakdown> getFuturePaymentBreakdownsByLoanAcountNumber(String accountNumber) {
        Query q = em.createQuery("SELECT l FROM LoanPaymentBreakdown l WHERE l.loanAccount.accountNumber = :accountNumber AND l.schedulePaymentDate > :todayDate");
        q.setParameter("accountNumber", accountNumber); 
        q.setParameter("todayDate", new Date()); 
        return q.getResultList();
    }
    
    @Override
    public LoanPaymentBreakdown getFutureNearestPaymentBreakdownsByLoanAcountNumber(String accountNumber) {
        Query q = em.createQuery("SELECT l FROM LoanPaymentBreakdown l WHERE l.loanAccount.accountNumber = :accountNumber AND l.schedulePaymentDate BETWEEN :startDate AND :endDate");
        q.setParameter("accountNumber", accountNumber); 
        q.setParameter("startDate", new Date()); 
        q.setParameter("endDate", DateUtils.addMonths(new Date(), 1)); 
        
        List<LoanPaymentBreakdown> result = q.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
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
