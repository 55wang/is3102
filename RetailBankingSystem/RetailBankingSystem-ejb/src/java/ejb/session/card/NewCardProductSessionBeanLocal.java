/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardOrder;
import entity.card.account.MileCardProduct;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Local
public interface NewCardProductSessionBeanLocal {

    public List<MileCardProduct> showAllMileProducts();

    public MileCardProduct getMileProductFromId(Long orderNumber);

    public void createMileProduct(MileCardProduct mcp);

}
