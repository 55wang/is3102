/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface BizIntelligenceSessionBeanLocal {
    
    //deposit and loan service
    public Long getBankTotalDepositAcct(Date startDate, Date endDate);
    public Long getBankTotalActiveDepositAcct(Date startDate, Date endDate);
    public Long getBankTotalNewDepositAcct(Date startDate, Date endDate);
    public Double getBankTotalDepositAmount(Date startDate, Date endDate);
    public Double getBankTotalDepositInterestAmount(Date startDate, Date endDate);
    
    // bad loan
    public Long getBankTotalLoanAcct(Date startDate, Date endDate);
    public Long getBankTotalNewLoanAcct(Date startDate, Date endDate);
    public Double getBankTotalLoanAmount(Date startDate, Date endDate);
    public Double getBankLoanInterestEarned(Date startDate, Date endDate);
    public Double getBankLoanInterestUnearned(Date startDate, Date endDate);
    public Long getBankTotalDefaultLoanAcct(Date startDate, Date endDate);
    
    //card service
    public Long getBankTotalCardAcct(Date startDate, Date endDate);
    public Long getBankTotalActiveCardAcct(Date startDate, Date endDate);
    public Long getBankTotalNewCardAcct(Date startDate, Date endDate);
    public Double getBankTotalCardCurrentAmount(Date startDate, Date endDate);
    public Double getBankTotalCardOutstandingAmount(Date startDate, Date endDate);
    
    //portfolio service
    public Long getBankTotalExecutedPortfolio(Date startDate, Date endDate);
    public Long getBankNewExecutedPortfolio(Date startDate, Date endDate);
    public Double getBankTotalInvestmentAmount(Date startDate, Date endDate);
    public Double getBankTotalProfitAmount(Date startDate, Date endDate);
    
    // TODO:
    //    - 360/Merlion Deposit Account
                // Bill
                // no withdraw
                // CC  
    // Deposit Account Transaction breakdown
    // Credit Card Transaction Breakdown
    // CRM: Redeem rate
    
    // category
//    public Long getTotalCustomerCase(Date startDate, Date endDate);
    // TODO: Device logging
}
