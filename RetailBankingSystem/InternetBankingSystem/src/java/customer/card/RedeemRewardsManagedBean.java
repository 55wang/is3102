/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardProduct;
import entity.card.account.PromoCode;
import entity.card.account.PromoProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
    private CardAcctSessionBeanLocal cardBean;
    @EJB
    private UtilsSessionBeanLocal utilBean;
    private String creditCardId;
    private List<PromoProduct> products;
    private CreditCardAccount cca;

    /**
     * Creates a new instance of RedeemRewardsManagedBean
     */
    public RedeemRewardsManagedBean() {
    }

    public void init() {

        cca = cardBean.getCardAccountFromId(Long.parseLong(getCreditCardId()));
        System.out.println(cca.getCreditCardNum());

        setProducts(new ArrayList<>());
        List<Object> results = utilBean.findAll("PromoProduct");
        for (Object o : results) {
            getProducts().add((PromoProduct) o);
        }

    }

    public void redeem(PromoProduct p) {
        Double currentPoint = cca.getMerlionPoints();
        System.out.println("Current Point is:" + currentPoint);
        Double productPoint = p.getPoints().doubleValue();
        System.out.println("product Point is:" + productPoint);
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
     * @return the products
     */
    public List<PromoProduct> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<PromoProduct> products) {
        this.products = products;
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

}
