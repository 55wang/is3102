/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CreditCardOrderSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.card.account.CreditCardOrder;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "creditCardApplicationStatusManagedBean")
@ViewScoped
public class CreditCardApplicationStatusManagedBean implements Serializable {
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private CreditCardOrderSessionBeanLocal creditCardOrderSessionBean;
    
    private String searchText="";
    private List<CreditCardOrder> applications;
    private MainAccount ma;
    /**
     * Creates a new instance of CreditCardApplicationStaticsManagedBean
     */
    public CreditCardApplicationStatusManagedBean() {
    }
    
    public void searchApplication(){
        applications = new ArrayList<CreditCardOrder>();
        CreditCardOrder cco = creditCardOrderSessionBean.searchMainAccountCreditCardOrderByID(ma, searchText);
        if(cco == null){
            MessageUtils.displayInfo("Application record not found!");
        }else
            applications.add(creditCardOrderSessionBean.searchCreditCardOrderByID(searchText));
    }
    
    public void cancel(CreditCardOrder cco){
        Boolean result = creditCardOrderSessionBean.cancelCreditCardOrder(cco);
        if(result){
            applications.remove(cco);
            MessageUtils.displayInfo("Cancel successfully");
        }
        else
            MessageUtils.displayError("Error");
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
        this.ma = loginSessionBean.getMainAccountByUserID(SessionUtils.getUserName());
        this.applications = creditCardOrderSessionBean.getMainAccountAllCreditCardOrders(ma);      
    }
}
