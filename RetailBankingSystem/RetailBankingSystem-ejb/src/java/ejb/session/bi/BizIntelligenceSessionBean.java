/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bi;

import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        System.out.println(startDate.toString());
        System.out.println(endDate.toString());
        Query q = em.createNativeQuery("SELECT count(*) FROM DepositAccount");
        return (Long) q.getSingleResult();
    }
    
    @Override
    public Long getBankTotalActiveDepositAcct(Date startDate, Date endDate) {
        return 0L;
    }
    
    @Override
    public Long getBankTotalNewDepositAcct(Date startDate, Date endDate) {
        return 0L;
    }
    
    @Override
    public Double getBankTotalDepositAmount(Date startDate, Date endDate) {
        return 0.0;
    }
    
    @Override
    public Double getBankTotalDepositInterestAmount(Date startDate, Date endDate) {
        return 0.0;
    }
    
    // bad loan
    @Override
    public Long getBankTotalLoanAcct(Date startDate, Date endDate) {
        return 0L;
    }
    @Override
    public Long getBankTotalNewLoanAcct(Date startDate, Date endDate) {
        return 0L;
    }
    @Override
    public Double getBankTotalLoanAmount(Date startDate, Date endDate) {
        return 0.0;
    }
    @Override
    public Double getBankLoanInterestEarned(Date startDate, Date endDate) {
        return 0.0;
    }
    @Override
    public Double getBankLoanInterestUnearned(Date startDate, Date endDate) {
        return 0.0;
    }
    @Override
    public Long getBankTotalDefaultLoanAcct(Date startDate, Date endDate) {
        return 0L;
    }
    
    //card service
    @Override
    public Long getBankTotalCardAcct(Date startDate, Date endDate) {
        return 0L;
    }
    @Override
    public Long getBankTotalActiveCardAcct(Date startDate, Date endDate) {
        return 0L;
    }
    @Override
    public Long getBankTotalNewCardAcct(Date startDate, Date endDate) {
        return 0L;
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
        return 0L;
    }
    @Override
    public Long getBankNewExecutedPortfolio(Date startDate, Date endDate) {
        return 0L;
    }
    @Override
    public Double getBankTotalInvestmentAmount(Date startDate, Date endDate) {
        return 0.0;
    }
    @Override
    public Double getBankTotalProfitAmount(Date startDate, Date endDate) {
        return 0.0;
    }
}
