/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CashBackCardProduct;
import entity.card.account.CreditCardProduct;
import entity.card.account.MileCardProduct;
import entity.card.account.RewardCardProduct;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface NewCardProductSessionBeanLocal {

    public List<MileCardProduct> showAllMileProducts();

    public MileCardProduct getMileProductFromId(Long orderNumber);

    public void createMileProduct(MileCardProduct mcp);

    public List<RewardCardProduct> showAllRewardProducts();

    public RewardCardProduct getRewardProductFromId(Long orderNumber);

    public void createRewardProduct(RewardCardProduct rcp);

    public List<CashBackCardProduct> showAllCashBackCardProducts();

    public CashBackCardProduct getCashBackCardProductFromId(Long orderNumber);

    public void createCashBackProduct(CashBackCardProduct cbcp);

    public List<CreditCardProduct> getAllCreditCardProducts();

    public CreditCardProduct getSingleCreditCardProduct(String productName);


}
