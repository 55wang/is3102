/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.loan.LoanAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import utils.JSUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "loanLumsumPaymentManagedBean")
@ViewScoped
public class LoanLumsumPaymentManagedBean implements Serializable {

    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;
    @EJB
    private LoanPaymentSessionBeanLocal loanPaymentBena;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private OTPSessionBeanLocal otpBean;

    private String accountId;
    private String fromAccountNo;
    private Integer additionalAmount;
    private Double totalAmount;
    private LoanAccount loanAccount;
    private MainAccount ma;
    private List<CustomerDepositAccount> accounts = new ArrayList<>();
    
    private String inputTokenString;
    
    /**
     * Creates a new instance of LoanLumsumPaymentManagedBean
     */
    public LoanLumsumPaymentManagedBean() {
    }
    
    public void init() {
        System.out.println("Account id is: " + getAccountId());
        setLoanAccount(loanAccountBean.getLoanAccountByAccountNumber(getAccountId()));
        System.out.println("Account retrieved is: " + getLoanAccount().getAccountNumber());
        setAccounts(depositBean.getAllNonFixedCustomerAccounts(getLoanAccount().getMainAccount().getId()));
        ma = loanAccount.getMainAccount();
    }
    
    public Double totalAmount() {
        totalAmount = 10000.0 + additionalAmount * 1000;
        return totalAmount;
    }
    
    public void transfer() {
        
        if (!checkOptAndProceed()) {
            return;
        }
        
        String result = loanPaymentBena.loanLumsumPaymentFromAccount(loanAccount.getAccountNumber(), fromAccountNo, new BigDecimal(totalAmount()));
        if (result.equals("SUCCESS")) {
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        } else if (result.equals("FAIL1")){
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED2);
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
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the loanAccount
     */
    public LoanAccount getLoanAccount() {
        return loanAccount;
    }

    /**
     * @param loanAccount the loanAccount to set
     */
    public void setLoanAccount(LoanAccount loanAccount) {
        this.loanAccount = loanAccount;
    }

    /**
     * @return the accounts
     */
    public List<CustomerDepositAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<CustomerDepositAccount> accounts) {
        this.accounts = accounts;
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
     * @return the additionalAmount
     */
    public Integer getAdditionalAmount() {
        return additionalAmount;
    }

    /**
     * @param additionalAmount the additionalAmount to set
     */
    public void setAdditionalAmount(Integer additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    /**
     * @return the totalAmount
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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
