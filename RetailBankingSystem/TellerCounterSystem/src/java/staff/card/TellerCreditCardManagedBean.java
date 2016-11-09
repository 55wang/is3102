/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.Date;
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
@Named(value = "tellerCreditCardManagedBean")
@ViewScoped
public class TellerCreditCardManagedBean implements Serializable {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private LoginSessionBeanLocal loginBean;

    private Customer customer;
    private List<CreditCardAccount> activeCcas;
    private List<CreditCardAccount> pendingCcas;

    private List<CardTransaction> cardTransactions;
    private static CreditCardAccount cca;
    private Date currentDate = new Date();
    private String customerIC;
    private MainAccount mainAccount;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;

    public void retrieveMainAccount() {
        try {
            mainAccount = loginBean.getMainAccountByUserIC(getCustomerIC());
            activeCcas = cardAcctSessionBean.getListCreditCardAccountsByMainId(mainAccount.getId());

            System.out.println("success");
        } catch (Exception ex) {
            System.out.println(ex);
            mainAccount = null;
            MessageUtils.displayError("Customer Main Account Not Found!");
        }
    }

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
    }

    public void sendDCTransactionDetail(DebitCardAccount aDca) {
        Map<String, String> map = new HashMap<>();
        map.put("dcaId", aDca.getId().toString());
    }

    //
    public void terminateCreditCard(CreditCardAccount aCca) {
        System.out.println("in viewTerminatePage");
        if (aCca.getOutstandingAmount() > 0 || aCca.getCurrentMonthAmount() > 0) {
            MessageUtils.displayError("Card cannot be terminiated due to outstanding balance or current amount.");
        } else {
            cardAcctSessionBean.updateCardAccountStatus(aCca, EnumUtils.CardAccountStatus.CLOSED);
        }
    }

    public void redirectToChangeTransactionLimitPage(CreditCardAccount aCca) {
        cca = aCca;
        RedirectUtils.redirect("/TellerCounterSystem/card/change_credit_transaction_limit.xhtml");
    }

    public void updateTransactionLimit() {
        CreditCardAccount result = cardAcctSessionBean.updateCreditCardAccount(cca);
        if (result != null) {
            MessageUtils.displayInfo("Transaction limit is updated!");
            emailServiceSessionBean.sendTransactionLimitChangeNotice(cca.getMainAccount().getCustomer().getEmail());
        } else {
            MessageUtils.displayError("Change is unsuccessful!");
        }
        RedirectUtils.redirect("/TellerCounterSystem/card/change_credit_transaction_limit.xhtml");
    }

    public TellerCreditCardManagedBean() {
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
