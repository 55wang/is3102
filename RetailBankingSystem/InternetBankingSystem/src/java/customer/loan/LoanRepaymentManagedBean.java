/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import entity.loan.LoanAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import util.exception.dams.DepositAccountNotFoundException;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "loanRepaymentManagedBean")
@ViewScoped
public class LoanRepaymentManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private LoanPaymentSessionBeanLocal loanPaymentBean;
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;
    @EJB
    private OTPSessionBeanLocal otpBean;

    private String fromAccountNo;
    private String toLoanAccountNo;
    private BigDecimal amount;
    private MainAccount ma;
    private List<CustomerDepositAccount> depositAccounts = new ArrayList<>();
    private List<LoanAccount> loanAccounts = new ArrayList<>();

    private String inputTokenString;

    /**
     * Creates a new instance of LoanRepaymentManagedBean
     */
    public LoanRepaymentManagedBean() {
    }

    @PostConstruct
    public void init() {
        ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        depositAccounts = depositBean.getAllNonFixedCustomerAccounts(ma.getId());
        loanAccounts = loanAccountBean.getActiveLoanAccountListByMainAccountId(ma.getId());
    }

    public void transfer() {

        if (!checkOptAndProceed()) {
            return;
        }

        try {

            DepositAccount fromAccount = depositBean.getAccountFromId(getFromAccountNo());
            if (fromAccount != null && fromAccount.getBalance().compareTo(getAmount()) < 0) {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
            }
            //TODO: need another authentication
            String result = loanPaymentBean.loanRepaymentFromAccount(toLoanAccountNo, getFromAccountNo(), getAmount());
            if (result.equals("SUCCESS")) {
                JSUtils.callJSMethod("PF('myWizard').next()");
                MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            } else if (result.equals("FAIL1")) {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
            } else if (result.equals("FAIL2")) {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED2);
            } else {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED3);
            }

        } catch (DepositAccountNotFoundException e) {
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
     * @return the toLoanAccountNo
     */
    public String getToLoanAccountNo() {
        return toLoanAccountNo;
    }

    /**
     * @param toLoanAccountNo the toLoanAccountNo to set
     */
    public void setToLoanAccountNo(String toLoanAccountNo) {
        this.toLoanAccountNo = toLoanAccountNo;
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
    public List<CustomerDepositAccount> getDepositAccounts() {
        return depositAccounts;
    }

    /**
     * @param depositAccounts the depositAccounts to set
     */
    public void setDepositAccounts(List<CustomerDepositAccount> depositAccounts) {
        this.depositAccounts = depositAccounts;
    }

    /**
     * @return the loanAccounts
     */
    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    /**
     * @param loanAccounts the loanAccounts to set
     */
    public void setLoanAccounts(List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
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
