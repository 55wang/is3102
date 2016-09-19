/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardOrder;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface NewCardSessionBeanLocal {

    public List<CreditCardOrder> showAllCreditCardOrder();

    public CreditCardOrder getAccountFromId(Long orderNumber);

    public void addAccount(CreditCardOrder order);
}
