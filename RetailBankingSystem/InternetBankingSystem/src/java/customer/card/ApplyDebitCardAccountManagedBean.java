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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
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
    
    private List<CustomerDepositAccount> depositAccounts = new ArrayList<>();
    private List<String> accountOptions = new ArrayList<>();
    private String selectedAccountNumber;
    /**
     * Creates a new instance of ApplyDebitCardAccountManagedBean
     */
    public ApplyDebitCardAccountManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        depositAccounts = depositBean.getAllNonFixedCustomerAccounts(Long.parseLong(SessionUtils.getUserId()));
        for (CustomerDepositAccount a : depositAccounts) {
            accountOptions.add(a.getAccountNumber());
        }
    }
    
    public void applyDebitCard() {
        System.out.println("card account session bean called");
        CustomerDepositAccount selectedAccount = getSelectedAccount();
        if (selectedAccount != null) {
            // TODO: create debit card and link with account
//            dca.setCustomerDepositAccount(selectedAccount);
//            dca.setCardStatus(EnumUtils.CardAccountStatus.PENDING);
//            dca.setCreationDate(new Date());
//            dca.setNameOnCard(dca, selectedAccount);
//            cardAcctSessionBean.createDebitAccount(dca, );
            System.out.println("inside the loop");
            cardAcctSessionBean.createDebitAccount(selectedAccount);
            MessageUtils.displayInfo("Your application is successful!");
            RedirectUtils.redirect("/InternetBankingSystem/personal_cards/debit_card_summary.xhtml");
            
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
    
}
