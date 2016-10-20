/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Named(value = "cardViewPendingTransactionManagedBean")
@ViewScoped
public class CardViewPendingTransactionManagedBean implements Serializable {

    @EJB
    CardAcctSessionBeanLocal cardAcctSessionBean;
    
    @EJB
    CardTransactionSessionBeanLocal cardTransactionSessionBean;

    private List<CardTransaction> cts;

    public CardViewPendingTransactionManagedBean() {
    }

    @PostConstruct
    public void init() {
        setCts(cardTransactionSessionBean.getListCardTransactionsByStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION));
    }

    public void demoEODSettlement() {
        
    }

    public List<CardTransaction> getCts() {
        return cts;
    }

    public void setCts(List<CardTransaction> cts) {
        this.cts = cts;
    }


}
