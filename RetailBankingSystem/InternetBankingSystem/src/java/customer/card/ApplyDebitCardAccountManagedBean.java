/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.card.account.DebitCardAccount;
import entity.dams.account.CustomerDepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.CommonUtils;
import server.utilities.EnumUtils;
import utils.JSUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "applyDebitCardAccountManagedBean")
@ViewScoped
public class ApplyDebitCardAccountManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    private String selectedCardNetwork;
    
    private List<CustomerDepositAccount> depositAccounts = new ArrayList<>();
    private List<String> accountOptions = new ArrayList<>();
    private List<String> cardNetworkOptions = CommonUtils.getEnumList(EnumUtils.CardNetwork.class);
    private String selectedAccountNumber;
    private Boolean agreedTerm = false;

    /**
     * Creates a new instance of ApplyDebitCardAccountManagedBean
     */
    public ApplyDebitCardAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        depositAccounts = depositBean.getAllNonFixedCustomerAccounts(SessionUtils.getUserId());
        for (CustomerDepositAccount a : depositAccounts) {
            accountOptions.add(a.getAccountNumber());
        }
    }

    public void applyDebitCard() {
        if (!agreedTerm) {
            MessageUtils.displayError("Your must agree to the terms first!");
            return;
        }
 
        System.out.println("card account session bean called");
        CustomerDepositAccount selectedAccount = getSelectedAccount();
        if (selectedAccount != null) {
            DebitCardAccount result = cardAcctSessionBean.createDebitAccount(selectedAccount);
            
            if (result != null) {
                result.setCardNetwork(EnumUtils.CardNetwork.getEnum(selectedCardNetwork));
                List<DebitCardAccount> dcas = selectedAccount.getDebitCardAccount();
                dcas.add(result);
                selectedAccount.setDebitCardAccount(dcas);
                depositBean.updateCustomerDepositAccount(selectedAccount);
                cardAcctSessionBean.updateDebitAccount(result);
                MessageUtils.displayInfo("Your application is successful!");
                RedirectUtils.redirect("/InternetBankingSystem/personal_cards/debit_card_summary.xhtml");
            }
        }
        System.out.println("ended");
    }

    public CustomerDepositAccount getSelectedAccount() {
        for (CustomerDepositAccount a : depositAccounts) {
            if (a.getAccountNumber().equals(selectedAccountNumber)) {
                return a;
            }
        }
        return null;
    }

    /**
     * @return the accountOptions
     */
    public List<String> getAccountOptions() {
        return accountOptions;
    }

    /**
     * @param accountOptions the accountOptions to set
     */
    public void setAccountOptions(List<String> accountOptions) {
        this.accountOptions = accountOptions;
    }

    /**
     * @return the selectedAccountNumber
     */
    public String getSelectedAccountNumber() {
        return selectedAccountNumber;
    }

    /**
     * @param selectedAccountNumber the selectedAccountNumber to set
     */
    public void setSelectedAccountNumber(String selectedAccountNumber) {
        this.selectedAccountNumber = selectedAccountNumber;
    }

    /**
     * @return the cardNetworkOptions
     */
    public List<String> getCardNetworkOptions() {
        return cardNetworkOptions;
    }

    /**
     * @param cardNetworkOptions the cardNetworkOptions to set
     */
    public void setCardNetworkOptions(List<String> cardNetworkOptions) {
        this.cardNetworkOptions = cardNetworkOptions;
    }

    /**
     * @return the selectedCardNetwork
     */
    public String getSelectedCardNetwork() {
        return selectedCardNetwork;
    }

    /**
     * @param selectedCardNetwork the selectedCardNetwork to set
     */
    public void setSelectedCardNetwork(String selectedCardNetwork) {
        this.selectedCardNetwork = selectedCardNetwork;
    }

    /**
     * @return the agreedTerm
     */
    public Boolean getAgreedTerm() {
        return agreedTerm;
    }

    /**
     * @param agreedTerm the agreedTerm to set
     */
    public void setAgreedTerm(Boolean agreedTerm) {
        this.agreedTerm = agreedTerm;
    }

}
