/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.card.account.DebitCardAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author leiyang
 */
@Entity // TODO: Only Current Account has cheque
public class CustomerDepositAccount extends DepositAccount {
    
    // info
    // Counter to decrease
    private Integer waivedFeesCounter = 0;
    private Integer waivedChargesCounter = 0;
    @Column(precision=18, scale=4)
    private BigDecimal previousBalance = new BigDecimal(0);
    
    // mapping
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "")
    private List<DebitCardAccount> debitCardAccount = new ArrayList<>();
    // REMARK: Type != SAVING
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "account")
    private List<Cheque> cheques = new ArrayList<>();
    
    /**
     * @return the waivedFeesCounter
     */
    public Integer getWaivedFeesCounter() {
        return waivedFeesCounter;
    }

    /**
     * @param waivedFeesCounter the waivedFeesCounter to set
     */
    public void setWaivedFeesCounter(Integer waivedFeesCounter) {
        this.waivedFeesCounter = waivedFeesCounter;
    }

    /**
     * @return the waivedChargesCounter
     */
    public Integer getWaivedChargesCounter() {
        return waivedChargesCounter;
    }

    /**
     * @param waivedChargesCounter the waivedChargesCounter to set
     */
    public void setWaivedChargesCounter(Integer waivedChargesCounter) {
        this.waivedChargesCounter = waivedChargesCounter;
    }

    /**
     * @return the previousBalance
     */
    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    /**
     * @param previousBalance the previousBalance to set
     */
    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }

    public List<DebitCardAccount> getDebitCardAccount() {
        return debitCardAccount;
    }

    public void setDebitCardAccount(List<DebitCardAccount> debitCardAccount) {
        this.debitCardAccount = debitCardAccount;
    }

    /**
     * @return the cheques
     */
    public List<Cheque> getCheques() {
        return cheques;
    }

    /**
     * @param cheques the cheques to set
     */
    public void setCheques(List<Cheque> cheques) {
        this.cheques = cheques;
    }
}
