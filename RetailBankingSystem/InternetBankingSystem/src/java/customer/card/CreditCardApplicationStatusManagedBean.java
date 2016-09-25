/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CreditCardOrderSessionBeanLocal;
import entity.card.account.CreditCardOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "creditCardApplicationStatusManagedBean")
@ViewScoped
public class CreditCardApplicationStatusManagedBean implements Serializable {
    @EJB
    private CreditCardOrderSessionBeanLocal creditCardOrderSessionBean;
    
    private String searchText="";
    private List<CreditCardOrder> applications;
    /**
     * Creates a new instance of CreditCardApplicationStaticsManagedBean
     */
    public CreditCardApplicationStatusManagedBean() {
    }
    
    public void searchApplication(){
        applications = new ArrayList<CreditCardOrder>();
        CreditCardOrder cco = creditCardOrderSessionBean.searchCreditCardOrderByID(searchText);
        if(cco == null){
            MessageUtils.displayInfo("Application record not found!");
        }else
            applications.add(creditCardOrderSessionBean.searchCreditCardOrderByID(searchText));
    }
    
    public void showAllApplication(){
        this.applications = creditCardOrderSessionBean.getAllCreditCardOrders();
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<CreditCardOrder> getApplications() {
        return applications;
    }

    @PostConstruct
    public void setApplications() {
        this.applications = creditCardOrderSessionBean.getAllCreditCardOrders();
    }
}
