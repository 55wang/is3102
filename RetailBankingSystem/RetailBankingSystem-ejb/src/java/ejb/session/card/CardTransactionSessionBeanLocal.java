/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface CardTransactionSessionBeanLocal {
     public Boolean createCardTransaction(CardTransaction ct);
     public List<CardTransaction> retrieveTransactionByDate(Date start, Date end);
}
