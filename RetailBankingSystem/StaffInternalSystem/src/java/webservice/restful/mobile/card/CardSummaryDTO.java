/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.card;

import java.util.ArrayList;
import java.util.List;
import webservice.restful.mobile.summary.AccountDTO;

/**
 *
 * @author leiyang
 */
public class CardSummaryDTO {
    private List<CardDTO> creditCards = new ArrayList<>();
    private AccountDTO mobileAccount;

    /**
     * @return the creditCards
     */
    public List<CardDTO> getCreditCards() {
        return creditCards;
    }

    /**
     * @param creditCards the creditCards to set
     */
    public void setCreditCards(List<CardDTO> creditCards) {
        this.creditCards = creditCards;
    }

    /**
     * @return the mobileAccount
     */
    public AccountDTO getMobileAccount() {
        return mobileAccount;
    }

    /**
     * @param mobileAccount the mobileAccount to set
     */
    public void setMobileAccount(AccountDTO mobileAccount) {
        this.mobileAccount = mobileAccount;
    }
}
