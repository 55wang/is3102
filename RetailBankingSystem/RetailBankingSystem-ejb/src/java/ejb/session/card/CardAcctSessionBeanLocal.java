/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardOrder;
import entity.card.account.DebitCardAccount;
import entity.customer.MainAccount;
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

    public List<CreditCardOrder> showAllCreditCardOrder();
    
    public List<CreditCardOrder> showAllCreditCardOrder(EnumUtils.ApplicationStatus status);

    public CreditCardOrder getCardOrderFromId(Long orderNumber);

    public String createCardOrder(CreditCardOrder order);

    public String updateCardOrderStatus(CreditCardOrder order, EnumUtils.ApplicationStatus status);

    public List<CreditCardAccount> showAllCreditCardAccount(EnumUtils.CardAccountStatus status, Long id);

    public CreditCardAccount getCardAccountFromId(Long cardID);

    public CreditCardAccount getCardByCardNumber(String cardNumber);

    public CreditCardAccount validateCreditCardDailyTransactionLimit(CreditCardAccount creditCard, double requestAmount);

    public CreditCardAccount validateCreditCardMonthlyTransactionLimit(CreditCardAccount creditCard, double requestAmount);

    public CreditCardAccount createCardAccount(CreditCardAccount cca);

    public CardTransaction createCardAccountTransaction(String ccNumber, CardTransaction ct);

    public String updateCardAccountStatus(CreditCardAccount cca, EnumUtils.CardAccountStatus status);

    public String updateCardAcctTransactionLimit(CreditCardAccount cca);

    public List<CardTransaction> getCardTransactionFromId(Long ccaId);

    public DebitCardAccount createDebitAccount(CustomerDepositAccount da);

    public List<DebitCardAccount> showAllDebitCardAccount(EnumUtils.CardAccountStatus status, Long id);

    public List<CardTransaction> getDailyTransactionFromAccount(CreditCardAccount creditCard);

    public List<CardTransaction> getMonthlyTransactionFromAccount(CreditCardAccount creditCard);

    public CardTransaction getSpecificCaedTransactionFromId(Long ccaId);

    public String updateCreditCardOrder(CreditCardOrder cco);

    public MainAccount activateCreditCard(String identityNumber, Date birthday, String cardNumber, String cvv);
    
    public MainAccount activateDebitCard(String identityNumber, Date birthday, String cardNumber, String cvv);

}
