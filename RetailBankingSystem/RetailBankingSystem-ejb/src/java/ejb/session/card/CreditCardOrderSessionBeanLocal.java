/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardAccount;
import entity.card.order.CreditCardOrder;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils;

/**
 *
 * @author VIN-S
 */
@Local
public interface CreditCardOrderSessionBeanLocal {

    public List<CreditCardOrder> getListCreditCardOrders();

    public List<CreditCardOrder> getListCreditCardOrdersByNotCancelStatus();

    public List<CreditCardOrder> getListCreditCardOrdersByMainIdAndNotCancelStatus(String mainAccountId);

    public CreditCardOrder getCreditCardOrderById(Long ccoId);

    public CreditCardOrder getCreditCardOrderByIdMainId(Long ccoId, String mainId);

    public CreditCardOrder updateCreditCardOrderStatus(CreditCardOrder cco, EnumUtils.CardApplicationStatus status);

    public List<CreditCardAccount> getListCreditCardOrdersByPendingStatus();

    public List<CreditCardOrder> getListCreditCardOrdersByApplicationStatus(EnumUtils.CardApplicationStatus applicationStatus);

    public CreditCardOrder createCardOrder(CreditCardOrder order);

    public CreditCardOrder updateCreditCardOrder(CreditCardOrder cco);
}
