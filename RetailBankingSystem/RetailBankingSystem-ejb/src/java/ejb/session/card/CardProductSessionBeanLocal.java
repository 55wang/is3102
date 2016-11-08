/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.product.CashBackCardProduct;
import entity.card.product.CreditCardProduct;
import entity.card.product.MileCardProduct;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface CardProductSessionBeanLocal {

    public List<MileCardProduct> getListMileProducts();

    public MileCardProduct getMileProductById(Long productId);

    public void createMileProduct(MileCardProduct mcp);

    public List<RewardCardProduct> getListRewardProducts();

    public RewardCardProduct getRewardProductById(Long productId);

    public RewardCardProduct createRewardProduct(RewardCardProduct rcp);

    public List<CashBackCardProduct> getListCashBackCardProducts();

    public CashBackCardProduct getCashBackCardProductById(Long productId);

    public void createCashBackProduct(CashBackCardProduct cbcp);

    public List<CreditCardProduct> getListCreditCardProducts();

    public CreditCardProduct getCreditCardProductByProductName(String productName);

    public List<PromoProduct> getPromoProductByCardType(String cardType);

}
