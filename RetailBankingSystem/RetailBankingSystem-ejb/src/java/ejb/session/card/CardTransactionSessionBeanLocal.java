/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils;

/**
 *
 * @author VIN-S
 */
@Local
public interface CardTransactionSessionBeanLocal {
     public Boolean createCardTransaction(CardTransaction ct);
     public List<CardTransaction> getTransactionByStartDateAndEndDate(Date start, Date end);
     public List<CardTransaction> getListCardTransactionsByCcaId(Long ccaId);
     public List<CardTransaction> getListCardTransactionsByStatus(EnumUtils.CardTransactionStatus status);
     public CardTransaction getCardTransactionByCcaId(Long ccaId);
     public CardTransaction getCardTransactionByVisaId(String visaId);
     public CardTransaction getLatestCardTransactionByCcaId(Long ccaId);
     public CardTransaction createCardAccountTransaction(CreditCardAccount cca, CardTransaction ct);
     public List<CardTransaction> getListDailyTransactionsByCreditCardAccount(CreditCardAccount cca);
     public List<CardTransaction> getListMonthlyTransactionsByCreditCardAccount(CreditCardAccount cca);
     public boolean validateCreditCardDailyTransactionLimit(CreditCardAccount creditCard, double requestAmount);
     public boolean validateCreditCardMonthlyTransactionLimit(CreditCardAccount creditCard, double requestAmount);
     public CardTransaction updateCardTransaction(CardTransaction ct);
}
