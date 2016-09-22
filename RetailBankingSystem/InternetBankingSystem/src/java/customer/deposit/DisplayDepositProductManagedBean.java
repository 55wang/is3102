/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.deposit;

import ejb.session.dams.DepositProductSessionBeanLocal;
import entity.dams.account.DepositAccountProduct;
import entity.dams.account.FixedDepositAccountProduct;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author leiyang
 */
@Named(value = "displayDepositProductManagedBean")
@ViewScoped
public class DisplayDepositProductManagedBean implements Serializable {

    @EJB
    private DepositProductSessionBeanLocal dpBean;
    
    private Boolean hasCurrentDepositProduct;
    private Boolean hasSavingsDepositProduct;
    private Boolean hasTimeDepositProduct;
    private Boolean hasCustomDepositProduct;
    
    private List<DepositAccountProduct> currentDepositProducts;
    private List<DepositAccountProduct> savingsDepositProducts;
    private List<DepositAccountProduct> customDepositProducts;
    private List<FixedDepositAccountProduct> fixedDepositProducts;
    
    /**
     * Creates a new instance of DisplayDepositProductManagedBean
     */
    public DisplayDepositProductManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        currentDepositProducts = dpBean.getAllPresentCurrentDepositProducts();
        hasCurrentDepositProduct = currentDepositProducts != null && !currentDepositProducts.isEmpty();
        savingsDepositProducts = dpBean.getAllPresentSavingsDepositProducts();
        hasSavingsDepositProduct = savingsDepositProducts != null && !savingsDepositProducts.isEmpty();
        customDepositProducts = dpBean.getAllPresentCustomDepositProducts();
        hasCustomDepositProduct = customDepositProducts != null && !customDepositProducts.isEmpty();
        fixedDepositProducts = dpBean.getAllPresentFixedDepositProducts();
        hasTimeDepositProduct = fixedDepositProducts != null && !fixedDepositProducts.isEmpty();
    }
    
    

    /**
     * @return the hasCurrentDepositProduct
     */
    public Boolean getHasCurrentDepositProduct() {
        return hasCurrentDepositProduct;
    }

    /**
     * @param hasCurrentDepositProduct the hasCurrentDepositProduct to set
     */
    public void setHasCurrentDepositProduct(Boolean hasCurrentDepositProduct) {
        this.hasCurrentDepositProduct = hasCurrentDepositProduct;
    }

    /**
     * @return the hasSavingsDepositProduct
     */
    public Boolean getHasSavingsDepositProduct() {
        return hasSavingsDepositProduct;
    }

    /**
     * @param hasSavingsDepositProduct the hasSavingsDepositProduct to set
     */
    public void setHasSavingsDepositProduct(Boolean hasSavingsDepositProduct) {
        this.hasSavingsDepositProduct = hasSavingsDepositProduct;
    }

    /**
     * @return the hasTimeDepositProduct
     */
    public Boolean getHasTimeDepositProduct() {
        return hasTimeDepositProduct;
    }

    /**
     * @param hasTimeDepositProduct the hasTimeDepositProduct to set
     */
    public void setHasTimeDepositProduct(Boolean hasTimeDepositProduct) {
        this.hasTimeDepositProduct = hasTimeDepositProduct;
    }

    /**
     * @return the hasCustomDepositProduct
     */
    public Boolean getHasCustomDepositProduct() {
        return hasCustomDepositProduct;
    }

    /**
     * @param hasCustomDepositProduct the hasCustomDepositProduct to set
     */
    public void setHasCustomDepositProduct(Boolean hasCustomDepositProduct) {
        this.hasCustomDepositProduct = hasCustomDepositProduct;
    }

    /**
     * @return the currentDepositProducts
     */
    public List<DepositAccountProduct> getCurrentDepositProducts() {
        return currentDepositProducts;
    }

    /**
     * @param currentDepositProducts the currentDepositProducts to set
     */
    public void setCurrentDepositProducts(List<DepositAccountProduct> currentDepositProducts) {
        this.currentDepositProducts = currentDepositProducts;
    }

    /**
     * @return the savingsDepositProducts
     */
    public List<DepositAccountProduct> getSavingsDepositProducts() {
        return savingsDepositProducts;
    }

    /**
     * @param savingsDepositProducts the savingsDepositProducts to set
     */
    public void setSavingsDepositProducts(List<DepositAccountProduct> savingsDepositProducts) {
        this.savingsDepositProducts = savingsDepositProducts;
    }

    /**
     * @return the customDepositProducts
     */
    public List<DepositAccountProduct> getCustomDepositProducts() {
        return customDepositProducts;
    }

    /**
     * @param customDepositProducts the customDepositProducts to set
     */
    public void setCustomDepositProducts(List<DepositAccountProduct> customDepositProducts) {
        this.customDepositProducts = customDepositProducts;
    }

    /**
     * @return the fixedDepositProducts
     */
    public List<FixedDepositAccountProduct> getFixedDepositProducts() {
        return fixedDepositProducts;
    }

    /**
     * @param fixedDepositProducts the fixedDepositProducts to set
     */
    public void setFixedDepositProducts(List<FixedDepositAccountProduct> fixedDepositProducts) {
        this.fixedDepositProducts = fixedDepositProducts;
    }
}
