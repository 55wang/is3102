/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.report;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.ejb.Local;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author leiyang
 */
@Local
public interface ReportGenerationBeanLocal {
    public boolean generateMonthlyDepositAccountTransactionReport(String accountNumber, Date startDate, Date endDate) throws JRException, IOException, SQLException, ClassNotFoundException ;
}
