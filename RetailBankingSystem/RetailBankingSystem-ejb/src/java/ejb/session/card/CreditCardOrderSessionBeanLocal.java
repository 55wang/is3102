/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardOrder;
import entity.customer.MainAccount;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface CreditCardOrderSessionBeanLocal {
      public List<CreditCardOrder> getAllCreditCardOrders ();
      public CreditCardOrder searchCreditCardOrderByID(String id);
      public List<CreditCardOrder> getMainAccountAllCreditCardOrders (MainAccount mainAccount);
      public CreditCardOrder searchMainAccountCreditCardOrderByID(MainAccount ma, String id);
      public Boolean cancelCreditCardOrder(CreditCardOrder cco);
}
