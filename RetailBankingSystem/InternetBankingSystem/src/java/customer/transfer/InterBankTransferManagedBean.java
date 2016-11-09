/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BankEntity;
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
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;
import util.exception.dams.DepositAccountNotFoundException;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "interBankTransferManagedBean")
@ViewScoped
public class InterBankTransferManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;
    @EJB
    private OTPSessionBeanLocal otpBean;

    private String payeeId;
    private String bankId;
    private String fromAccountNo;
    private String purpose;
    private String transferLimitLeft;
    private BigDecimal amount;
    private Payee payee = new Payee();
    private MainAccount ma;
    private List<Payee> payees = new ArrayList<>();
    private List<BankEntity> bankList = new ArrayList<>();
    private List<CustomerDepositAccount> accounts = new ArrayList<>();
    private List<String> purposeOptions = CommonUtils.getEnumList(EnumUtils.TransferPurpose.class);
    private String transferMethod;

    private String inputTokenString;

    public InterBankTransferManagedBean() {
    }

    @PostConstruct
    public void init() {
        ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        setAccounts(depositBean.getAllNonFixedCustomerAccounts(ma.getId()));
        setPayees(transferBean.getPayeeFromUserIdWithType(ma.getId(), EnumUtils.PayeeType.LOCAL));
        setBankList(billBean.getActiveListBankEntities());
        payeeId = "New Receipiant";
        calculateTransferLimits();
    }

    public void changePayee() {
        if (getPayeeId().equals("New Receipiant")) {
            setPayee(new Payee());
        } else {
            setPayee(transferBean.getPayeeById(Long.parseLong(getPayeeId())));
        }
    }

    public void transfer() {

        if (!checkOptAndProceed()) {
            return;
        }

        try {

            DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNo);
            if (fromAccount != null && fromAccount.getBalance().compareTo(amount) < 0) {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
                return;
            }

            BigDecimal currentTransferLimit = new BigDecimal(transferLimitLeft);
            System.out.println(currentTransferLimit);
            System.out.println(amount);
            System.out.println(currentTransferLimit.compareTo(amount));
            if (currentTransferLimit.compareTo(amount) < 0) {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.EXCEED_TRANSFER_LIMIT);
                return;
            }

            if (getPayeeId().equals("New Receipiant")) {
                BankEntity bank = billBean.getBankEntityById(Long.parseLong(bankId));
                payee.setBankCode(bank.getBankCode());
                payee.setType(EnumUtils.PayeeType.LOCAL);
                payee.setMainAccount(ma);
                payee.setFromName(ma.getCustomer().getFullName());
                Payee result = transferBean.createPayee(payee);
                if (result != null) {
                    transferClearing();
                    JSUtils.callJSMethod("PF('myWizard').next()");
                    MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
                } else {
                    JSUtils.callJSMethod("PF('myWizard').back()");
                    MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
                }
            } else {
                payee = transferBean.getPayeeById(Long.parseLong(payeeId));
                transferClearing();
                JSUtils.callJSMethod("PF('myWizard').next()");
                MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            }

        } catch (DepositAccountNotFoundException e) {
            System.out.println("DepositAccountNotFoundException InterBankTransferManagedBean transfer()");
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }

    public void transferIBG() {
        transferMethod = "IBG";
        transfer();
    }

    public void transferFAST() {
        transferMethod=  "FAST";
        transfer();
    }

    public String getBankName(String bankCode) {
        if (bankCode == null || bankCode.equals("")) {
            return "No bank selected";
        }
        BankEntity b = billBean.getBankEntityByCode(bankCode);
        return b.getName();
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

    private void transferClearing() {
        try {

            DepositAccount da = depositBean.getAccountFromId(fromAccountNo);
            System.out.println("----------------FAST transfer clearing----------------");
            TransferRecord tr = new TransferRecord();
            tr.setAccountNumber(payee.getAccountNumber());
            tr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
            tr.setAmount(amount);
            tr.setToBankCode(payee.getBankCode());
            tr.setToBranchCode("010");// dummy
            tr.setName(payee.getName());
            tr.setMyInitial(payee.getMyInitial());
            tr.setFromName(payee.getFromName());
            tr.setPurpose(EnumUtils.TransferPurpose.getEnum(purpose));
            tr.setFromAccount(da);
            tr.setType(EnumUtils.PayeeType.LOCAL);
            tr.setActionType(EnumUtils.TransactionType.TRANSFER);
            if (transferMethod.equals("FAST")) {
                webserviceBean.transferClearingFAST(tr);
            } else {
                webserviceBean.transferClearingSACH(tr);
            }
            depositBean.transferFromAccount(da, amount);
            calculateTransferLimits();

        } catch (DepositAccountNotFoundException e) {
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }

    private void calculateTransferLimits() {
        BigDecimal todayTransferAmount = transferBean.getTodayBankTransferAmount(ma, EnumUtils.PayeeType.LOCAL);
        BigDecimal currentTransferLimit = new BigDecimal(ma.getTransferLimits().getDailyIntraBankLimit().toString());
        transferLimitLeft = currentTransferLimit.subtract(todayTransferAmount).setScale(2).toString();
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
     * @return the payee
     */
    public Payee getPayee() {
        return payee;
    }

    /**
     * @param payee the payee to set
     */
    public void setPayee(Payee payee) {
        this.payee = payee;
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
     * @return the bankList
     */
    public List<BankEntity> getBankList() {
        return bankList;
    }

    /**
     * @param bankList the bankList to set
     */
    public void setBankList(List<BankEntity> bankList) {
        this.bankList = bankList;
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
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
     * @return the purposeOptions
     */
    public List<String> getPurposeOptions() {
        return purposeOptions;
    }

    /**
     * @param purposeOptions the purposeOptions to set
     */
    public void setPurposeOptions(List<String> purposeOptions) {
        this.purposeOptions = purposeOptions;
    }

    /**
     * @return the bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
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

    public String getTransferMethod() {
        return transferMethod;
    }

    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod;
    }

}
