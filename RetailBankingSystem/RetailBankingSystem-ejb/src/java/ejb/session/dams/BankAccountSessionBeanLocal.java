/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.BankAccount;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface BankAccountSessionBeanLocal {

    void createAccount(BankAccount account);
    
    public List<BankAccount> showAllAccounts();
}
