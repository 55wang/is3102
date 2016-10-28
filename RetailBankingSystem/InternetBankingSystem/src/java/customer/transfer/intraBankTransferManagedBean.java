/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import entity.bill.Payee;
import entity.common.TransferRecord;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
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
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "intraBankTransferManagedBean")
@ViewScoped
public class intraBankTransferManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private OTPSessionBeanLocal otpBean;
    
    private String fromAccountNo;
    private String payeeId;
    private String transferLimitLeft;
    private String toAccountNo;
    private String toName;
    private String myInitial;
    private BigDecimal amount;
    private List<CustomerDepositAccount> accounts = new ArrayList<>();
    private List<Payee> payees = new ArrayList<>();
    private MainAccount ma;
    
    private String inputTokenString;
    
    public intraBankTransferManagedBean() {}
    
    @PostConstruct
    public void init() {
        ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        payees = transferBean.getPayeeFromUserIdWithType(ma.getId(), EnumUtils.PayeeType.MERLION);
        accounts = depositBean.getAllNonFixedCustomerAccounts(ma.getId());
        calculateTransferLimits();
    }
    
    public void adhocTransfer() {
        
        if (!checkOptAndProceed()) {
            return;
        }
        
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNo);
        if (fromAccount != null && fromAccount.getBalance().compareTo(amount) < 0) {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
            return;
        }
        
        BigDecimal currentTransferLimit = new BigDecimal(transferLimitLeft);
        if (currentTransferLimit.compareTo(amount) < 0) {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.EXCEED_TRANSFER_LIMIT);
            return;
        }
        
        try {
            DepositAccount da = depositBean.getAccountFromId(getToAccountNo());
            if (da == null) {
                MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
                return;
            }
        } catch (Exception e) {
            MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
            return;
        }
        //TODO: need another authentication
        TransferRecord tr = new TransferRecord();
        
        tr.setAccountNumber(getToAccountNo());
        tr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        tr.setAmount(amount);
        tr.setName(getToName());
        tr.setMyInitial(getMyInitial());
        tr.setFromName(ma.getCustomer().getFullName());
        tr.setFromAccount(fromAccount);
        DepositAccount toAccount = depositBean.getAccountFromId(getToAccountNo());
        tr.setToAccount(toAccount);
        tr.setType(EnumUtils.PayeeType.MERLION);
        transferBean.createTransferRecord(tr);
        
        String result = transferBean.transferFromAccountToAccount(getFromAccountNo(), getToAccountNo(), getAmount());
        if (result.equals("SUCCESS")) {
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }
    
    public void transferToPayee() {
        
        if (!checkOptAndProceed()) {
            return;
        }
        
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNo);
        if (fromAccount != null && fromAccount.getBalance().compareTo(amount) < 0) {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
            return;
        }
        
        BigDecimal currentTransferLimit = new BigDecimal(transferLimitLeft);
        if (currentTransferLimit.compareTo(amount) < 0) {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.EXCEED_TRANSFER_LIMIT);
            return;
        }
        
        Payee payee = transferBean.getPayeeById(Long.parseLong(getPayeeId()));
        
        try {
            DepositAccount da = depositBean.getAccountFromId(payee.getAccountNumber());
            if (da == null) {
                MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
                return;
            }
        } catch (Exception e) {
            MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
            return;
        }
        //TODO: need another authentication
        TransferRecord tr = new TransferRecord();
        
        tr.setAccountNumber(payee.getAccountNumber());
        tr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        tr.setAmount(amount);
        tr.setName(payee.getName());
        tr.setMyInitial(payee.getMyInitial());
        tr.setFromName(payee.getFromName());
        tr.setFromAccount(fromAccount);
        DepositAccount toAccount = depositBean.getAccountFromId(payee.getAccountNumber());
        tr.setToAccount(toAccount);
        tr.setType(EnumUtils.PayeeType.MERLION);
        transferBean.createTransferRecord(tr);
        
        String result = transferBean.transferFromAccountToAccount(getFromAccountNo(), payee.getAccountNumber(), getAmount());
        if (result.equals("SUCCESS")) {
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            calculateTransferLimits();
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }
    
    
    
    public void sendOpt() {
        System.out.println("sendOTP clicked, sending otp to: " + ma.getCustomer().getPhone());
        JSUtils.callJSMethod("PF('myWizard').next()");
        otpBean.generateOTP(ma.getCustomer().getPhone());
        Payee payee = transferBean.getPayeeById(Long.parseLong(getPayeeId()));
        toAccountNo = payee.getAccountNumber();
    }
    
    private void calculateTransferLimits() {
        BigDecimal todayTransferAmount = transferBean.getTodayBankTransferAmount(ma, EnumUtils.PayeeType.MERLION);
        BigDecimal currentTransferLimit = new BigDecimal(ma.getTransferLimits().getDailyInterBankLimit().toString());
        transferLimitLeft = currentTransferLimit.subtract(todayTransferAmount).setScale(2).toString();
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
     * @return the payees
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * @param payees the payees to set
     */
    public void setPayees(List<Payee> payees) {
        this.payees = payees;
    }

    /**
     * @return the payeeId
     */
    public String getPayeeId() {
        return payeeId;
    }

    /**
     * @param payeeId the payeeId to set
     */
    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    /**
     * @return the transferLimitLeft
     */
    public String getTransferLimitLeft() {
        return transferLimitLeft;
    }

    /**
     * @param transferLimitLeft the transferLimitLeft to set
     */
    public void setTransferLimitLeft(String transferLimitLeft) {
        this.transferLimitLeft = transferLimitLeft;
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
     * @return the toName
     */
    public String getToName() {
        return toName;
    }

    /**
     * @param toName the toName to set
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * @return the myInitial
     */
    public String getMyInitial() {
        return myInitial;
    }

    /**
     * @param myInitial the myInitial to set
     */
    public void setMyInitial(String myInitial) {
        this.myInitial = myInitial;
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
