/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.CardAccountStatus;
import util.exception.cms.CustomerNotExistException;
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

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;

    private Customer customer;
    private List<CreditCardAccount> activeCcas;
    private List<CreditCardAccount> pendingCcas;

    private List<CardTransaction> cardTransactions;
    private static CreditCardAccount cca;
    private Date currentDate = new Date();

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    public Double displayOutstandingAmount(Double amount) {
        if (amount >= 0.0) {
            return amount;
        } else {
            return 0.0;
        }
    }

    public Double retrieveCreditLimit(CreditCardAccount cca) {
        if (cca.getOutstandingAmount() < 0.0) {
            return cca.getCreditLimit() - cca.getOutstandingAmount();
        } else {
            return cca.getCreditLimit();
        }
    }

    public void sendCCTransactionDetail(CreditCardAccount aCca) {
        Map<String, String> map = new HashMap<>();
        map.put("ccaId", aCca.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("credit_card_transactions.xhtml" + params);
    }

    public void sendDCTransactionDetail(DebitCardAccount aDca) {
        Map<String, String> map = new HashMap<>();
        map.put("dcaId", aDca.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("debit_card_transactions.xhtml" + params);
    }

    public void terminateCreditCard(CreditCardAccount aCca) {
        System.out.println("in viewTerminatePage");
        if (aCca.getOutstandingAmount() > 0 || aCca.getCurrentMonthAmount() >0) {
            MessageUtils.displayError("Card cannot be terminiated due to outstanding balance.");
        } else {
            cardAcctSessionBean.updateCardAccountStatus(aCca, CardAccountStatus.CLOSED);
            RedirectUtils.redirect("/InternetBankingSystem/personal_cards/credit_card_summary.xhtml");
        }

    }

    public void terminateDebitCard(DebitCardAccount dca) {
        System.out.println("in viewTerminatePage");
        cardAcctSessionBean.updateDebitAccountStatus(dca, CardAccountStatus.CLOSED);
        RedirectUtils.redirect("/InternetBankingSystem/personal_cards/debit_card_summary.xhtml");
    }

    public void viewRedeemPage(CreditCardAccount cca) {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("creditCardId", cca.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        if (cca.getCreditCardProduct().getCartType().equals(EnumUtils.CreditCardType.REWARD)) {
            RedirectUtils.redirect("redeem_rewards.xhtml" + params);
        } else {
            RedirectUtils.redirect("redeem_miles.xhtml" + params);
        }
    }

    public void redirectToChangeTransactionLimitPage(CreditCardAccount aCca) {
        cca = aCca;
        RedirectUtils.redirect("/InternetBankingSystem/personal_cards/change_transaction_limit.xhtml");
    }

    public void updateTransactionLimit() {
        CreditCardAccount result = cardAcctSessionBean.updateCreditCardAccount(cca);
        if (result != null) {
            MessageUtils.displayInfo("Transaction limit is updated!");
            emailServiceSessionBean.sendTransactionLimitChangeNotice(cca.getMainAccount().getCustomer().getEmail());
        } else {
            MessageUtils.displayError("Change is unsuccessful!");

        }
    }

    public CustomerCardManagedBean() {
    }

    @PostConstruct
    public void setCustomer() {
        System.out.println("@POSTCONSTRUCT INIT CustomerCardManagedBean");

        try {
            this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        } catch (CustomerNotExistException e) {
            System.out.println("CustomerNotExistException @PostConstruct setCustomer()");
        }
        this.setActiveCcas(cardAcctSessionBean.getAllActiveCreditCardAccountsByMainId(customer.getMainAccount().getId()));
        this.setPendingCcas(cardAcctSessionBean.getListCreditCardAccountsInProcess());
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

    /**
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return the activeCcas
     */
    public List<CreditCardAccount> getActiveCcas() {
        return activeCcas;
    }

    /**
     * @param activeCcas the activeCcas to set
     */
    public void setActiveCcas(List<CreditCardAccount> activeCcas) {
        this.activeCcas = activeCcas;
    }

    /**
     * @return the pendingCcas
     */
    public List<CreditCardAccount> getPendingCcas() {
        return pendingCcas;
    }

    /**
     * @param pendingCcas the pendingCcas to set
     */
    public void setPendingCcas(List<CreditCardAccount> pendingCcas) {
        this.pendingCcas = pendingCcas;
    }
}
