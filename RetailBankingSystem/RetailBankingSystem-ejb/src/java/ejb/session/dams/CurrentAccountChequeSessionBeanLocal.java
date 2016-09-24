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
    public List<Cheque> getChequeByMainAccountId(String mainAccountId);
}
