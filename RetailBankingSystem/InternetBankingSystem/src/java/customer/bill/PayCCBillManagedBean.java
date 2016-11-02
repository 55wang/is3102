/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.bill;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BillingOrg;
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
    
    private String fromAccountNo;
    private String ccBillOrgId;
    private BigDecimal amount;
    private MainAccount ma;
    private List<BillingOrg> ccBillList = new ArrayList<>();
    private List<CustomerDepositAccount> accounts = new ArrayList<>();
    
    private String inputTokenString;
    
    public PayCCBillManagedBean() {}
    
    @PostConstruct
    public void init() {
        setMa(loginBean.getMainAccountByUserID(SessionUtils.getUserName()));
        setAccounts(depositBean.getAllNonFixedCustomerAccounts(ma.getId()));
        ccBillList = billBean.getCreditCardBillingMainAccountId(ma.getId());
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
        transferClearing();
        JSUtils.callJSMethod("PF('myWizard').next()");
        MessageUtils.displayError(ConstantUtils.TRANSFER_SUCCESS);
    }
    
    
    private void transferClearing() {
        DepositAccount da = depositBean.getAccountFromId(fromAccountNo);
        BillingOrg bo = billBean.getBillingOrganizationById(Long.parseLong(ccBillOrgId));
        
        System.out.println("Bill Payment clearing");
        BillTransferRecord btr = new BillTransferRecord();
        btr.setBillReferenceNumber(bo.getBillReference());// it will be credit card number
        btr.setOrganizationName(bo.getOrganization().getName());
        btr.setAmount(amount);
        btr.setPartnerBankCode(bo.getOrganization().getPartnerBankCode());
        btr.setSettled(false);
        btr.setShortCode(bo.getOrganization().getShortCode());
        btr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        btr.setActionType(EnumUtils.TransactionType.CCSPENDING);
        webserviceBean.billingClearingSACH(btr);
        da.removeBalance(amount);
        depositBean.updateAccount(da);
        transferBean.createBillTransferRecord(btr);
        
    }
    
    public String getBillName(String id) {
        BillingOrg bo = billBean.getBillingOrganizationById(Long.parseLong(ccBillOrgId));
        return bo.getOrganization().getName() + " - " + bo.getBillReference();
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
  
}
