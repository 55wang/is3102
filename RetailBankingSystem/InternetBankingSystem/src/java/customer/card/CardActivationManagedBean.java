/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.NotNull;
import server.utilities.ConstantUtils;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "cardActivationManagedBean")
@ViewScoped
public class CardActivationManagedBean implements Serializable {

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @NotNull(message = "ID is required")
    private String identityNumber;
    private Date birthday;
    @NotNull
    private String cardNumber;
    @NotNull
    private String cvv;

    /**
     * Creates a new instance of CardActivationManagedBean
     */
    public CardActivationManagedBean() {
    }

    public void activateCreditCard() {
        
        if (cardAcctSessionBean.activateCreditCard(identityNumber, birthday, cardNumber, cvv) == null) {
            MessageUtils.displayError("Invalid input. Please check");
        } else {
            MessageUtils.displayInfo("Your card is successfully activated");
            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
            RedirectUtils.redirect("https://" + ConstantUtils.ipAddress +":8181/InternetBankingSystem/personal_cards/activation_success.xhtml");

        }

    }

    public void activateDebitCard() {

        if (cardAcctSessionBean.activateDebitCard(identityNumber, birthday, cardNumber, cvv) == null) {
            MessageUtils.displayError("Invalid input. Please check");
        } else {
            MessageUtils.displayInfo("Your card is successfully activated");
            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
            RedirectUtils.redirect("https://" + ConstantUtils.ipAddress +":8181/InternetBankingSystem/personal_cards/activation_success.xhtml");

        }

    }

    /**
     * @return the identityNumber
     */
    public String getIdentityNumber() {
        return identityNumber;
    }

    /**
     * @param identityNumber the identityNumber to set
     */
    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    /**
     * @return the birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the cvv
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @param cvv the cvv to set
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

}
