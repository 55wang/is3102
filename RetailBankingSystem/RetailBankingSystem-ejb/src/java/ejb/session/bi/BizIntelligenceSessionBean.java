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
import server.utilities.EnumUtils.CardTransactionStatus;
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
    public Long getBankTotalDepositAcct(Date endDate) {
        Query q = em.createQuery("SELECT COUNT(d.accountNumber) FROM DepositAccount d WHERE d.status=:activeStatus AND d.creationDate <= :endDate");
        q.setParameter("activeStatus", StatusType.ACTIVE);
        q.setParameter("endDate", endDate);
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
    public Double getBankTotalDepositAmount(Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(d.balance) FROM DepositAccount d WHERE d.status=:activeStatus AND d.creationDate <= :endDate");
            q.setParameter("activeStatus", StatusType.ACTIVE);
            q.setParameter("endDate", endDate);
            BigDecimal totalDepositAmount = (BigDecimal) q.getSingleResult();
            return totalDepositAmount.doubleValue();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    @Override
    public Double getBankTotalDepositInterestAmount(Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(t.amount) FROM TransactionRecord t WHERE t.actionType=:intrestTransaction AND t.creationDate <= :endDate");
            q.setParameter("intrestTransaction", TransactionType.INTEREST);
            q.setParameter("endDate", endDate);
            BigDecimal totalInterestAmount = (BigDecimal) q.getSingleResult();
            return totalInterestAmount.doubleValue();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    // bad loan
    @Override
    public Long getBankTotalLoanAcct(Date endDate) {
        Query q = em.createQuery("SELECT COUNT(l.accountNumber) FROM LoanAccount l WHERE l.loanAccountStatus=:approveStatus AND l.creationDate <= :endDate");
        q.setParameter("approveStatus", LoanAccountStatus.APPROVED);
        q.setParameter("endDate", endDate);
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
    public Double getBankTotalLoanAmount(Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(l.principal) FROM LoanAccount l WHERE l.loanAccountStatus=:approveStatus AND l.creationDate <= :endDate" );
            q.setParameter("approveStatus", LoanAccountStatus.APPROVED);
            q.setParameter("endDate", endDate);
            return (Double) q.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    @Override
    public Double getBankLoanInterestEarned(Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(l.interestAccrued) FROM LoanRepaymentRecord l WHERE l.transactionDate <= :endDate" );
            q.setParameter("endDate", endDate);
            return (Double) q.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    @Override
    public Double getBankLoanInterestUnearned(Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(l.interestPayment) FROM LoanPaymentBreakdown l WHERE l.schedulePaymentDate <= :endDate" );
            q.setParameter("endDate", endDate);
            return (Double) q.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    @Override
    public Long getBankTotalDefaultLoanAcct(Date endDate) {
        Query q = em.createQuery("SELECT COUNT(l.accountNumber) FROM LoanAccount l WHERE l.overduePayment >= l.monthlyInstallment*3 AND l.loanAccountStatus=:approveStatus AND l.creationDate <= :endDate" );
        q.setParameter("approveStatus", LoanAccountStatus.APPROVED);
        q.setParameter("endDate", endDate);
        return (Long) q.getSingleResult();
    }
    
    //credit card service
    @Override
    public Long getBankTotalCardAcct(Date endDate) {
        Query q1 = em.createQuery("SELECT COUNT(cc.id) FROM CreditCardAccount cc WHERE cc.CardStatus=:activeStatus AND cc.creationDate <= :endDate");
        q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        q1.setParameter("endDate", endDate);
        return (Long) q1.getSingleResult();
    }
    
    @Override
    public Long getBankTotalActiveCardAcct(Date startDate, Date endDate) {
        Query q1 = em.createQuery("SELECT COUNT(DISTINCT(cc.id)) FROM CreditCardAccount cc, CardTransaction ct WHERE cc.CardStatus=:activeStatus AND (ct.createDate BETWEEN :startDate AND :endDate AND ct.creditCardAccount.id=cc.id)");
        q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        q1.setParameter("startDate", startDate);
        q1.setParameter("endDate", endDate);
        return (Long) q1.getSingleResult();
    }
    @Override
    public Long getBankTotalNewCardAcct(Date startDate, Date endDate) {
        Query q1 = em.createQuery("SELECT COUNT(cc.id) FROM CreditCardAccount cc WHERE cc.CardStatus=:activeStatus AND cc.creationDate BETWEEN :startDate AND :endDate");
        q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        q1.setParameter("startDate", startDate);
        q1.setParameter("endDate", endDate);
        return (Long) q1.getSingleResult();
    }
    @Override
    public Double getBankTotalCardCurrentAmount(Date endDate) {
        try{
            Query q1 = em.createQuery("SELECT SUM(cc.currentMonthAmount) FROM CreditCardAccount cc WHERE cc.CardStatus=:activeStatus AND cc.creationDate <= :endDate");
            q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
            q1.setParameter("endDate", endDate);
            return (Double) q1.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    @Override
    public Double getBankTotalCardOutstandingAmount(Date endDate) {
        try{
            Query q1 = em.createQuery("SELECT SUM(cc.outstandingAmount) FROM CreditCardAccount cc WHERE cc.CardStatus=:activeStatus AND cc.creationDate <= :endDate");
            q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
            q1.setParameter("endDate", endDate);
            return (Double) q1.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    @Override
    public Double getBankSettledTransactionAmount(Date startDate, Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(ct.amount) FROM CardTransaction ct WHERE ct.cardTransactionStatus =:settledStatus AND ct.createDate BETWEEN :startDate AND :endDate");
            q.setParameter("settledStatus", CardTransactionStatus.SETTLEDTRANSACTION);
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            return (Double) q.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    @Override
    public Long getBankNumOfBadCardAccount(Date startDate, Date endDate){
        Query q = em.createQuery("SELECT COUNT(cca.id) FROM CreditCardAccount cca WHERE cca.lastLatePaymentDate BETWEEN :startDate AND  :endDate");
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return (Long)q.getSingleResult();
    }
    
    //portfolio service
    @Override
    public Long getBankTotalWealthManagementSubsciber(){
        Query q = em.createQuery("SELECT COUNT(wms.id) FROM WealthManagementSubscriber wms" );
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Long getBankTotalExecutedPortfolio(Date endDate) {
        Query q = em.createQuery("SELECT COUNT(ip.id) FROM InvestmentPlan ip WHERE ip.status=:executedStatus AND ip.executionDate <= :endDate" );
        q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
        q.setParameter("endDate", endDate);
        return (Long) q.getSingleResult();
    }
    @Override
    public Long getBankNewExecutedPortfolio(Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT COUNT(ip.id) FROM InvestmentPlan ip WHERE ip.status=:executedStatus AND ip.executionDate BETWEEN :startDate AND :endDate" );
        q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return (Long) q.getSingleResult();
    }
    @Override
    public Double getBankTotalInvestmentAmount(Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(ip.amountOfInvestment) FROM InvestmentPlan ip WHERE ip.status=:executedStatus AND ip.executionDate <= :endDate" );
            q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
            q.setParameter("endDate", endDate);
            Long totalInvestAmount = (Long) q.getSingleResult();      
            return totalInvestAmount.doubleValue();
        }catch(Exception ex){
            return 0.0;
        }
    }
    @Override
    public Double getBankTotalProfitAmount(Date endDate) {
        try{
            Query q = em.createQuery("SELECT SUM(ip.wealthManagementSubscriber.accumulatedAdvisoryFee) FROM InvestmentPlan ip WHERE ip.status=:executedStatus AND ip.executionDate <= :endDate" );
            q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
            q.setParameter("endDate", endDate);
            return (Double) q.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
}
