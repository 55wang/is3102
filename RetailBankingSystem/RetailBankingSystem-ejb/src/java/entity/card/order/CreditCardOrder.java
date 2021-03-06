/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.order;

import entity.card.account.CreditCardAccount;
import entity.customer.MainAccount;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import server.utilities.EnumUtils.CardApplicationStatus;

/**
 *
 * @author wang
 */
@Entity
public class CreditCardOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // info
    private CardApplicationStatus applicationStatus;

    // mapping
    @ManyToOne
    private MainAccount mainAccount;
    @OneToOne(cascade = {CascadeType.MERGE}, mappedBy = "creditCardOrder")
    private CreditCardAccount creditCardAccount;
    
    public CreditCardOrder() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }


    public CreditCardAccount getCreditCardAccount() {
        return creditCardAccount;
    }

    public void setCreditCardAccount(CreditCardAccount creditCardAccount) {
        this.creditCardAccount = creditCardAccount;
    }

    public CardApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(CardApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }


}
