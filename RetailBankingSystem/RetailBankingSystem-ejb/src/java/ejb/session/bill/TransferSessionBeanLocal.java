/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.bill.Payee;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils.PayeeType;

/**
 *
 * @author leiyang
 */
@Local
public interface TransferSessionBeanLocal {
    // transfer
    public String transferFromAccountToAccount(String fromAcc, String toAcc, BigDecimal amount);
    
    // payee
    public Payee createPayee(Payee p);
    public String deletePayee(Payee p);
    public String deletePayeeById(Long id);
    public List<Payee> getPayeeFromUserIdWithType(Long userId, PayeeType type);
    public Payee getPayeeById(Long id);
}
