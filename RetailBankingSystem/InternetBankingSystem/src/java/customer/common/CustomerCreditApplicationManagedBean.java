/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.card.account.CreditCardAccount;
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
    private CardAcctSessionBeanLocal cardAcctSessionBean;


    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public CustomerCreditApplicationManagedBean() {
    }
    
    public String applyCreditCard() {
        
        return null;
    }
    
    public String activateCreditCard() {
        
        return null;
    }
    
    //after receive email, call this function.
    public String cancelCardApplication() {
    
        return null;
    }
    
    //for existing customer, not implementing.
    public String viewApplicationStatus() {
        
        return null;
    }
    
    public String closedCreditCard() {
        
        return null;
    }
    
    /*
    retrieve card info and card transaction
    debit card number
    link to deposit account
    Available Spending limit
    total amount pending settlement
    transaction date, description, amount
    date, code, desc, debit, credit
    */
    public String viewCreditCardSummary(Long cardID) {
        CreditCardAccount cca = cardAcctSessionBean.getCardAccountFromId(cardID);
        //continue from here
        
        
        return null;
    }
    
    
    

}
