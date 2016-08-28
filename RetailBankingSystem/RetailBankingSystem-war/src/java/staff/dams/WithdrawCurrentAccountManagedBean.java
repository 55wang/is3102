/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.CurrentAccountSessionBeanLocal;
import entity.CurrentAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "withdrawCurrentAccountManagedBean")
@ViewScoped
public class WithdrawCurrentAccountManagedBean implements Serializable {

    @EJB
    private CurrentAccountSessionBeanLocal currentAccountSessionBean;
    
    private List<CurrentAccount> accounts;
    private Long accountNumber;
    private BigDecimal withdrawAmount;
    
    /**
     * Creates a new instance of WithdrawCurrentAccountManagedBean
     */
    public WithdrawCurrentAccountManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        // For testing
        setAccounts(currentAccountSessionBean.showAllAccounts());
    }
    
    public void withdrawFromAccount() {
        String message = currentAccountSessionBean.withdrawFromAccount(getAccountNumber(), getWithdrawAmount());
        MessageUtils.displayInfo(message);
    }

    /**
     * @return the accounts
     */
    public List<CurrentAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<CurrentAccount> accounts) {
        this.accounts = accounts;
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
