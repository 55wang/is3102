/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.card.NewCardSessionBean;
import ejb.session.card.NewCardSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.dams.account.DepositAccount;
import entity.dams.account.CurrentAccount;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.SavingAccount;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
import utils.MessageUtils;
import utils.RedirectUtils;

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
    private NewCardSessionBeanLocal newCardSessionBean;


    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public CustomerCreditApplicationManagedBean() {
    }

}
