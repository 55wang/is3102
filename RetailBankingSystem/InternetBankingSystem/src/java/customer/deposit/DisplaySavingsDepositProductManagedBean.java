/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.deposit;

import ejb.session.dams.DepositProductSessionBeanLocal;
import entity.dams.account.DepositAccountProduct;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "displaySavingsDepositProductManagedBean")
@ViewScoped
public class DisplaySavingsDepositProductManagedBean implements Serializable {

    @EJB
    private DepositProductSessionBeanLocal dpBean;
    
    private List<DepositAccountProduct> savingsDepositProducts;
    
    /**
     * Creates a new instance of DisplaySavingsDepositProductManagedBean
     */
    public DisplaySavingsDepositProductManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct =======================>>>>>>>>>");
        setSavingsDepositProducts(dpBean.getAllPresentSavingsDepositProducts());
        System.out.println("displaySavingsDepositProductManagedBean: " + savingsDepositProducts.size());
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
}
