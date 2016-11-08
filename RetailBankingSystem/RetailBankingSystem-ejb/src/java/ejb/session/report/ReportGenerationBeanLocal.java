/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.report;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface ReportGenerationBeanLocal {
    public boolean generateMonthlyDepositAccountTransactionReport(String accountNumber, Date startDate, Date endDate);
    public boolean generateMonthlyCreditCardAccountTransactionReport(String accountNumber, Date startDate, Date endDate);
}
