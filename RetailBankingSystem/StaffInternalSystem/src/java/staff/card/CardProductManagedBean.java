/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.NewCardProductSessionBeanLocal;
import entity.card.account.CashBackCardProduct;
import entity.card.account.MileCardProduct;
import entity.card.account.RewardCardProduct;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Named(value = "cardProductManagedBean")
@ViewScoped
public class CardProductManagedBean implements Serializable {

    @EJB
    private NewCardProductSessionBeanLocal newProductSessionBean;

    /**
     * Creates a new instance of CardProductManagedBean
     */
    public CardProductManagedBean() {
    }

    public List<RewardCardProduct> showAllRewardProducts() {
        return newProductSessionBean.showAllRewardProducts();
    }

    public List<CashBackCardProduct> showAllCashBackCardProducts() {
        return newProductSessionBean.showAllCashBackCardProducts();
    }

    public List<MileCardProduct> showAllMileProducts() {
        return newProductSessionBean.showAllMileProducts();
    }

    public String addNewMileCreditCard(String productName, double minSpendingAmount,
            boolean minSpending, double overseaMileRate, double localMileRate) {
        try {
            MileCardProduct mcp = new MileCardProduct();
            mcp.setProductName(productName);
            mcp.setMinSpendingAmount(minSpendingAmount);
            mcp.setMinSpending(minSpending);
            mcp.setOverseaMileRate(overseaMileRate);
            mcp.setLocalMileRate(localMileRate);

            newProductSessionBean.createMileProduct(mcp);

            return "SUCCESS"; //set the return to webpage path
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewMileCreditCard Error");
            System.out.println(ex);
            return "FAIL";
        }
    }

    public String addNewRewardCreditCard(String productName, double minSpendingAmount,
            boolean minSpending, double localMileRate, double localPointRate) {
        try {
            RewardCardProduct rcp = new RewardCardProduct();
            rcp.setProductName(productName);
            rcp.setMinSpendingAmount(minSpendingAmount);
            rcp.setMinSpending(minSpending);
            rcp.setLocalMileRate(localMileRate);
            rcp.setLocalPointRate(localPointRate);

            newProductSessionBean.createRewardProduct(rcp);

            return "SUCCESS"; //set the return to webpage path
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewRewardCreditCard Error");
            System.out.println(ex);
            return "FAIL";
        }
    }

    public String addNewCashBackCreditCard(String productName, double minSpendingAmount,
            boolean minSpending, double petrolCashBackRate, double groceryCashBackRate,
            double diningCashBackRate) {
        try {
            CashBackCardProduct cbcp = new CashBackCardProduct();
            cbcp.setProductName(productName);
            cbcp.setMinSpendingAmount(minSpendingAmount);
            cbcp.setMinSpending(minSpending);
            cbcp.setPetrolCashBackRate(petrolCashBackRate);
            cbcp.setGroceryCashBackRate(groceryCashBackRate);
            cbcp.setDiningCashBackRate(diningCashBackRate);

            newProductSessionBean.createCashBackProduct(cbcp);

            return "SUCCESS"; //set the return to webpage path
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewCashBackCreditCard Error");
            System.out.println(ex);
            return "FAIL";
        }
    }

}
