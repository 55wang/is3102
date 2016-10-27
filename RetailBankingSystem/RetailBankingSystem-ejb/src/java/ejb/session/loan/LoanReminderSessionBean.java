/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.loan.LoanAccount;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.LoanAccountStatus;

/**
 *
 * @author qiuxiaqing
 */
@Stateless
public class LoanReminderSessionBean implements LoanReminderSessionBeanLocal {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private LoanPaymentSessionBeanLocal loanPaymentSessionBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void remindLoanPaymentForAllActiveCustomers(Date currentDate) {
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.loanAccountStatus = :status");
        q.setParameter("status", LoanAccountStatus.APPROVED);

        List<LoanAccount> las = q.getResultList();
        for (LoanAccount loanAccount : las) {
            Date paymentDate = loanPaymentSessionBean.getNextPaymentDateByLoanAccountNumber(loanAccount.getAccountNumber());
            System.out.print(loanAccount.getAccountNumber() + " " + DateUtils.normalDisplayDate(paymentDate) + " is next payment day.");

            if (DateUtils.dayDifferenceWithSign(currentDate, paymentDate) == 5) {
                emailServiceSessionBean.sendPaymentReminderEmailToCustomer(loanAccount.getMainAccount().getCustomer().getEmail(), loanAccount.getAccountNumber(), paymentDate);
                System.out.print(loanAccount.getAccountNumber() + " is notified.");
            }
        }
    }

    @Override
    public void remindLatePaymentPenaltyForAllActiveCustomers(Date currentDate) {
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.loanAccountStatus = :status");
        q.setParameter("status", LoanAccountStatus.APPROVED);

        List<LoanAccount> las = q.getResultList();
        for (LoanAccount loanAccount : las) {
            Date previousPaymentDate = loanPaymentSessionBean.getPreviousPaymentDateByLoanAccountNumber(loanAccount.getAccountNumber());
            System.out.print(loanAccount.getAccountNumber() + " " + DateUtils.normalDisplayDate(previousPaymentDate) + " is previous payment day.");
            if (DateUtils.dayDifferenceWithSign(previousPaymentDate, currentDate) == 1 && loanAccount.getOverduePayment() > 0) {
                emailServiceSessionBean.sendLatePaymentReminderEmailToCustomer(loanAccount.getMainAccount().getCustomer().getEmail(), loanAccount.getAccountNumber(), previousPaymentDate);
                System.out.print(loanAccount.getAccountNumber() + " is notified.");
            }
        }
    }

    @Override
    public void remindBadLoanForLoanOfficer() {
        Query q = em.createQuery("SELECT l FROM LoanAccount l WHERE l.loanAccountStatus = :status");
        q.setParameter("status", LoanAccountStatus.APPROVED);

        List<LoanAccount> las = q.getResultList();
        for (LoanAccount loanAccount : las) {
            
            if (loanAccount.getOverduePayment() >= (loanAccount.getMonthlyInstallment() * 3)) {
                
                emailServiceSessionBean.sendBadLoanNoticeToLoanOfficer(loanAccount);
                System.out.print("loan officer is notified with bad loan of " + loanAccount.getAccountNumber() );
            }
        }
    }
}
