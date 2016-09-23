/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author VIN-S
 */
@Named(value = "CustomerCreditApplicationManagedBean")
@ViewScoped
public class CustomerCreditApplicationManagedBean implements Serializable {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private CardAcctSessionBeanLocal newCardSessionBean;


    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public CustomerCreditApplicationManagedBean() {
    }
    
    

}
