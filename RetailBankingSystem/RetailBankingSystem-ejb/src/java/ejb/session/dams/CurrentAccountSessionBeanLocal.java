/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.CurrentAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface CurrentAccountSessionBeanLocal {
    
    void createAccount(CurrentAccount account);
    
    public CurrentAccount getAccountFromId(Long id);
            
    public long showNumberOfAccounts();
    
    // TODO: For internal testing only, not for demo
    public List<CurrentAccount> showAllAccounts();
    
    public String depositIntoAccount(Long accountNumber, BigDecimal depositAmount);
}
