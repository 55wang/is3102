/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.bill;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "payMerlionCreditCardBillManagedBean")
@ViewScoped
public class PayMerlionCreditCardBillManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    private String fromAccountNo;
    private String toCreditCardNo;
    private BigDecimal amount;
    private List<DepositAccount> depositAccounts = new ArrayList<>();
    private List<CreditCardAccount> creditCardAccounts = new ArrayList<>();
    
    public PayMerlionCreditCardBillManagedBean() {}
    
    @PostConstruct
    public void init() {
        MainAccount ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        setDepositAccounts(ma.getBankAcounts());
        setCreditCardAccounts(ma.getCreditCardAccounts());
    }
    
    public void transfer() {
        DepositAccount fromAccount = depositBean.getAccountFromId(getFromAccountNo());
        if (fromAccount != null && fromAccount.getBalance().compareTo(getAmount()) < 0) {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
        }
        //TODO: need another authentication
        String result = transferBean.transferFromAccountToCreditCard(getFromAccountNo(), getToCreditCardNo(), getAmount());
        if (result.equals("SUCCESS")) {
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }

    /**
     * @return the fromAccountNo
     */
    public String getFromAccountNo() {
        return fromAccountNo;
    }

    /**
     * @param fromAccountNo the fromAccountNo to set
     */
    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    /**
     * @return the toCreditCardNo
     */
    public String getToCreditCardNo() {
        return toCreditCardNo;
    }

    /**
     * @param toCreditCardNo the toCreditCardNo to set
     */
    public void setToCreditCardNo(String toCreditCardNo) {
        this.toCreditCardNo = toCreditCardNo;
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
     * @return the depositAccounts
     */
    public List<DepositAccount> getDepositAccounts() {
        return depositAccounts;
    }

    /**
     * @param depositAccounts the depositAccounts to set
     */
    public void setDepositAccounts(List<DepositAccount> depositAccounts) {
        this.depositAccounts = depositAccounts;
    }

    /**
     * @return the creditCardAccounts
     */
    public List<CreditCardAccount> getCreditCardAccounts() {
        return creditCardAccounts;
    }

    /**
     * @param creditCardAccounts the creditCardAccounts to set
     */
    public void setCreditCardAccounts(List<CreditCardAccount> creditCardAccounts) {
        this.creditCardAccounts = creditCardAccounts;
    }
    
}
