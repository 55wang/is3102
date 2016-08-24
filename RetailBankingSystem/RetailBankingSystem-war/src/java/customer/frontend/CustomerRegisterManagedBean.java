/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.frontend;

import entity.Customer;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerRegisterManagedBean")
@ViewScoped
public class CustomerRegisterManagedBean implements Serializable {
    private Customer customer = new Customer();
     

    /**
     * Creates a new instance of CustomerRegisterManagedBean
     */
    public CustomerRegisterManagedBean() {
    }
     
    public Customer getCustomer() {
        return customer;
    }
 
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
     
    public void save() {        
        FacesMessage msg = new FacesMessage("Successful", "Welcome :" + customer.getFirstname());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
        
    public String onFlowProcess(FlowEvent event) {
            return event.getNewStep();
    }
    
}
