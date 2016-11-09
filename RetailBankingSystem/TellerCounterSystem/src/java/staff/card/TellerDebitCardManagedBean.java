/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author wang
 */
@Named(value = "tellerDebitCardManagedBean")
@ViewScoped
public class TellerDebitCardManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;

    public TellerDebitCardManagedBean() {
    }

    private Customer customer;
    private List<DebitCardAccount> activeDcas;
    private List<DebitCardAccount> pendingDcas;
    private List<CardTransaction> cardTransactions;
    private String APPLICATION_STATUS_PENDING = EnumUtils.CardAccountStatus.PENDING.toString();
    private String customerIC;
    private MainAccount mainAccount;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;

    public void retrieveMainAccount() {
        try {
            mainAccount = loginBean.getMainAccountByUserIC(getCustomerIC());
            activeDcas = cardAcctSessionBean.getListDebitCardAccountsByMainAccountId(mainAccount.getId());

            System.out.println("success");
        } catch (Exception ex) {
            System.out.println(ex);
            mainAccount = null;
            MessageUtils.displayError("Customer Main Account Not Found!");
        }
    }

    public void sendCCTransactionDetail(DebitCardAccount dca) {
        Map<String, String> map = new HashMap<>();
        map.put("dcaId", dca.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("debit_card_transactions.xhtml" + params);
    }

    public void viewTerminatePage(CreditCardAccount cca) {
        System.out.println("in viewTerminatePage");
        cardAcctSessionBean.updateCardAccountStatus(cca, EnumUtils.CardAccountStatus.CLOSED);
    }

    public void terminateDebitCard(DebitCardAccount dca) {
        System.out.println("in viewTerminatePage");
        cardAcctSessionBean.updateDebitAccountStatus(dca, EnumUtils.CardAccountStatus.CLOSED);
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

    public String getAPPLICATION_STATUS_PENDING() {
        return APPLICATION_STATUS_PENDING;
    }

    public void setAPPLICATION_STATUS_PENDING(String APPLICATION_STATUS_PENDING) {
        this.APPLICATION_STATUS_PENDING = APPLICATION_STATUS_PENDING;
    }

    /**
     * @return the activeDcas
     */
    public List<DebitCardAccount> getActiveDcas() {
        return activeDcas;
    }

    /**
     * @param activeDcas the activeDcas to set
     */
    public void setActiveDcas(List<DebitCardAccount> activeDcas) {
        this.activeDcas = activeDcas;
    }

    /**
     * @return the pendingDcas
     */
    public List<DebitCardAccount> getPendingDcas() {
        return pendingDcas;
    }

    /**
     * @param pendingDcas the pendingDcas to set
     */
    public void setPendingDcas(List<DebitCardAccount> pendingDcas) {
        this.pendingDcas = pendingDcas;
    }

    public String getCustomerIC() {
        return customerIC;
    }

    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

}
