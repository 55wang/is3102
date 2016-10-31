/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.MobileAccount;
import java.math.BigDecimal;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface MobileAccountSessionBeanLocal {
    public MobileAccount createMobileAccount(MainAccount ma);
    public MobileAccount updateMobileAccount(MobileAccount ma);
    public MobileAccount getMobileAccountByUserId(String userId);
    public MobileAccount getMobileAccountByMobileNumber(String number);
    public MobileAccount topupMobileAccount(MobileAccount ma, DepositAccount da, BigDecimal amount);
    public String payCCBillFromMobileAccount(String mobileNumber, String ccNumber, BigDecimal amount);
    public TransactionRecord latestTransactionFromMobileAccount(MobileAccount ma);
    public TransactionRecord latestTransactionFromMobileNumber(String mobileNumber);
    public String transferFromAccountToAccount(String from, String to, BigDecimal amount);
}
