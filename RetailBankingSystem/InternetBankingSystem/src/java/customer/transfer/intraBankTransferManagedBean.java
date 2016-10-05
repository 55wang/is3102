/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.transfer.TransferSessionBeanLocal;
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
    
    private String fromAccountNo;
    private String toAccountNo;
    private BigDecimal amount;
    private List<DepositAccount> accounts = new ArrayList<>();
    
    public intraBankTransferManagedBean() {}
    
    @PostConstruct
    public void init() {
        MainAccount ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        setAccounts(ma.getBankAcounts());
    }
    
    public void transfer() {
        try {
            DepositAccount da = depositBean.getAccountFromId(toAccountNo);
            if (da == null) {
                MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
                return;
            }
        } catch (Exception e) {
            MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
            return;
        }
        //TODO: need another authentication
        String result = transferBean.transferFromAccountToAccount(getFromAccountNo(), getToAccountNo(), getAmount());
        if (result.equals("SUCCESS")) {
            init();
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        } else {
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
}
