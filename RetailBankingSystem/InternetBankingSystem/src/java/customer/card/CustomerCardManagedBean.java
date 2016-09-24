/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBean;
import entity.card.account.CreditCardAccount;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "customerCardManagedBean")
@ViewScoped
public class CustomerCardManagedBean implements Serializable{

    private Customer customer; 
    private List<CreditCardAccount> ccas;
    
    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private CustomerProfileSessionBean customerProfileSessionBean;

    public CustomerCardManagedBean() {
    }

    @PostConstruct
    public void setCustomer() {
        this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        this.ccas = customer.getMainAccount().getCreditCardAccounts();
    }
}
