/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import entity.dams.account.CustomerDepositAccount;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Local
public interface CardAcctSessionBeanLocal {

    /*
    CreditCard
    create
    update 
    get
    get list
    */
    public Date setOverDueDate(CreditCardAccount cca, Date overDueDate);
    public CreditCardAccount createCardAccount(CreditCardAccount cca);
    public CreditCardAccount updateCreditCardAccount(CreditCardAccount cca);
    public List<CreditCardAccount> updateListDemoEndOfMonthCcas();
    public CreditCardAccount updateCardAccountStatus(CreditCardAccount cca, EnumUtils.CardAccountStatus status);
    public CreditCardAccount updateCardAcctTransactionDailyLimit(CreditCardAccount cca, double newDailyLimit);
    public CreditCardAccount activateCreditCard(String identityNumber, Date birthday, String cardNumber, String cvv);
    public Double addInterestToOutStandingAmount(CreditCardAccount cca);
    public Double addLatePaymentToOutstandingAmount(CreditCardAccount cca);
    public Double addCurrentMonthAmountToOutstandingAmount(CreditCardAccount cca);
    public CreditCardAccount getCardAccountById(Long cardID);
    public CreditCardAccount getCreditCardAccountByCardNumber(String cardNumber);
    public List<CreditCardAccount> getListCreditCardAccountsByCardStatusAndAppStatus(EnumUtils.CardAccountStatus cardAccountStatus, EnumUtils.ApplicationStatus cardApplicationStatus);
    public List<CreditCardAccount> getListCreditCardAccountsByIdAndNotStatus(Long id, EnumUtils.CardAccountStatus status);
    public List<CreditCardAccount> updateListInterestToOutstandingAmountCcas();
    public List<CreditCardAccount> updateListLatePaymentToOutstandingAmountCcas();
    public List<CreditCardAccount> getListCreditCardAccountsByActiveOrFreezeCardStatus();
    /*
    DebitCard
    create
    update 
    get
    get list
    */
    public DebitCardAccount createDebitAccount(CustomerDepositAccount da);
    public DebitCardAccount updateDebitAccountStatus(DebitCardAccount dca, EnumUtils.CardAccountStatus status);
    public DebitCardAccount activateDebitCard(String identityNumber, Date birthday, String cardNumber, String cvv);
    public List<DebitCardAccount> getListDebitCardAccountsByIdAndNotStatus(Long id, EnumUtils.CardAccountStatus status);
    
}
