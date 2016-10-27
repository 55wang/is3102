/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import ejb.session.wealth.WealthManegementSubscriberSessionBeanLocal;
import entity.customer.WealthManagementSubscriber;
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
@Named(value = "customerPortfolioManagedBean")
@ViewScoped
public class CustomerPortfolioDetailManagedBean implements Serializable {
    @EJB
    private WealthManegementSubscriberSessionBeanLocal wealthManegementSubscriberSessionBean;
    
    private String searchText;
    private List<WealthManagementSubscriber> wmsLists = new ArrayList<WealthManagementSubscriber>();
    /**
     * Creates a new instance of CustomerPortfolioManagedBean
     */
    public CustomerPortfolioDetailManagedBean() {
    }
    
    @PostConstruct
    public void init() {
       wmsLists = wealthManegementSubscriberSessionBean.getAllWealthManagementSubscribers();
    }
    
    public void searchCustomer() {
        try{
            wmsLists = new ArrayList<WealthManagementSubscriber>();
            System.out.println("search: "+searchText);
            WealthManagementSubscriber wms = wealthManegementSubscriberSessionBean.getWealthManagementSubscriberById(Long.parseLong(searchText));
             System.out.println("wms: "+wms.getId());
            if(wms != null)
                wmsLists.add(wms);
        }catch(Exception ex){
            MessageUtils.displayError("Input Error");
        }
    }
    
    public void searchAllCustomer(){
        wmsLists = wealthManegementSubscriberSessionBean.getAllWealthManagementSubscribers();
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<WealthManagementSubscriber> getWmsLists() {
        return wmsLists;
    }

    public void setWmsLists(List<WealthManagementSubscriber> wmsLists) {
        this.wmsLists = wmsLists;
    }
}
