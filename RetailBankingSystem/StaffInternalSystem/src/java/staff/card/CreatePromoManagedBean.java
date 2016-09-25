package staff.card;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.PromoProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.CommonUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createPromoManagedBean")
@ViewScoped
public class CreatePromoManagedBean implements Serializable {
    
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private List<PromoProduct> promos;
    private PromoProduct promo = new PromoProduct();
    private List<String> typeOptions = CommonUtils.getEnumList(EnumUtils.PromoType.class);
    /**
     * Creates a new instance of CreatePromoManagedBean
     */
    public CreatePromoManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        List<Object> results = utilsBean.findAll("PromoProduct");
        setPromos(new ArrayList<>());
        for (Object o : results) {
            getPromos().add((PromoProduct) o);
        }
    }
    
    public void createProduct() {
        Object result = utilsBean.persist(getPromo());
        if (result != null) {
            promos.add((PromoProduct)result);
            MessageUtils.displayInfo("New Product Created!");
            promo = new PromoProduct();
        } else {
            MessageUtils.displayError("New Product not created! Something went wrong.");
        }
    }

    /**
     * @return the promos
     */
    public List<PromoProduct> getPromos() {
        return promos;
    }

    /**
     * @param promos the promos to set
     */
    public void setPromos(List<PromoProduct> promos) {
        this.promos = promos;
    }

    /**
     * @return the promo
     */
    public PromoProduct getPromo() {
        return promo;
    }

    /**
     * @param promo the promo to set
     */
    public void setPromo(PromoProduct promo) {
        this.promo = promo;
    }

    /**
     * @return the typeOptions
     */
    public List<String> getTypeOptions() {
        return typeOptions;
    }

    /**
     * @param typeOptions the typeOptions to set
     */
    public void setTypeOptions(List<String> typeOptions) {
        this.typeOptions = typeOptions;
    }
}
