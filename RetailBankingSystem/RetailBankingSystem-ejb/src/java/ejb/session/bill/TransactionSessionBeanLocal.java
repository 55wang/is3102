/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.common.BillTransferRecord;
import entity.common.TransactionRecord;
import entity.common.TransferRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface TransactionSessionBeanLocal {
    
    public TransferRecord createTransferRecord(TransferRecord tr);
    public BillTransferRecord createBillTransferRecord(BillTransferRecord btr);
    public List<TransactionRecord> getTransactionRecordByAccountNumberStartDateEndDate(String accountNumber, Date startDate, Date endDate);
    
}
