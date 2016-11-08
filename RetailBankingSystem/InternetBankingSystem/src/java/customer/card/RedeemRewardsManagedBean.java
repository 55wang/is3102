/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardProductSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.card.product.MileCardProduct;
import entity.card.product.PromoCode;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.PincodeGenerationUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "redeemRewardsManagedBean")
@ViewScoped
public class RedeemRewardsManagedBean implements Serializable {

    @EJB
    private CardProductSessionBeanLocal cardProductSessionBean;

    @EJB
    private CardAcctSessionBeanLocal cardBean;
    @EJB
    private UtilsSessionBeanLocal utilBean;
    private String creditCardId;
    private List<PromoProduct> rewardProducts;
    private List<PromoProduct> mileProducts;
    private CreditCardAccount cca;
    private String cardType;

    /**
     * Creates a new instance of RedeemRewardsManagedBean
     */
    public RedeemRewardsManagedBean() {
    }

    public void init() {
        System.out.println(creditCardId);
        cca = cardBean.getCardAccountById(Long.parseLong(creditCardId));
        if (cca.getCreditCardProduct().getCartType().equals(EnumUtils.CreditCardType.MILE)) {
            setMileProducts(cardProductSessionBean.getPromoProductByCardType("MILE"));
        } else if (cca.getCreditCardProduct() instanceof RewardCardProduct) {
            setRewardProducts(cardProductSessionBean.getPromoProductByCardType("REWARD"));
        } else {
        }

    }

    public Double getPoints() {
        if (getCardType().equals("MILE")) {
            return cca.getMerlionMiles();
        } else {
            return cca.getMerlionPoints();
        }
    }

    public void redeemReward(PromoProduct p) {

        Double currentPoint = cca.getMerlionPoints();
        System.out.println("Current Point is:" + currentPoint);
        Double productPoint = p.getPoints();
        System.out.println("Product Point is:" + productPoint);
        if (productPoint > currentPoint) {
            MessageUtils.displayError("Not enough points!");
        } else {
            System.out.println("Start ");
            cca.deductPoints(productPoint);
            PromoCode pc = new PromoCode();
            pc.setProduct(p);
            pc.setPromotionCode(PincodeGenerationUtils.generateRandom(false, 8));
            pc.setCreditCardAccount(cca);
            cca.addPromoCode(pc);
            Object result = utilBean.merge(cca);
            System.out.println("Success!!");
            if (result != null) {
                MessageUtils.displayInfo("Successfully redeemed!");
            } else {
                MessageUtils.displayError("Something went wrong!");
            }
        }

    }

    public void redeemMile(PromoProduct p) {

        Double currentPoint = cca.getMerlionMiles();
        System.out.println("Current Point is:" + currentPoint);
        Double productPoint = p.getPoints();
        System.out.println("Product Point is:" + productPoint);
        if (productPoint > currentPoint) {
            MessageUtils.displayError("Not enough points!");
        } else {
            System.out.println("Start ");
            cca.deductMiles(productPoint);
            PromoCode pc = new PromoCode();
            pc.setProduct(p);
            pc.setPromotionCode(PincodeGenerationUtils.generateRandom(false, 8));
            pc.setCreditCardAccount(cca);
            cca.addPromoCode(pc);
            Object result = utilBean.merge(cca);
            System.out.println("Success!!");
            if (result != null) {
                MessageUtils.displayInfo("Successfully redeemed!");
            } else {
                MessageUtils.displayError("Something went wrong!");
            }
        }

    }

    /**
     * @return the creditCardId
     */
    public String getCreditCardId() {
        return creditCardId;
    }

    /**
     * @param creditCardId the creditCardId to set
     */
    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    /**
     * @return the cca
     */
    public CreditCardAccount getCca() {
        return cca;
    }

    /**
     * @param cca the cca to set
     */
    public void setCca(CreditCardAccount cca) {
        this.cca = cca;
    }

    /**
     * @return the rewardProducts
     */
    public List<PromoProduct> getRewardProducts() {
        return rewardProducts;
    }

    /**
     * @param rewardProducts the rewardProducts to set
     */
    public void setRewardProducts(List<PromoProduct> rewardProducts) {
        this.rewardProducts = rewardProducts;
    }

    /**
     * @return the mileProducts
     */
    public List<PromoProduct> getMileProducts() {
        return mileProducts;
    }

    /**
     * @param mileProducts the mileProducts to set
     */
    public void setMileProducts(List<PromoProduct> mileProducts) {
        this.mileProducts = mileProducts;
    }

    /**
     * @return the cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * @param cardType the cardType to set
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

}
