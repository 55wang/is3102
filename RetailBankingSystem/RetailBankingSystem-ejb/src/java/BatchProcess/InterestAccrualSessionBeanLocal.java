/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BatchProcess;

import entity.BankAccount;
import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author litong
 */
@Local
public interface InterestAccrualSessionBeanLocal {
    public List<BankAccount> getAccountList();
}
