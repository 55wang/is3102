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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "accountDepositManagedBean")
@ViewScoped
public class AccountDepositManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal customerDepositSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
    private String accountType;
    private String ACCOUNT_TYPE_CURRENT = EnumUtils.DepositAccountType.CURRENT.toString();
    private String ACCOUNT_TYPE_FIXED = EnumUtils.DepositAccountType.FIXED.toString();
    private String ACCOUNT_TYPE_SAVING = EnumUtils.DepositAccountType.SAVING.toString();
    private String accountNumber;
    private BigDecimal depositAmount;
    
    public AccountDepositManagedBean() {}
    
    @PostConstruct
    public void init() {
        
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter deposit account.xhtml");
        a.setFunctionName("AccountDepositManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all AccountDepositManagedBean information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);

    }
    
    public void depositIntoAccount(ActionEvent event) {
        String message = customerDepositSessionBean.depositIntoAccount(getAccountNumber(), getDepositAmount());
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

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the ACCOUNT_TYPE_CURRENT
     */
    public String getACCOUNT_TYPE_CURRENT() {
        return ACCOUNT_TYPE_CURRENT;
    }

    /**
     * @param ACCOUNT_TYPE_CURRENT the ACCOUNT_TYPE_CURRENT to set
     */
    public void setACCOUNT_TYPE_CURRENT(String ACCOUNT_TYPE_CURRENT) {
        this.ACCOUNT_TYPE_CURRENT = ACCOUNT_TYPE_CURRENT;
    }

    /**
     * @return the ACCOUNT_TYPE_FIXED
     */
    public String getACCOUNT_TYPE_FIXED() {
        return ACCOUNT_TYPE_FIXED;
    }

    /**
     * @param ACCOUNT_TYPE_FIXED the ACCOUNT_TYPE_FIXED to set
     */
    public void setACCOUNT_TYPE_FIXED(String ACCOUNT_TYPE_FIXED) {
        this.ACCOUNT_TYPE_FIXED = ACCOUNT_TYPE_FIXED;
    }

    /**
     * @return the ACCOUNT_TYPE_SAVING
     */
    public String getACCOUNT_TYPE_SAVING() {
        return ACCOUNT_TYPE_SAVING;
    }

    /**
     * @param ACCOUNT_TYPE_SAVING the ACCOUNT_TYPE_SAVING to set
     */
    public void setACCOUNT_TYPE_SAVING(String ACCOUNT_TYPE_SAVING) {
        this.ACCOUNT_TYPE_SAVING = ACCOUNT_TYPE_SAVING;
    }
}
