/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
@Named(value = "accountWithdrawManagedBean")
@ViewScoped
public class AccountWithdrawManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal bankAccountSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
    private String accountNumber;
    private BigDecimal withdrawAmount;
    
    /**
     * Creates a new instance of WithdrawCurrentAccountManagedBean
     */
    public AccountWithdrawManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter AccountWithdrawManagedBean");
        a.setFunctionName("AccountWithdrawManagedBean @PostConstruct init()");
        a.setInput("Getting all AccountWithdrawManagedBean information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);

    }
    
    public void withdrawFromAccount() {
        String message = bankAccountSessionBean.withdrawFromAccount(getAccountNumber(), getWithdrawAmount());
        MessageUtils.displayInfo(message);
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
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
