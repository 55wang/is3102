/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.deposit;

import ejb.session.dams.DepositProductSessionBeanLocal;
import entity.dams.account.DepositAccountProduct;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "displaySavingsDepositProductManagedBean")
@ViewScoped
public class DisplaySavingsDepositProductManagedBean {

    @EJB
    private DepositProductSessionBeanLocal dpBean;
    
    private List<DepositAccountProduct> currentDepositProducts;
    
    /**
     * Creates a new instance of DisplaySavingsDepositProductManagedBean
     */
    public DisplaySavingsDepositProductManagedBean() {
    }
    
}
