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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Stateless
public class CardProductSessionBean implements CardProductSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<MileCardProduct> getListMileProducts() {
        Query q = em.createQuery("SELECT mcp FROM MileCardProduct mcp");
        return q.getResultList();
    }

    @Override
    public MileCardProduct getMileProductById(Long productId) {
        return em.find(MileCardProduct.class, productId);
    }

    @Override
    public void createMileProduct(MileCardProduct mcp) {
        em.persist(mcp);
    }

    @Override
    public List<RewardCardProduct> getListRewardProducts() {
        Query q = em.createQuery("SELECT rcp FROM RewardCardProduct rcp");
        return q.getResultList();
    }

    @Override
    public RewardCardProduct getRewardProductById(Long productId) {
        return em.find(RewardCardProduct.class, productId);
    }

    @Override
    public RewardCardProduct createRewardProduct(RewardCardProduct rcp) {
        em.persist(rcp);
        return rcp;
    }

    @Override
    public List<CashBackCardProduct> getListCashBackCardProducts() {
        Query q = em.createQuery("SELECT cbcp FROM CashBackCardProduct cbcp");
        return q.getResultList();
    }

    @Override
    public CashBackCardProduct getCashBackCardProductById(Long productId) {
        return em.find(CashBackCardProduct.class, productId);
    }

    @Override
    public void createCashBackProduct(CashBackCardProduct cbcp) {
        em.persist(cbcp);
    }

    @Override
    public List<CreditCardProduct> getListCreditCardProducts() {
        Query q = em.createQuery("SELECT ccp FROM CreditCardProduct ccp");
        return q.getResultList();
    }

    @Override
    public CreditCardProduct getCreditCardProductByProductName(String productName) {
        Query q = em.createQuery("SELECT ccp FROM CreditCardProduct ccp WHERE ccp.productName=:inPName");
        q.setParameter("inPName", productName);
        return (CreditCardProduct)q.getSingleResult();
    }
    
    @Override
    public List<PromoProduct> getPromoProductByCardType(String cardType) {
        Query q = em.createQuery("SELECT pp FROM PromoProduct pp WHERE pp.cardType = :cardType");
        q.setParameter("cardType", EnumUtils.CreditCardType.getEnum(cardType));
        return q.getResultList();
    }

}
