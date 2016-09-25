/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.NewCardProductSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CashBackCardProduct;
import entity.card.account.MileCardProduct;
import entity.card.account.RewardCardProduct;
import entity.common.AuditLog;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "cardProductManagedBean")
@ViewScoped
public class CardProductManagedBean implements Serializable {

    @EJB
    private NewCardProductSessionBeanLocal newProductSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private MileCardProduct mcp = new MileCardProduct();
    private RewardCardProduct rcp = new RewardCardProduct();
    private CashBackCardProduct cbcp = new CashBackCardProduct();

    /**
     * Creates a new instance of CardProductManagedBean
     */
    public CardProductManagedBean() {
    }

    
    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter card_product.xhtml");
        a.setFunctionName("CardProductManagedBean @PostConstruct init()");
        a.setInput("Getting all card products");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
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

    public void addNewMileCreditCard() {
        try {
            mcp.setProductName(mcp.getProductName());
            mcp.setMinSpendingAmount(mcp.getMinSpendingAmount());
            mcp.setMinSpending(mcp.isMinSpending());
            mcp.setOverseaMileRate(mcp.getOverseaMileRate());
            mcp.setLocalMileRate(mcp.getLocalMileRate());

            newProductSessionBean.createMileProduct(mcp);

            RedirectUtils.redirect("/StaffInternalSystem/card/staff-view-card.xhtml");

        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewMileCreditCard Error");
            System.out.println(ex);

            RedirectUtils.redirect("/StaffInternalSystem/card/card-create-product.xhtml");
        }
    }

    public void addNewRewardCreditCard() {
        try {
            rcp.setProductName(rcp.getProductName());
            rcp.setMinSpendingAmount(rcp.getMinSpendingAmount());
            rcp.setMinSpending(rcp.isMinSpending());
            rcp.setLocalMileRate(rcp.getLocalMileRate());
            rcp.setLocalPointRate(rcp.getLocalPointRate());

            newProductSessionBean.createRewardProduct(rcp);

            RedirectUtils.redirect("/StaffInternalSystem/card/staff-view-card.xhtml");
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewRewardCreditCard Error");
            System.out.println(ex);
            RedirectUtils.redirect("/StaffInternalSystem/card/card-create-product.xhtml");
        }
    }

    public void addNewCashBackCreditCard() {
        try {
            cbcp.setProductName(cbcp.getProductName());
            cbcp.setMinSpendingAmount(cbcp.getMinSpendingAmount());
            cbcp.setMinSpending(cbcp.isMinSpending());
            cbcp.setPetrolCashBackRate(cbcp.getPetrolCashBackRate());
            cbcp.setGroceryCashBackRate(cbcp.getGroceryCashBackRate());
            cbcp.setDiningCashBackRate(cbcp.getDiningCashBackRate());

            newProductSessionBean.createCashBackProduct(cbcp);

            RedirectUtils.redirect("/StaffInternalSystem/card/staff-view-card.xhtml");
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewCashBackCreditCard Error");
            System.out.println(ex);
            RedirectUtils.redirect("/StaffInternalSystem/card/card-create-product.xhtml");
        }
    }

    public RewardCardProduct getRcp() {
        return rcp;
    }

    public void setRcp(RewardCardProduct rcp) {
        this.rcp = rcp;
    }

    public CashBackCardProduct getCbcp() {
        return cbcp;
    }

    public void setCbcp(CashBackCardProduct cbcp) {
        this.cbcp = cbcp;
    }

    public MileCardProduct getMcp() {
        return mcp;
    }

    public void setMcp(MileCardProduct mcp) {
        this.mcp = mcp;
    }

}
