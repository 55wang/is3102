/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "accountWithdrawManagedBean")
@ViewScoped
public class AccountWithdrawManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal bankAccountSessionBean;
    
    private Long accountNumber;
    private BigDecimal withdrawAmount;
    
    /**
     * Creates a new instance of WithdrawCurrentAccountManagedBean
     */
    public AccountWithdrawManagedBean() {
    }
    
    public void withdrawFromAccount() {
        String message = bankAccountSessionBean.withdrawFromAccount(getAccountNumber(), getWithdrawAmount());
        MessageUtils.displayInfo(message);
    }

    /**
     * @return the accountNumber
     */
    public Long getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the withdrawAmount
     */
    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    /**
     * @param withdrawAmount the withdrawAmount to set
     */
    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }
}
