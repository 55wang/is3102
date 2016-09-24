/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.deposit.display;

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
@Named(value = "displayCurrentDepositProductManagedBean")
@ViewScoped
public class DisplayCurrentDepositProductManagedBean implements Serializable {

    @EJB
    private DepositProductSessionBeanLocal dpBean;
    
    private List<DepositAccountProduct> currentDepositProducts;
    
    /**
     * Creates a new instance of DisplayCurrentDepositProductManagedBean
     */
    public DisplayCurrentDepositProductManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        setCurrentDepositProducts(dpBean.getAllPresentCurrentDepositProducts());
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
}
