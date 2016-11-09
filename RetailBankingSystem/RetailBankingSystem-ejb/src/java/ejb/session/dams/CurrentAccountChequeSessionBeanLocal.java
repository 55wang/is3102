/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.Cheque;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface CurrentAccountChequeSessionBeanLocal {
    // If Cheque status change to transfered, withdraw from the deposit account
    public List<Cheque> getChequeByMainAccountId(String mainAccountId);
    public Cheque createCheque(Cheque c);
    public Cheque updateCheque(Cheque c);
}
