/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.crm.RFMSessionBeanLocal;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "customerRFMManagedBean")
@ViewScoped
public class CustomerRFMManagedBean implements Serializable {

    @EJB
    RFMSessionBeanLocal rFMSessionBean;
    @EJB
    CustomerProfileSessionBeanLocal customerProfileSessionBean;

    private List<Customer> customers;

    public CustomerRFMManagedBean() {
    }

    @PostConstruct
    public void init() {
        customers = customerProfileSessionBean.getListCustomers();
    }

    public void refreshCustomerRFM() {
        //to be continued
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

}
