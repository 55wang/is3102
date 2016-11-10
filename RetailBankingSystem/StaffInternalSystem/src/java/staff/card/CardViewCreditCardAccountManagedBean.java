/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "cardViewCreditCardAccountManagedBean")
@ViewScoped
public class CardViewCreditCardAccountManagedBean implements Serializable {

    @EJB
    CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    EmailServiceSessionBeanLocal emailServiceSessionBean;
    
    private List<CreditCardAccount> ccas;

    public CardViewCreditCardAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();
    }

    public void demoPaidMPD() {
        ccas = cardAcctSessionBean.updateListDemoPaidMPD();
    }

    public void demoPaidOutstandingAmount() {
        //set both MPD and outstanding = 0
        ccas = cardAcctSessionBean.updateListDemoPaidOutstandingAmountCcas();
    }

    public void demoCutOffTimer() {
        //shift current month amount to outstanding amount
        //retrieve again to prevent inaccuracy.
        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();

        for (CreditCardAccount cca : ccas) {
            cardAcctSessionBean.addCurrentMonthAmountToOutstandingAmount(cca);
            cardAcctSessionBean.setOverDueDateAndMPD(cca);
        }
        demoSendEStatement();
    }

    public void demoSendEStatement() {
        //send e-statement
        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();

        for (CreditCardAccount cca : ccas) {
            demoPrintEStatement(cca);
        }
    }

    public void calculateDueCreditCardAccount() {
        ccas = cardAcctSessionBean.calculateDueCreditCardAccount();
    }

    public void demoDueDayTimer() {
        calculateDueCreditCardAccount();
    }

    public List<CreditCardAccount> getCcas() {
        return ccas;
    }

    public void setCcas(List<CreditCardAccount> ccas) {
        this.ccas = ccas;
    }

    private void demoPrintEStatement(CreditCardAccount cca) {
        System.out.println("outstanding amount: " + cca.getOutstandingAmount());
        System.out.println("min pay due: " + cca.getMinPayDue());
        System.out.println("interest amount: " + cca.getInterestAmount());
        System.out.println("overdue date: " + cca.getOverDueDate());
        
        String subject = "eStatement ready for online viewing";
        String content = "https://localhost:8181/InternetBankingSystem/personal_request/cc_estatement_55444980599967262016_10_10.pdf";
        emailServiceSessionBean.sendEmailCreditCardEStatement(cca.getMainAccount().getCustomer().getEmail(), subject, content);
        
    }

}
