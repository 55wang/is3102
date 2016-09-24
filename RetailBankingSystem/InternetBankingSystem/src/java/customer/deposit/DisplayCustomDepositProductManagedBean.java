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
@Named(value = "displayCustomDepositProductManagedBean")
@ViewScoped
public class DisplayCustomDepositProductManagedBean implements Serializable {

    @EJB
    private DepositProductSessionBeanLocal dpBean;
    
    private List<DepositAccountProduct> customDepositProducts;
    /**
     * Creates a new instance of DisplayCustomDepositProductManagedBean
     */
    public DisplayCustomDepositProductManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        customDepositProducts = dpBean.getAllPresentCustomDepositProducts();
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
}
