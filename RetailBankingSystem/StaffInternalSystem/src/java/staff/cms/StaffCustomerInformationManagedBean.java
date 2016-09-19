/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "staffCustomerInformationManagedBean")
@Dependent
public class StaffCustomerInformationManagedBean {
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    private List<Customer> customers;
    /**
     * Creates a new instance of StaffCustomerInformationManagedBean
     */
    public StaffCustomerInformationManagedBean() {
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


    
}
