/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.bill.TransactionSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import entity.bill.Payee;
import entity.common.TransferRecord;
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
    private TransactionSessionBeanLocal transactionBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    private String fromAccountNo;
    private String payeeId;
    private String transferLimitLeft;
    private BigDecimal amount;
    private List<DepositAccount> accounts = new ArrayList<>();
    private List<Payee> payees = new ArrayList<>();
    
    public intraBankTransferManagedBean() {}
    
    @PostConstruct
    public void init() {
        MainAccount ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        payees = transferBean.getPayeeFromUserIdWithType(ma.getId(), EnumUtils.PayeeType.MERLION);
        setAccounts(ma.getBankAcounts());
        
        BigDecimal todayTransferAmount = transferBean.getTodayBankTransferAmount(ma, EnumUtils.PayeeType.MERLION);
        BigDecimal currentTransferLimit = new BigDecimal(ma.getTransferLimits().getDailyInterBankLimit().toString());
        transferLimitLeft = currentTransferLimit.subtract(todayTransferAmount).setScale(2).toString();
    }
    
    public void transfer() {
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
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNo);
        tr.setFromAccount(fromAccount);
        DepositAccount toAccount = depositBean.getAccountFromId(payee.getAccountNumber());
        tr.setToAccount(toAccount);
        tr.setType(EnumUtils.PayeeType.MERLION);
        transactionBean.createTransferRecord(tr);
        
        String result = transferBean.transferFromAccountToAccount(getFromAccountNo(), payee.getAccountNumber(), getAmount());
        if (result.equals("SUCCESS")) {
            init();
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
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
}
