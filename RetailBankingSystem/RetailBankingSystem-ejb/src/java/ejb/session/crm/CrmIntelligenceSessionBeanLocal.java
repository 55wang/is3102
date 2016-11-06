/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface CrmIntelligenceSessionBeanLocal {
    public Long getDepositChurnCustomer(Date startDate, Date endDate);
    public Long getCardChurnCustomer(Date startDate, Date endDate);
    public Long getLoanChurnCustomer(Date startDate, Date endDate);
    public Long getWealthChurnCustomer(Date startDate, Date endDate);
    public Double getCustomerAvgDepositSavingAmount(Date endDate);
    public Double getCustomerAvgLoanAmount(Date endDate);
    public Long getTotalCustomerAgeGroup(Integer startAge, Integer endAge);
    public Long getNewCustomerAgeGroup(Integer startAge, Integer endAge, Date startDate, Date endDate);
}
