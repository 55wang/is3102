/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bill;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import util.exception.dams.DepositAccountNotFoundException;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "payOwnBankCreditCardBillCounterManagedBean")
@ViewScoped
public class PayOwnBankCreditCardBillCounterManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;

    private String fromAccountNo;
    private String toCreditCardNo;
    private BigDecimal amount;
    private String customerIC;
    private MainAccount mainAccount;
    private List<CustomerDepositAccount> depositAccounts = new ArrayList<>();
    private List<CreditCardAccount> creditCardAccounts = new ArrayList<>();

    /**
     * Creates a new instance of PayOwnBankCreditCardBillCounterManagedBean
     */
    public PayOwnBankCreditCardBillCounterManagedBean() {
    }

    public void retrieveMainAccount() {

        try {
            mainAccount = loginBean.getMainAccountByUserIC(getCustomerIC());
            depositAccounts = depositBean.getAllNonFixedCustomerAccounts(mainAccount.getId());
            setCreditCardAccounts(mainAccount.getCreditCardAccounts());
        } catch (Exception e) {
            mainAccount = null;
            MessageUtils.displayError("Customer Main Account Not Found!");
        }

    }

    public void transfer() {

        try {

            DepositAccount fromAccount = depositBean.getAccountFromId(getFromAccountNo());
            if (fromAccount != null && fromAccount.getBalance().compareTo(getAmount()) < 0) {
                MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
            }
            //TODO: need another authentication
            String result = transferBean.transferFromAccountToCreditCard(getFromAccountNo(), getToCreditCardNo(), getAmount());
            if (result.equals("SUCCESS")) {
                MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            } else {
                MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
            }

        } catch (DepositAccountNotFoundException e) {
            System.out.println("DepositAccountNotFoundException PayOwnBankCreditCardBillCounterManagedBean transfer()");
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
