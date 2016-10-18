/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.bill.Payee;
import entity.customer.MainAccount;
import entity.customer.TransferLimits;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.PayeeType;

/**
 *
 * @author leiyang
 */
@Local
public interface TransferSessionBeanLocal {
    // transfer
    public String transferFromAccountToAccount(String fromAcc, String toAcc, BigDecimal amount);
    public TransferLimits createTransferLimits(TransferLimits t);
    public TransferLimits updateTransferLimits(TransferLimits t);
    
    public BigDecimal getTodayBankTransferAmount(MainAccount ma, EnumUtils.PayeeType inType);
    
    // payee
    public Payee createPayee(Payee p);
    public String deletePayee(Payee p);
    public String deletePayeeById(Long id);
    public List<Payee> getPayeeFromUserIdWithType(Long userId, PayeeType type);
    public Payee getPayeeById(Long id);
    
}