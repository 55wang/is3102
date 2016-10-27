/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
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
@Named(value = "interAccountTransferManagedBean")
@ViewScoped
public class InterAccountTransferManagedBean implements Serializable {
    
    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private OTPSessionBeanLocal otpBean;
    
    private String fromAccountNo;
    private String toAccountNo;
    private BigDecimal amount;
    private MainAccount ma;
    private List<DepositAccount> accounts = new ArrayList<>();
    
    private String inputTokenString;
    
    public InterAccountTransferManagedBean() {}
    
    @PostConstruct
    public void init() {
        ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        accounts = ma.getBankAcounts();
    }
    
    public void transfer() {
        
        if (!checkOptAndProceed()) {
            return;
        }
        
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNo);
        if (fromAccount != null && fromAccount.getBalance().compareTo(amount) < 0) {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
            return;
        }
        //TODO: need another authentication
        String result = transferBean.transferFromAccountToAccount(fromAccountNo, toAccountNo, amount);
        if (result.equals("SUCCESS")) {
            init();
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }
    
    public void sendOpt() {
        System.out.println("sendOTP clicked, sending otp to: " + ma.getCustomer().getPhone());
        JSUtils.callJSMethod("PF('myWizard').next()");
        otpBean.generateOTP(ma.getCustomer().getPhone());
    }
    
    private Boolean checkOptAndProceed() {
        if (inputTokenString == null || inputTokenString.isEmpty()) {
            MessageUtils.displayError("Please enter one time password!");
            return false;
        }
        if (!otpBean.isOTPExpiredByPhoneNumber(inputTokenString, ma.getCustomer().getPhone())) {
            if (otpBean.checkOTPValidByPhoneNumber(inputTokenString, ma.getCustomer().getPhone())) {
                return true;
            } else {
                MessageUtils.displayError("One Time Password Not Match!");
                return false;
            }
        } else {
            MessageUtils.displayError("One Time Password Expired!");
            return false;
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
     * @return the toAccountNo
     */
    public String getToAccountNo() {
        return toAccountNo;
    }

    /**
     * @param toAccountNo the toAccountNo to set
     */
    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }

    /**
     * @return the accounts
     */
    public List<DepositAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<DepositAccount> accounts) {
        this.accounts = accounts;
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
     * @return the inputTokenString
     */
    public String getInputTokenString() {
        return inputTokenString;
    }

    /**
     * @param inputTokenString the inputTokenString to set
     */
    public void setInputTokenString(String inputTokenString) {
        this.inputTokenString = inputTokenString;
    }
    
}
