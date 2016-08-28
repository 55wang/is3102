/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.LoginSessionBeanLocal;
import entity.Customer;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerHomeManagedBean")
@ViewScoped
public class CustomerHomeManagedBean implements Serializable{
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    private Customer customer = new Customer();
    
    
    /**
     * Creates a new instance of CustomerHomeManagedBean
     */
    public CustomerHomeManagedBean() {
    }

    public Customer getCustomer() {
        return customer;
    }

    @PostConstruct
    public void setCustomer() {
        this.customer = loginSessionBean.getCustomerByUserID(SessionUtils.getUserName());;
    }
    
    
    
}
