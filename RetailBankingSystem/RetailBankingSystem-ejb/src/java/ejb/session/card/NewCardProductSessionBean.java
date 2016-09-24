/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CashBackCardProduct;
import entity.card.account.DebitCardAccount;
import entity.card.account.MileCardProduct;
import entity.card.account.RewardCardProduct;
import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author wang
 */
@Stateless
public class NewCardProductSessionBean implements NewCardProductSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<MileCardProduct> showAllMileProducts() {
        Query q = em.createQuery("SELECT mcp FROM MileCardProduct mcp");
        return q.getResultList();
    }

    @Override
    public MileCardProduct getMileProductFromId(Long orderNumber) {
        return em.find(MileCardProduct.class, orderNumber);
    }

    @Override
    public void createMileProduct(MileCardProduct mcp) {
        em.persist(mcp);
    }

    @Override
    public List<RewardCardProduct> showAllRewardProducts() {
        Query q = em.createQuery("SELECT rcp FROM RewardCardProduct rcp");
        return q.getResultList();
    }

    @Override
    public RewardCardProduct getRewardProductFromId(Long orderNumber) {
        return em.find(RewardCardProduct.class, orderNumber);
    }

    @Override
    public void createRewardProduct(RewardCardProduct rcp) {
        em.persist(rcp);
    }

    @Override
    public List<CashBackCardProduct> showAllCashBackCardProducts() {
        Query q = em.createQuery("SELECT cbcp FROM CashBackCardProduct cbcp");
        return q.getResultList();
    }

    @Override
    public CashBackCardProduct getCashBackCardProductFromId(Long orderNumber) {
        return em.find(CashBackCardProduct.class, orderNumber);
    }

    @Override
    public void createCashBackProduct(CashBackCardProduct cbcp) {
        em.persist(cbcp);
    }

}
