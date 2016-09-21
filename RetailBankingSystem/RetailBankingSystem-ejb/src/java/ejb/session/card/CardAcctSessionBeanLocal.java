/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardOrder;
import java.util.List;
import javax.ejb.Local;
import utils.EnumUtils;

/**
 *
 * @author wang
 */
@Local
public interface CardAcctSessionBeanLocal {

    public List<CreditCardOrder> showAllCreditCardOrder();

    public CreditCardOrder getCardOrderFromId(Long orderNumber);

    public String createCardOrder(CreditCardOrder order);

    public String updateCardOrderStatus(CreditCardOrder order, EnumUtils.ApplicationStatus status);

    public List<CreditCardAccount> showAllCreditCardAccount();

    public CreditCardAccount getCardAccountFromId(Long cardID);

    public String createCardAccount(CreditCardAccount cca);

    public String updateCardAccountStatus(CreditCardAccount cca, EnumUtils.CardAccountStatus status);

    public String updateCardAcctTransactionDailyLimit(CreditCardAccount cca, double newDailyLimit);
    
    public String updateCardAcctTransactionMonthlyLimit(CreditCardAccount cca, double newMonthlyLimit);
    
}
