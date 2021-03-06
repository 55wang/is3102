/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardProductSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.product.CashBackCardProduct;
import entity.card.product.MileCardProduct;
import entity.card.product.RewardCardProduct;
import entity.common.AuditLog;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
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
    private CardProductSessionBeanLocal cardProductSessionBean;
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
        a.setFunctionInput("Getting all card products");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    public List<RewardCardProduct> getListRewardProducts() {
        return cardProductSessionBean.getListRewardProducts();
    }

    public List<CashBackCardProduct> getListCashBackCardProducts() {
        return cardProductSessionBean.getListCashBackCardProducts();
    }

    public List<MileCardProduct> getListMileProducts() {
        return cardProductSessionBean.getListMileProducts();
    }

    public void addNewMileCreditCard() {
        try {
            mcp.setProductName(mcp.getProductName());
            mcp.setMinSpendingAmount(mcp.getMinSpendingAmount());
            mcp.setOverseaMileRate(mcp.getOverseaMileRate());
            mcp.setLocalMileRate(mcp.getLocalMileRate());

            cardProductSessionBean.createMileProduct(mcp);

            RedirectUtils.redirect(ConstantUtils.STAFF_CARD_STAFF_VIEW_CARD);

        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewMileCreditCard Error");
            System.out.println(ex);

            RedirectUtils.redirect(ConstantUtils.STAFF_CARD_CARD_CREATE_PRODUCT);
        }
    }

    public void addNewRewardCreditCard() {
        try {
            rcp.setProductName(rcp.getProductName());
            rcp.setMinSpendingAmount(rcp.getMinSpendingAmount());
            rcp.setLocalMileRate(rcp.getLocalMileRate());
            rcp.setLocalPointRate(rcp.getLocalPointRate());

            cardProductSessionBean.createRewardProduct(rcp);

            RedirectUtils.redirect(ConstantUtils.STAFF_CARD_STAFF_VIEW_CARD);
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewRewardCreditCard Error");
            System.out.println(ex);
            RedirectUtils.redirect(ConstantUtils.STAFF_CARD_CARD_CREATE_PRODUCT);
        }
    }

    public void addNewCashBackCreditCard() {
        try {
            cbcp.setProductName(cbcp.getProductName());
            cbcp.setMinSpendingAmount(cbcp.getMinSpendingAmount());
            cbcp.setPetrolCashBackRate(cbcp.getPetrolCashBackRate());
            cbcp.setGroceryCashBackRate(cbcp.getGroceryCashBackRate());
            cbcp.setDiningCashBackRate(cbcp.getDiningCashBackRate());

            cardProductSessionBean.createCashBackProduct(cbcp);

            RedirectUtils.redirect(ConstantUtils.STAFF_CARD_STAFF_VIEW_CARD);
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewCashBackCreditCard Error");
            System.out.println(ex);
            RedirectUtils.redirect(ConstantUtils.STAFF_CARD_CARD_CREATE_PRODUCT);
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
