/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;

import entity.loan.LoanAccount;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author litong
 */
@Stateless
@LocalBean
public class LoanBatchProcessingSessionBean {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    public void accumulateOverduePayment(){
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.loanAccountStatus = :status");
        q.setParameter("status", EnumUtils.LoanAccountStatus.APPROVED);

        List<LoanAccount> las = q.getResultList();
        for (LoanAccount loanAccount : las) {
            if(loanAccount.getOverduePayment()>0)
                loanAccount.setOverduePayment(loanAccount.getOverduePayment()+loanAccount.getOverduePayment()*loanAccount.getLoanProduct().getPenaltyInterestRate());
                em.merge(loanAccount);
        }
    }
    
    public void calculateOverduePayment(){
       
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.loanAccountStatus = :status");
        q.setParameter("status", EnumUtils.LoanAccountStatus.APPROVED);

        List<LoanAccount> las = q.getResultList();
        for (LoanAccount loanAccount : las) {
            if(DateUtils.getDayNumber(new Date())==loanAccount.getPaymentDate()){
            if(loanAccount.getAmountPaidBeforeDueDate()<loanAccount.getMonthlyInstallment()){
                loanAccount.setOverduePayment(loanAccount.getMonthlyInstallment()-loanAccount.getAmountPaidBeforeDueDate());
                em.merge(loanAccount);
            }
        }
        }
    }
    
// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
