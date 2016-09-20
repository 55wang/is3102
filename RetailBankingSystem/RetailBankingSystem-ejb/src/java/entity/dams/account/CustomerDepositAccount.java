/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.embedded.CumulatedInterest;
import entity.embedded.TransferLimits;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import utils.EnumUtils.DepositAccountType;

/**
 *
 * @author leiyang
 */
@Entity // TODO: Only Current Account has cheque
public class CustomerDepositAccount extends DepositAccount {
    
    // Counter to decrease
    private Integer waivedFeesCounter = 0;
    private Integer waivedChargesCounter = 0;
    @Embedded
    private TransferLimits transferLimits = new TransferLimits();
    @Embedded
    private CumulatedInterest cumulatedInterest = new CumulatedInterest();
    
    @Column(precision=12, scale=2)
    private BigDecimal previousBalance = new BigDecimal(0);
    
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
     * @return the transferLimits
     */
    public TransferLimits getTransferLimits() {
        return transferLimits;
    }

    /**
     * @param transferLimits the transferLimits to set
     */
    public void setTransferLimits(TransferLimits transferLimits) {
        this.transferLimits = transferLimits;
    }

    /**
     * @return the cumulatedInterest
     */
    public CumulatedInterest getCumulatedInterest() {
        return cumulatedInterest;
    }

    /**
     * @param cumulatedInterest the cumulatedInterest to set
     */
    public void setCumulatedInterest(CumulatedInterest cumulatedInterest) {
        this.cumulatedInterest = cumulatedInterest;
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
}
