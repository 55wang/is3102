/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import java.io.Serializable;
import java.util.Calendar;
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

    private List<CreditCardAccount> ccas;

    public CardViewCreditCardAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();
    }

    public void demoPaidMPD() {
        //set MPD = 0 and deduct outstanding
        //assume paid amount is $60
        Double paidAmount = 60.0;

        //need to change, retrieve cca by accountId and deduct instead of all accounts
        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();
        for (CreditCardAccount cca : ccas) {

            if (paidAmount >= cca.getMinPayDue()) {
                cca.setMinPayDue(0.0);
                cca.setOutstandingAmount(cca.getOutstandingAmount() - paidAmount);
            } else if (paidAmount < cca.getMinPayDue()) {
                cca.setMinPayDue(cca.getMinPayDue() - paidAmount);
                cca.setOutstandingAmount(cca.getOutstandingAmount() - paidAmount);
            }
            cardAcctSessionBean.updateCreditCardAccount(cca);
        }
    }

    public void demoPaidOutstandingAmount() {
        //set both MPD and outstanding = 0

        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();
        for (CreditCardAccount cca : ccas) {
            cca.setMinPayDue(0.0);
            cca.setOutstandingAmount(0.0);
            cardAcctSessionBean.updateCreditCardAccount(cca);
        }
    }

    public void demoCutOffTimer() {
        //shift current month amount to outstanding amount
        //retrieve again to prevent inaccuracy.
        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();

        for (CreditCardAccount cca : ccas) {
            cardAcctSessionBean.addCurrentMonthAmountToOutstandingAmount(cca);
            cca.setMinPayDue(cca.calculateMinPayDue());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 15);
            cardAcctSessionBean.setOverDueDate(cca, cal.getTime());
        }
    }

    public void demoSendEStatement() {
        //send e-statement
        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();

        for (CreditCardAccount cca : ccas) {
            System.out.println("outstanding amount: " + cca.getOutstandingAmount());
            System.out.println("min pay due: " + cca.getMinPayDue());
            System.out.println("interest amount: " + cca.getInterestAmount());
            System.out.println("overdue date: " + cca.getOverDueDate());
        }
    }

    public void calculateDueCreditCardAccount() {

        ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();

        for (CreditCardAccount cca : ccas) {
            //if fail to pay MPD
            if (cca.getMinPayDue() > 0) {
                if (cca.getOutstandingAmount() > 0) {
                    //it is overdue
                    cca.setLatePaymentFee(60.0);
                    System.out.println(cca.getCreditCardNum() + ": Total Outstanding Amount: " + cca.getOutstandingAmount() + " + " + cca.getInterestAmount() + " + " + cca.getLatePaymentFee());
                    cardAcctSessionBean.addInterestToOutStandingAmount(cca);
                    cardAcctSessionBean.addLatePaymentToOutstandingAmount(cca);
                    cca.setLatePaymentFee(0.0);

                    //add bad credit record
                    cca.setNumOfLatePayment(cca.getNumOfLatePayment() + 1);

                    if (cca.getOverDueDays() >= 30 && cca.getOverDueDays() <= 59) {
                        cca.setNumOf_30_59_LatePayment(cca.getNumOf_30_59_LatePayment() + 1);
                    } else if (cca.getOverDueDays() >= 60 && cca.getOverDueDays() <= 89) {
                        cca.setNumOf_60_89_LatePayment(cca.getNumOf_60_89_LatePayment() + 1);
                    } else if (cca.getOverDueDays() >= 90) {
                        cca.setNumOf_90_LatePayment(cca.getNumOf_90_LatePayment() + 1);
                    }
                    cca.setMinPayDue(0.0);
                    cardAcctSessionBean.updateCreditCardAccount(cca);

                }
            } else if (cca.getMinPayDue() == 0) {
                //paid MPD only, still got outstandingamount;
                if (cca.getOutstandingAmount() > 0) {
                    System.out.println(cca.getCreditCardNum() + ": Total Outstanding Amount: " + cca.getOutstandingAmount() + " + " + cca.getInterestAmount());
                    cardAcctSessionBean.addInterestToOutStandingAmount(cca);
                    //clear overdue dates
                    cca.setOverDueDate(null);
                    cca.setMinPayDue(0.0);
                    cardAcctSessionBean.updateCreditCardAccount(cca);
                }
            } else if (cca.getMinPayDue() == 0 && cca.getOutstandingAmount() == 0) {
                //No outstanding amount
                //clear overdue dates
                cca.setOverDueDate(null);
                cca.setMinPayDue(0.0);
                cardAcctSessionBean.updateCreditCardAccount(cca);

                System.out.println(cca.getCreditCardNum() + ": Total Outstanding Amount: " + cca.getOutstandingAmount() + " + " + cca.getInterestAmount());
            }
        }
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

}
