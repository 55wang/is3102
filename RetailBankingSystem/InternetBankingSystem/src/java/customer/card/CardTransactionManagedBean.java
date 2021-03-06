/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBean;
import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.cms.CustomerNotExistException;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "cardTransactionManagedBean")
@ViewScoped
public class CardTransactionManagedBean implements Serializable {

    private Customer customer;
    private List<CardTransaction> cardTransactions;
    private String ccaId;
    private String dcaId;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    private CardTransactionSessionBeanLocal cardTransactionSessionBean;
    
    public void viewCCTransactionDetail() {
        System.out.println("ccaId: " +ccaId);
        this.cardTransactions = cardTransactionSessionBean.getListCardTransactionsByCcaId(Long.parseLong(ccaId));
    }
    
    public void viewDebitCardTransactionDetail() {
        System.out.println("dcaId: " +dcaId);
        this.cardTransactions = cardTransactionSessionBean.getListCardTransactionsByCcaId(Long.parseLong(dcaId));
    }

    public CardTransactionManagedBean() {
    }

    @PostConstruct
    public void setCustomer() {
        System.out.println("@POSTCONSTRUCT INIT CustomerCardManagedBean");
        try {
            this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        } catch (CustomerNotExistException e) {
            System.out.println("CustomerNotExistException @PostConstruct setCustomer()");
        }
        
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CardTransaction> getCardTransactions() {
        return cardTransactions;
    }

    public void setCardTransactions(List<CardTransaction> cardTransactions) {
        this.cardTransactions = cardTransactions;
    }

    public String getCcaId() {
        return ccaId;
    }

    public void setCcaId(String ccaId) {
        this.ccaId = ccaId;
    }

    /**
     * @return the dcaId
     */
    public String getDcaId() {
        return dcaId;
    }

    /**
     * @param dcaId the dcaId to set
     */
    public void setDcaId(String dcaId) {
        this.dcaId = dcaId;
    }
}
