/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface CardTransactionSessionBeanLocal {
     public Boolean createCardTransaction(CardTransaction ct);
}
