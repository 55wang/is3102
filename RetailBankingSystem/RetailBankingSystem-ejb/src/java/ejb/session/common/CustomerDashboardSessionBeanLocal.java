/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.common;

import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface CustomerDashboardSessionBeanLocal {
    //Account Number
    public Long getCustomerTotalDepositAccountByMainAccountUserId(String userID);
    public Long getCustomerTotalLoanAccountByMainAccountUserId(String userID);
    public Long getCustomerTotalCreditCardAccountByMainAccountUserId(String userID);
    public Long getCustomerTotalExecutedInvestmentPlanByMainAccountUserId(String userID);
    
    //Amount
    public Double getCustomerTotalDepositAmountByMainAccountUserId(String userID);
    public Double getCustomerTotalLoanAmountByMainAccountUserId(String userID);
    public Double getCustomerTotalCreditCardAmountByMainAccountUserId(String userID);
    public Double getCustomerTotalExecutedInvestmentAmountByMainAccountUserId(String userID);
}
