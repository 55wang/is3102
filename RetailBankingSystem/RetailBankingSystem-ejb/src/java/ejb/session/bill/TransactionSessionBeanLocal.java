/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.common.TransactionRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface TransactionSessionBeanLocal {
    
    public List<TransactionRecord> getTransactionRecordByAccountNumberStartDateEndDate(String accountNumber, Date startDate, Date endDate);
}
