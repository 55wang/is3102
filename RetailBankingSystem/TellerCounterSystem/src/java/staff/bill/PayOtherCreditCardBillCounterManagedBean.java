/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bill;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BillingOrg;
import entity.bill.Organization;
import entity.common.BillTransferRecord;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;
import util.exception.dams.DepositAccountNotFoundException;
import util.exception.dams.UpdateDepositAccountException;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "payOtherCreditCardBillCounterManagedBean")
@ViewScoped
public class PayOtherCreditCardBillCounterManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;
    @EJB
    private BillSessionBeanLocal billBean;

    private String selectedBillId;
    private Long selectedBillOrgId;

    private String fromAccountNumber;
    private String toReferenceNumber;
    private BigDecimal amount;

    private String customerIC;

    private MainAccount mainAccount;

    private BillingOrg billingOrg;
    private List<Organization> billOrgsOptions;
    private List<BillingOrg> addedBillOrgs;
    private List<DepositAccount> depositAccounts = new ArrayList<>();

    /**
     * Creates a new instance of PayOtherCreditCardBillCounterManagedBean
     */
    public PayOtherCreditCardBillCounterManagedBean() {
    }

    public void retrieveMainAccount() {

        try {
            billingOrg = new BillingOrg();
            mainAccount = loginBean.getMainAccountByUserIC(getCustomerIC());
            depositAccounts = mainAccount.getBankAcounts();
            billOrgsOptions = billBean.getCreditCardOrganization();
            addedBillOrgs = billBean.getCreditCardBillingMainAccountId(mainAccount.getId());
            selectedBillId = "New Receipiant";
        } catch (Exception e) {
            mainAccount = null;
            MessageUtils.displayError("Customer Main Account Not Found!");
        }

    }

    public void changeBillOrg() {
        if (selectedBillId.equals("New Receipiant")) {
            billingOrg = new BillingOrg();
        } else {
            billingOrg = billBean.getBillingOrganizationById(Long.parseLong(selectedBillId));
        }
    }

    public void transfer() {

        try {
            DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNumber);
            if (fromAccount != null && fromAccount.getBalance().compareTo(amount) < 0) {
                MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
                return;
            }

            if (selectedBillId.equals("New Receipiant")) {
                Organization o = billBean.getOrganizationById(selectedBillOrgId);

                billingOrg.setOrganization(o);
                billingOrg.setBillReference(toReferenceNumber);
                billingOrg.setMainAccount(mainAccount);
                BillingOrg result = billBean.createBillingOrganization(billingOrg);

                if (result != null) {
                    transferClearing();
                    MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
                } else {
                    MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
                }

            } else {
                billingOrg = billBean.getBillingOrganizationById(Long.parseLong(selectedBillId));
                transferClearing();
                MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            }

        } catch (DepositAccountNotFoundException e) {
            System.out.println("DepositAccountNotFoundException PayOtherCreditCardBillCounterManagedBean transfer()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }

    private void transferClearing() {
        try {
            DepositAccount da = depositBean.getAccountFromId(fromAccountNumber);

            System.out.println("----------------Bill Payment clearing----------------");
            BillTransferRecord btr = new BillTransferRecord();
            btr.setBillReferenceNumber(billingOrg.getBillReference());// it will be credit card number
            btr.setOrganizationName(billingOrg.getOrganization().getName());
            btr.setAmount(amount);
            btr.setPartnerBankCode(billingOrg.getOrganization().getPartnerBankCode());
            btr.setPartnerBankAccount(billingOrg.getOrganization().getPartnerBankAccount());
            btr.setFromAccount(da);
            btr.setSettled(false);
            btr.setShortCode(billingOrg.getOrganization().getShortCode());
            btr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
            btr.setActionType(EnumUtils.TransactionType.CCSPENDING);
            webserviceBean.billingClearingSACH(btr);
            da.removeBalance(amount);
            depositBean.updateAccount(da);
        } catch (DepositAccountNotFoundException | UpdateDepositAccountException e) {
            System.out.println("DepositAccountNotFoundException | UpdateDepositAccountException PayOtherCreditCardBillCounterManagedBean transfer()");
            MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
        }
    }

    /**
     * @return the selectedBillId
     */
    public String getSelectedBillId() {
        return selectedBillId;
    }

    /**
     * @param selectedBillId the selectedBillId to set
     */
    public void setSelectedBillId(String selectedBillId) {
        this.selectedBillId = selectedBillId;
    }

    /**
     * @return the fromAccountNumber
     */
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    /**
     * @param fromAccountNumber the fromAccountNumber to set
     */
    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    /**
     * @return the toReferenceNumber
     */
    public String getToReferenceNumber() {
        return toReferenceNumber;
    }

    /**
     * @param toReferenceNumber the toReferenceNumber to set
     */
    public void setToReferenceNumber(String toReferenceNumber) {
        this.toReferenceNumber = toReferenceNumber;
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
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

    /**
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    /**
     * @return the billingOrg
     */
    public BillingOrg getBillingOrg() {
        return billingOrg;
    }

    /**
     * @param billingOrg the billingOrg to set
     */
    public void setBillingOrg(BillingOrg billingOrg) {
        this.billingOrg = billingOrg;
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
     * @return the addedBillOrgs
     */
    public List<BillingOrg> getAddedBillOrgs() {
        return addedBillOrgs;
    }

    /**
     * @param addedBillOrgs the addedBillOrgs to set
     */
    public void setAddedBillOrgs(List<BillingOrg> addedBillOrgs) {
        this.addedBillOrgs = addedBillOrgs;
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
     * @return the selectedBillOrgId
     */
    public Long getSelectedBillOrgId() {
        return selectedBillOrgId;
    }

    /**
     * @param selectedBillOrgId the selectedBillOrgId to set
     */
    public void setSelectedBillOrgId(Long selectedBillOrgId) {
        this.selectedBillOrgId = selectedBillOrgId;
    }

}
