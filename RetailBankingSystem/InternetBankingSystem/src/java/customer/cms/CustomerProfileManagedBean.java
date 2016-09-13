/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.cms;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.Customer;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "customerProfileManagedBean")
@ViewScoped
public class CustomerProfileManagedBean implements Serializable {
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;
    private Customer customer;    

    
    /**
     * Creates a new instance of CustomerInformationManagedBean
     */
    public CustomerProfileManagedBean() {
       
    }

    public Customer getCustomer() {
        return customer ;
    }
    
    @PostConstruct
    public void setCustomer() {
        this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
    }
   
       
    
}
