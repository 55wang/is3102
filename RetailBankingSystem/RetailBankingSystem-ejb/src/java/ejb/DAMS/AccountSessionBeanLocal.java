/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.DAMS;

import entity.Account;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface AccountSessionBeanLocal {

    void createAccount(Account account);
    
    public List<Account> showAllAccounts();
}
