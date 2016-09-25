/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.CardAccountStatus;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "customerDebitManagedBean")
@ViewScoped
public class CustomerDebitManagedBean implements Serializable {

    private Customer customer;
    private List<DebitCardAccount> dcas;
    private List<CardTransaction> cardTransactions;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    public void sendCCTransactionDetail(DebitCardAccount dca) {
        Map<String, String> map = new HashMap<>();
        map.put("dcaId", dca.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("debit_card_transactions.xhtml" + params);
    }

    public void viewTerminatePage(CreditCardAccount cca) {
        System.out.println("in viewTerminatePage");
        cardAcctSessionBean.updateCardAccountStatus(cca, CardAccountStatus.CLOSED);
        RedirectUtils.redirect("/InternetBankingSystem/customer_card/application_success.xhtml");
    }

    public CustomerDebitManagedBean() {
    }

    @PostConstruct
    public void setCustomer() {
        System.out.println("@POSTCONSTRUCT INIT CustomerCardManagedBean");
        this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        this.setDcas(cardAcctSessionBean.showAllDebitCardAccount(CardAccountStatus.CLOSED, customer.getMainAccount().getId())); //that is not closed
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

    public List<DebitCardAccount> getDcas() {
        return dcas;
    }

    public void setDcas(List<DebitCardAccount> dcas) {
        this.dcas = dcas;
    }
}