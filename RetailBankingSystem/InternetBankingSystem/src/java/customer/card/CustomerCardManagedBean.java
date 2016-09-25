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
import javax.transaction.Transaction;
import static org.codehaus.groovy.runtime.DateGroovyMethods.updated;
import server.utilities.EnumUtils.CardAccountStatus;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "customerCardManagedBean")
@ViewScoped
public class CustomerCardManagedBean implements Serializable {

    private Customer customer;
    private List<CreditCardAccount> ccas;
    private List<CardTransaction> cardTransactions;
    private static CreditCardAccount cca;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    public void sendCCTransactionDetail(CreditCardAccount aCca) {
        Map<String, String> map = new HashMap<>();
        map.put("ccaId", aCca.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("credit_card_transactions.xhtml" + params);
    }

    public void viewTerminatePage(CreditCardAccount aCca) {
        System.out.println("in viewTerminatePage");
        cardAcctSessionBean.updateCardAccountStatus(cca, CardAccountStatus.CLOSED);
        RedirectUtils.redirect("/InternetBankingSystem/customer_card/debit_card_summary.xhtml");
    }

    public void viewTerminateDebitPage(DebitCardAccount dca) {
        System.out.println("in viewTerminatePage");
        cardAcctSessionBean.updateDebitAccountStatus(dca, CardAccountStatus.CLOSED);
        RedirectUtils.redirect("/InternetBankingSystem/customer_card/debit_card_summary.xhtml");
    }
    
    public void viewRedeemPage() {
        RedirectUtils.redirect("/InternetBankingSystem/customer_card/card_promotion.xhtml");
    }

    public void redirectToChangeTransactionLimitPage(CreditCardAccount aCca) {
        cca = aCca;
        RedirectUtils.redirect("/InternetBankingSystem/customer_card/change_transaction_limit.xhtml");
    }

    public void updateTransactionLimit() {
        String result = cardAcctSessionBean.updateCardAcctTransactionLimit(cca);
        if (result.equals("SUCCESS")) {
            MessageUtils.displayInfo("Transaction limit is updated!");
        } else {
            MessageUtils.displayError("Change is unsuccessful!");

        }
    }

    public CustomerCardManagedBean() {
    }

    @PostConstruct
    public void setCustomer() {
        System.out.println("@POSTCONSTRUCT INIT CustomerCardManagedBean");
        this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        this.setCcas(cardAcctSessionBean.showAllCreditCardAccount(CardAccountStatus.CLOSED, customer.getMainAccount().getId())); //that is not closed
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

    public List<CreditCardAccount> getCcas() {
        return ccas;
    }

    public void setCcas(List<CreditCardAccount> ccas) {
        this.ccas = ccas;
    }

    /**
     * @return the cca
     */
    public CreditCardAccount getCca() {
        return cca;
    }

    /**
     * @param cca the cca to set
     */
    public void setCca(CreditCardAccount cca) {
        this.cca = cca;
    }
}
