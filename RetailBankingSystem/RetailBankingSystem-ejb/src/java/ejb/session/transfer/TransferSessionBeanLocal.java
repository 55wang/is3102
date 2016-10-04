/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.transfer;

import java.math.BigDecimal;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface TransferSessionBeanLocal {
    public String transferFromAccountToAccount(String fromAcc, String toAcc, BigDecimal amount);
}
