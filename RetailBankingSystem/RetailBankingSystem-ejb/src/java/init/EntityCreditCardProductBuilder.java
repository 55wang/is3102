/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.card.CardProductSessionBeanLocal;
import entity.card.product.CashBackCardProduct;
import entity.card.product.MileCardProduct;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author wang
 */
@LocalBean
@Stateless
public class EntityCreditCardProductBuilder {

    @EJB
    private CardProductSessionBeanLocal cardProductSessionBean;

    public RewardCardProduct initCreditCardProduct(PromoProduct demoPromoProduct) {
        MileCardProduct mca = new MileCardProduct();
        mca.setLocalMileRate(1.3);
        mca.setOverseaMileRate(2.0);
        mca.setMinSpendingAmount(2000.0);
        mca.setProductName("Merlion MileCard");
        cardProductSessionBean.createMileProduct(mca);

        mca = new MileCardProduct();
        mca.setLocalMileRate(1.5);
        mca.setOverseaMileRate(2.1);
        mca.setMinSpendingAmount(3000.0);
        mca.setProductName("Merlion MileCard2");
        cardProductSessionBean.createMileProduct(mca);

        RewardCardProduct rcp = new RewardCardProduct();
        rcp.setLocalMileRate(1.3);
        rcp.setLocalPointRate(3.0);
        rcp.setMinSpendingAmount(500.0);
        rcp.setProductName("Merlion RewardCard");
        cardProductSessionBean.createRewardProduct(rcp);

        rcp = new RewardCardProduct();
        rcp.setLocalMileRate(1.4);
        rcp.setLocalPointRate(3.1);
        rcp.setMinSpendingAmount(700.0);
        rcp.setProductName("Merlion RewardCard2");
        RewardCardProduct demoRewardCardProduct = cardProductSessionBean.createRewardProduct(rcp);

        CashBackCardProduct cbcp = new CashBackCardProduct();
        cbcp.setDiningCashBackRate(0.9);
        cbcp.setGroceryCashBackRate(1.2);
        cbcp.setPetrolCashBackRate(0.7);
        cbcp.setMinSpendingAmount(2000.0);
        cbcp.setProductName("Merlion CashBackCard");
        cardProductSessionBean.createCashBackProduct(cbcp);

        cbcp = new CashBackCardProduct();
        cbcp.setDiningCashBackRate(1.1);
        cbcp.setGroceryCashBackRate(1.3);
        cbcp.setPetrolCashBackRate(0.6);
        cbcp.setMinSpendingAmount(2000.0);
        cbcp.setProductName("Merlion CashBackCard2");
        cardProductSessionBean.createCashBackProduct(cbcp);

        return demoRewardCardProduct;
    }
}
