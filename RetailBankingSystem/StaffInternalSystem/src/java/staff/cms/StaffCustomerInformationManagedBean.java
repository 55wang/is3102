/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "staffCustomerInformationManagedBean")
@ViewScoped
public class StaffCustomerInformationManagedBean implements Serializable {

    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    private List<Customer> customers;
    private String searchText;

    /**
     * Creates a new instance of StaffCustomerInformationManagedBean
     */
    public StaffCustomerInformationManagedBean() {
    }

    public void search() {
        customers = customerProfileSessionBean.searchCustomerByIdentityNumber(searchText);
        System.out.print("Managed Bean search ---------------" + customers.size());

    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers the customers to set
     */
    @PostConstruct
    public void setCustomers() {
        this.customers = customerProfileSessionBean.retrieveActivatedCustomers();

    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

}
