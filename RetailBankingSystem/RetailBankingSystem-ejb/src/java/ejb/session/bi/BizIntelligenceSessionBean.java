/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bi;

import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import server.utilities.EnumUtils.LoanAccountStatus;
import server.utilities.EnumUtils.StatusType;
import server.utilities.EnumUtils.TransactionType;

/**
 *
 * @author wang
 */
@Stateless
public class BizIntelligenceSessionBean implements BizIntelligenceSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    //deposit and loan service
    @Override
    public Long getBankTotalDepositAcct(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT COUNT(d.accountNumber) FROM DepositAccount d "
                + "WHERE d.status=:activeStatus");
        q.setParameter("activeStatus", StatusType.ACTIVE);
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Long getBankTotalActiveDepositAcct(Date startDate, Date endDate) {
        System.out.println(startDate.toString());
        System.out.println(endDate.toString());
        Query q = em.createQuery("SELECT COUNT(DISTINCT(d.accountNumber)) FROM DepositAccount d, TransactionRecord t1, TransactionRecord t2 WHERE d.status=:activeStatus AND ((t1.creationDate BETWEEN :startDate AND :endDate AND t1.fromAccount.accountNumber=d.accountNumber)  OR (t2.creationDate BETWEEN :startDate AND :endDate AND t2.toAccount.accountNumber=d.accountNumber))");
        q.setParameter("activeStatus", StatusType.ACTIVE);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Long getBankTotalNewDepositAcct(Date startDate, Date endDate) {
        System.out.println(startDate.toString());
        System.out.println(endDate.toString());
        Query q = em.createQuery("SELECT COUNT(d.accountNumber) FROM DepositAccount d "
                + "WHERE d.status=:activeStatus "
                + "AND d.creationDate BETWEEN :startDate AND :endDate");
        q.setParameter("activeStatus", StatusType.ACTIVE);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Double getBankTotalDepositAmount(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT SUM(d.balance) FROM DepositAccount d WHERE d.status=:activeStatus");
        q.setParameter("activeStatus", StatusType.ACTIVE);
        BigDecimal totalDepositAmount = (BigDecimal) q.getSingleResult();
        return totalDepositAmount.doubleValue();
    }
    
    @Override
    public Double getBankTotalDepositInterestAmount(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT SUM(t.amount) FROM TransactionRecord t WHERE t.actionType=:intrestTransaction");
        q.setParameter("intrestTransaction", TransactionType.INTEREST);
        BigDecimal totalInterestAmount = (BigDecimal) q.getSingleResult();
        return totalInterestAmount.doubleValue();
    }
    
    // bad loan
    @Override
    public Long getBankTotalLoanAcct(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT COUNT(l.accountNumber) FROM LoanAccount l "
                + "WHERE l.loanAccountStatus=:approveStatus");
        q.setParameter("approveStatus", LoanAccountStatus.APPROVED);
        return (Long) q.getSingleResult();
    }
    @Override
    public Long getBankTotalNewLoanAcct(Date startDate, Date endDate) {
        System.out.println(startDate.toString());
        System.out.println(endDate.toString());
        Query q = em.createQuery("SELECT COUNT(l.accountNumber) FROM LoanAccount l "
                + "WHERE l.loanAccountStatus=:approveStatus "
                + "AND l.creationDate BETWEEN :startDate AND :endDate");
        q.setParameter("approveStatus", LoanAccountStatus.APPROVED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return (Long) q.getSingleResult();
    }
    @Override
    public Double getBankTotalLoanAmount(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT SUM(l.principal) FROM LoanAccount l WHERE l.loanAccountStatus=:approveStatus" );
        q.setParameter("approveStatus", LoanAccountStatus.APPROVED);
        return (Double) q.getSingleResult();
    }
    @Override
    public Double getBankLoanInterestEarned(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT SUM(l.interestAccrued) FROM LoanRepaymentRecord l" );
        return (Double) q.getSingleResult();
    }
    @Override
    public Double getBankLoanInterestUnearned(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT SUM(l.interestPayment) FROM LoanPaymentBreakdown l" );
        return (Double) q.getSingleResult();
    }
    @Override
    public Long getBankTotalDefaultLoanAcct(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT COUNT(l.accountNumber) FROM LoanAccount l WHERE l.overduePayment >= l.monthlyInstallment*3 AND l.loanAccountStatus=:approveStatus" );
        q.setParameter("approveStatus", LoanAccountStatus.APPROVED);
        return (Long) q.getSingleResult();
    }
    
    //card service
    @Override
    public Long getBankTotalCardAcct(Date startDate, Date endDate) {
        Query q1 = em.createQuery("SELECT COUNT(cc.id) FROM CreditCardAccount cc WHERE cc.CardStatus=:activeStatus");
        q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        Query q2 = em.createQuery("SELECT COUNT(dc.id) FROM DebitCardAccount dc WHERE dc.CardStatus=:activeStatus");
        q2.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        return (Long) q1.getSingleResult() + (Long) q2.getSingleResult();
    }
    
    @Override
    public Long getBankTotalActiveCardAcct(Date startDate, Date endDate) {
        Query q1 = em.createQuery("SELECT COUNT(DISTINCT(cc.id)) FROM CreditCardAccount cc, CardTransaction ct WHERE cc.CardStatus=:activeStatus AND (ct.createDate BETWEEN :startDate AND :endDate AND ct.creditCardAccount.id=cc.id)");
        q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        q1.setParameter("startDate", startDate);
        q1.setParameter("endDate", endDate);
        Query q2 = em.createQuery("SELECT COUNT(DISTINCT(dc.id)) FROM DebitCardAccount dc, CardTransaction ct WHERE dc.CardStatus=:activeStatus AND (dc.creationDate BETWEEN :startDate AND :endDate AND ct.debitCardAccount.id=dc.id)");
        q2.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        q2.setParameter("startDate", startDate);
        q2.setParameter("endDate", endDate);
        return (Long) q1.getSingleResult() + (Long) q2.getSingleResult();
    }
    @Override
    public Long getBankTotalNewCardAcct(Date startDate, Date endDate) {
        Query q1 = em.createQuery("SELECT COUNT(cc.id) FROM CreditCardAccount cc WHERE cc.CardStatus=:activeStatus AND cc.creationDate BETWEEN :startDate AND :endDate");
        q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        q1.setParameter("startDate", startDate);
        q1.setParameter("endDate", endDate);
        Query q2 = em.createQuery("SELECT COUNT(dc.id) FROM DebitCardAccount dc WHERE dc.CardStatus=:activeStatus AND dc.creationDate BETWEEN :startDate AND :endDate");
        q2.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        q2.setParameter("startDate", startDate);
        q2.setParameter("endDate", endDate);
        return (Long) q1.getSingleResult() + (Long) q2.getSingleResult();
    }
    @Override
    public Double getBankTotalCardCurrentAmount(Date startDate, Date endDate) {
        return 0.0;
    }
    @Override
    public Double getBankTotalCardOutstandingAmount(Date startDate, Date endDate) {
        return 0.0;
    }
    
    //portfolio service
    @Override
    public Long getBankTotalExecutedPortfolio(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT COUNT(ip.id) FROM InvestmentPlan ip WHERE ip.status=:executedStatus" );
        q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
        return (Long) q.getSingleResult();
    }
    @Override
    public Long getBankNewExecutedPortfolio(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT COUNT(ip.id) FROM InvestmentPlan ip WHERE ip.status=:executedStatus AND ip.creationDate BETWEEN :startDate AND :endDate" );
        q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return (Long) q.getSingleResult();
    }
    @Override
    public Double getBankTotalInvestmentAmount(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT SUM(ip.amountOfInvestment) FROM InvestmentPlan ip WHERE ip.status=:executedStatus" );
        q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
        Long totalInvestAmount = (Long) q.getSingleResult();
        return totalInvestAmount.doubleValue();
    }
    @Override
    public Double getBankTotalProfitAmount(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT SUM(ip.wealthManagementSubscriber.accumulatedAdvisoryFee) FROM InvestmentPlan ip WHERE ip.status=:executedStatus" );
        q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
        return (Double) q.getSingleResult();
    }
}
