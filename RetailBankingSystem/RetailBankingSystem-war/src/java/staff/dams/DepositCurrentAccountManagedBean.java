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
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "depositCurrentAccountManagedBean")
@ViewScoped
public class DepositCurrentAccountManagedBean implements Serializable {

    @EJB
    private CurrentAccountSessionBeanLocal currentAccountSessionBean;
    
    private List<CurrentAccount> accounts;
    private Long accountNumber;
    private BigDecimal depositAmount;
    
    public DepositCurrentAccountManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        // For testing
        setAccounts(currentAccountSessionBean.showAllAccounts());
    }
    
    public void depositIntoAccount() {
        String message = currentAccountSessionBean.depositIntoAccount(getAccountNumber(), getDepositAmount());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
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
     * @return the depositAmount
     */
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    /**
     * @param depositAmount the depositAmount to set
     */
    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

}
