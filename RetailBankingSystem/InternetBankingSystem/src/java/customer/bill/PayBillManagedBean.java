/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.bill;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BillingOrg;
import entity.common.BillTransferRecord;
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
import server.utilities.GenerateAccountAndCCNumber;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "payBillManagedBean")
@ViewScoped
public class PayBillManagedBean implements Serializable {

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

    private String fromAccountNo;
    private String ccBillOrgId;
    private BigDecimal amount;
    private MainAccount ma;
    private List<BillingOrg> ccBillList = new ArrayList<>();
    private List<DepositAccount> accounts = new ArrayList<>();

    public PayBillManagedBean() {
    }

    @PostConstruct
    public void init() {
        setMa(loginBean.getMainAccountByUserID(SessionUtils.getUserName()));
        setAccounts(getMa().getBankAcounts());
        ccBillList = billBean.getBillingOrgMainAccountId(ma.getId());
    }

    public void transfer() {

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
        btr.setPartnerBankCode(bo.getOrganization().getPartnerBankCode());
        btr.setSettled(false);
        btr.setAmount(amount);
        btr.setShortCode(bo.getOrganization().getShortCode());
        btr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        webserviceBean.billingClearingSACH(btr);
        da.removeBalance(amount);
        depositBean.updateAccount(da);
        transferBean.createBillTransferRecord(btr);

    }

    public String getBillName(String id) {
        BillingOrg bo = billBean.getBillingOrganizationById(Long.parseLong(ccBillOrgId));
        return bo.getOrganization().getName() + " - " + bo.getBillReference();
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
    public List<DepositAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<DepositAccount> accounts) {
        this.accounts = accounts;
    }

}
