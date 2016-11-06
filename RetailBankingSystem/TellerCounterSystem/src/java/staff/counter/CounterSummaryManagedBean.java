/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.counter;

import ejb.session.counter.TellerCounterSessionBeanLocal;
import entity.counter.ServiceChargeTransaction;
import entity.counter.TellerCounter;
import entity.customer.MainAccount;
import entity.staff.ServiceCharge;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "counterSummaryManagedBean")
@ViewScoped
public class CounterSummaryManagedBean implements Serializable {

    @EJB
    private TellerCounterSessionBeanLocal counterBean;

    private TellerCounter tellerCounter;
    private Boolean isAdd = true;
    private BigDecimal amount;

    /**
     * Creates a new instance of CounterSummaryManagedBean
     */
    public CounterSummaryManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        tellerCounter = SessionUtils.getTellerCounter();
    }

    public void chargeFee() {
        // tc logics
        TellerCounter tc = SessionUtils.getTellerCounter();
        tc = counterBean.getTellerCounterById(tc.getId());
        if (isAdd) {
            tc.addCash(amount);
        } else {
            if (tc.hasEnoughCash(amount)) {
                tc.removeCash(amount);
            } else {
                MessageUtils.displayError("Not Enough Cash in Counter!");
                return;
            }
        }
        
        tc = counterBean.updateTellerCounter(tc);
        SessionUtils.setTellerCounter(tc);

        tellerCounter = tc;
        MessageUtils.displayInfo("Service Fee Charge Recorded!");
    }

    /**
     * @return the isAdd
     */
    public Boolean getIsAdd() {
        return isAdd;
    }

    /**
     * @param isAdd the isAdd to set
     */
    public void setIsAdd(Boolean isAdd) {
        this.isAdd = isAdd;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the tellerCounter
     */
    public TellerCounter getTellerCounter() {
        return tellerCounter;
    }

    /**
     * @param tellerCounter the tellerCounter to set
     */
    public void setTellerCounter(TellerCounter tellerCounter) {
        this.tellerCounter = tellerCounter;
    }

}
