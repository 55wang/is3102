/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author qiuxiaqing
 */
@Local
public interface LoanReminderSessionBeanLocal {
    public void remindLoanPaymentForAllActiveCustomers(Date currentDate);
    public void remindLatePaymentPenaltyForAllActiveCustomers(Date currentDate);
    public void remindBadLoanForLoanOfficer();

}
