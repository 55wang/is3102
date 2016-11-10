/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import server.utilities.EnumUtils.LoanAccountStatus;
import server.utilities.EnumUtils.StatusType;

/**
 *
 * @author VIN-S
 */
@Stateless
public class CustomerDashboardSessionBean implements CustomerDashboardSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    //Account Number
    @Override
    public Long getCustomerTotalDepositAccountByMainAccountUserId(String userID){
        Query q = em.createQuery("SELECT COUNT(da.accountNumber) FROM DepositAccount da, MainAccount ma WHERE da.mainAccount = ma AND ma.userID =:userID AND da.status = :activeStatus");
        q.setParameter("userID", userID);
        q.setParameter("activeStatus", StatusType.ACTIVE);
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Long getCustomerTotalLoanAccountByMainAccountUserId(String userID){
        Query q = em.createQuery("SELECT COUNT(la.accountNumber) FROM LoanAccount la, MainAccount ma WHERE la.mainAccount = ma AND ma.userID =:userID AND la.loanAccountStatus = :activeStatus");
        q.setParameter("userID", userID);
        q.setParameter("activeStatus", LoanAccountStatus.APPROVED);
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Long getCustomerTotalCreditCardAccountByMainAccountUserId(String userID){
        Query q = em.createQuery("SELECT COUNT(cca.id) FROM CreditCardAccount cca, MainAccount ma WHERE cca.mainAccount = ma AND ma.userID =:userID AND cca.CardStatus = :activeStatus");
        q.setParameter("userID", userID);
        q.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Long getCustomerTotalExecutedInvestmentPlanByMainAccountUserId(String userID){
        try{
            Query q = em.createQuery("SELECT COUNT(ip.id) FROM InvestmentPlan ip, MainAccount ma WHERE ip.wealthManagementSubscriber.mainAccount = ma AND ma.userID =:userID AND ip.status = :executedStatus");
            q.setParameter("userID", userID);
            q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
            return (Long) q.getSingleResult();
        }catch(Exception ex){
            System.out.println("Not Wealth Management Subscriber.");
            return 0L;
        }
    }
    
    //Amount
    @Override
    public Double getCustomerTotalDepositAmountByMainAccountUserId(String userID){
        try{
            Query q = em.createQuery("SELECT SUM(da.balance) FROM DepositAccount da, MainAccount ma WHERE da.mainAccount = ma AND ma.userID =:userID AND da.status = :activeStatus");
            q.setParameter("userID", userID);
            q.setParameter("activeStatus", StatusType.ACTIVE);
            BigDecimal tempTotalInvest = (BigDecimal) q.getSingleResult();
            return tempTotalInvest.doubleValue();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    @Override
    public Double getCustomerTotalLoanAmountByMainAccountUserId(String userID){
        try{
            Query q = em.createQuery("SELECT SUM(la.principal) FROM LoanAccount la, MainAccount ma WHERE la.mainAccount = ma AND ma.userID =:userID AND la.loanAccountStatus = :activeStatus");
            q.setParameter("userID", userID);
            q.setParameter("activeStatus", LoanAccountStatus.APPROVED);
            return (Double) q.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    @Override
    public Double getCustomerTotalCreditCardAmountByMainAccountUserId(String userID){
        try{
            Query q1 = em.createQuery("SELECT SUM(cca.outstandingAmount) FROM CreditCardAccount cca, MainAccount ma WHERE cca.mainAccount = ma AND ma.userID =:userID AND cca.CardStatus = :activeStatus");
            q1.setParameter("userID", userID);
            q1.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
            Query q2 = em.createQuery("SELECT SUM(cca.currentMonthAmount) FROM CreditCardAccount cca, MainAccount ma WHERE cca.mainAccount = ma AND ma.userID =:userID AND cca.CardStatus = :activeStatus");
            q2.setParameter("userID", userID);
            q2.setParameter("activeStatus", EnumUtils.CardAccountStatus.ACTIVE);
            return (Double) q1.getSingleResult() + (Double) q2.getSingleResult();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    @Override
    public Double getCustomerTotalExecutedInvestmentAmountByMainAccountUserId(String userID){
        try{
            Query q = em.createQuery("SELECT SUM(ip.amountOfInvestment) FROM InvestmentPlan ip, MainAccount ma WHERE ip.wealthManagementSubscriber.mainAccount = ma AND ma.userID =:userID AND ip.status = :executedStatus");
            q.setParameter("userID", userID);
            q.setParameter("executedStatus", InvestmentPlanStatus.EXECUTED);
            Long investAmount = (Long) q.getSingleResult();
            return investAmount.doubleValue();
        }catch(Exception ex){
            System.out.println("Not Wealth Management Subscriber.");
            return 0.0;
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
