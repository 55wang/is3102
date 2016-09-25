/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
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
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "cardViewProductManagedBean")
@ViewScoped
public class CardViewProductManagedBean implements Serializable {

    /**
     * Creates a new instance of CardViewProductManagedBean
     */
    @EJB
    NewCardProductSessionBeanLocal newCardProductSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
    private List<MileCardProduct> mcps;
    private List<RewardCardProduct> rcps;
    private List<CashBackCardProduct> cbcps;

    public CardViewProductManagedBean() {
    }

    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter view_card_product.xhtml");
        a.setFunctionName("CardViewProductManagedBean @PostConstruct init()");
        a.setInput("Getting all credit card products");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
        displayCards();
    }
    public void displayCards() {
        mcps = newCardProductSessionBean.showAllMileProducts();
        rcps = newCardProductSessionBean.showAllRewardProducts();
        cbcps = newCardProductSessionBean.showAllCashBackCardProducts();
    }

    public List<MileCardProduct> getMcps() {
        return mcps;
    }

    public void setMcps(List<MileCardProduct> mcps) {
        this.mcps = mcps;
    }

    public List<RewardCardProduct> getRcps() {
        return rcps;
    }

    public void setRcps(List<RewardCardProduct> rcps) {
        this.rcps = rcps;
    }

    public List<CashBackCardProduct> getCbcps() {
        return cbcps;
    }

    public void setCbcp(List<CashBackCardProduct> cbcps) {
        this.cbcps = cbcps;
    }

}
