/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.bill;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BillingOrg;
import entity.bill.Organization;
import entity.common.BillTransferRecord;
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
import util.exception.common.MainAccountNotExistException;
import util.exception.dams.DepositAccountNotFoundException;
import util.exception.dams.UpdateDepositAccountException;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "payCCBillManagedBean")
@ViewScoped
public class PayCCBillManagedBean implements Serializable {

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;
    @EJB
    private OTPSessionBeanLocal otpBean;

    private String fromAccountNo;
    private String ccBillOrgId;
    private BigDecimal amount;
    private MainAccount ma;
    private String referenceNumber;
    private BillingOrg bo = new BillingOrg();
    private String newCcBillOrgId;
    private List<BillingOrg> ccBillList = new ArrayList<>();
    private List<CustomerDepositAccount> accounts = new ArrayList<>();
    private List<Organization> billOrgsOptions;

    private String inputTokenString;

    public PayCCBillManagedBean() {
    }

    @PostConstruct
    public void init() {
        try{
            ma = mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName());
        }catch(MainAccountNotExistException ex){
            System.out.println("init.MainAccountNotExistException");
        }
        setAccounts(depositBean.getAllNonFixedCustomerAccounts(ma.getId()));
        ccBillList = billBean.getCreditCardBillingMainAccountId(ma.getId());
        setBillOrgsOptions(billBean.getCreditCardOrganization());
        ccBillOrgId = "New Receipiant";

    }

    public void changeBO() {
        if (ccBillOrgId.equals("New Receipiant")) {
            setBo(new BillingOrg());
        } else {
            setBo(billBean.getBillingOrganizationById(Long.parseLong(ccBillOrgId)));
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

        if (ccBillOrgId.equals("New Receipiant")) {
            ccBillOrgId = newCcBillOrgId;
            if (!GenerateAccountAndCCNumber.isValidCreditCardNumber(referenceNumber)) {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.CC_NUMBER_INVALID);
                return;
            }
            Organization o = billBean.getOrganizationById(Long.parseLong(ccBillOrgId));
            getBo().setOrganization(o);
            getBo().setBillReference(getReferenceNumber());
            getBo().setMainAccount(getMa());
            BillingOrg result = billBean.createBillingOrganization(getBo());

            if (result != null) {
                setBo(result);
                transferClearing();
                JSUtils.callJSMethod("PF('myWizard').next()");
                MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            } else {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
            }
        } else {
            transferClearing();
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        }

        } catch (DepositAccountNotFoundException e) {
            System.out.println("DepositAccountNotFoundException PayCCBillManagedBean.java transfer()");
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }

    private void transferClearing() {
        try {
            
        DepositAccount da = depositBean.getAccountFromId(fromAccountNo);

        System.out.println("----------------Bill Payment clearing----------------");
        BillTransferRecord btr = new BillTransferRecord();
        btr.setBillReferenceNumber(bo.getBillReference());// it will be credit card number
        btr.setOrganizationName(bo.getOrganization().getName());
        btr.setAmount(amount);
        btr.setPartnerBankCode(bo.getOrganization().getPartnerBankCode());
        btr.setPartnerBankAccount(bo.getOrganization().getPartnerBankAccount());
        btr.setFromAccount(da);
        btr.setSettled(false);
        btr.setShortCode(bo.getOrganization().getShortCode());
        btr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        btr.setActionType(EnumUtils.TransactionType.CCSPENDING);
        transferBean.createBillTransferRecord(btr);
        depositBean.payBillFromAccount(da, amount);
        webserviceBean.billingClearingSACH(btr);
        

        } catch (DepositAccountNotFoundException e) {
            System.out.println("DepositAccountNotFoundException | UpdateDepositAccountException PayCCBillManagedBean.java transfer()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }

    public String getBillName(String id) {
        Organization o = billBean.getOrganizationById(Long.parseLong(id));
        return o.getName();
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
     * @return the ccBillOrgId
     */
    public String getCcBillOrgId() {
        return ccBillOrgId;
    }

    /**
     * @param ccBillOrgId the ccBillOrgId to set
     */
    public void setCcBillOrgId(String ccBillOrgId) {
        this.ccBillOrgId = ccBillOrgId;
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
     * @return the ma
     */
    public MainAccount getMa() {
        return ma;
    }

    /**
     * @param ma the ma to set
     */
    public void setMa(MainAccount ma) {
        this.ma = ma;
    }

    /**
     * @return the ccBillList
     */
    public List<BillingOrg> getCcBillList() {
        return ccBillList;
    }

    /**
     * @param ccBillList the ccBillList to set
     */
    public void setCcBillList(List<BillingOrg> ccBillList) {
        this.ccBillList = ccBillList;
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

    /**
     * @return the billOrgsOptions
     */
    public List<Organization> getBillOrgsOptions() {
        return billOrgsOptions;
    }

    /**
     * @param billOrgsOptions the billOrgsOptions to set
     */
    public void setBillOrgsOptions(List<Organization> billOrgsOptions) {
        this.billOrgsOptions = billOrgsOptions;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the bo
     */
    public BillingOrg getBo() {
        return bo;
    }

    /**
     * @param bo the bo to set
     */
    public void setBo(BillingOrg bo) {
        this.bo = bo;
    }

    /**
     * @return the newCcBillOrgId
     */
    public String getNewCcBillOrgId() {
        return newCcBillOrgId;
    }

    /**
     * @param newCcBillOrgId the newCcBillOrgId to set
     */
    public void setNewCcBillOrgId(String newCcBillOrgId) {
        this.newCcBillOrgId = newCcBillOrgId;
    }

}
