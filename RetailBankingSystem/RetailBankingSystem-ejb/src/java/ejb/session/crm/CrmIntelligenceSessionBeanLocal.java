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
    public Double getCustomerChurnRate(Date startDate, Date endDate);
    public Double getCustomerAvgCLV(Date startDate, Date endDate);
    public Double getCustomerAvgAge(Date startDate, Date endDate);
    public Double getCustomerAvgDepositSavingAmount(Date startDate, Date endDate);
    public Double getCustomerAvgLoanAmount(Date startDate, Date endDate);
    
}
