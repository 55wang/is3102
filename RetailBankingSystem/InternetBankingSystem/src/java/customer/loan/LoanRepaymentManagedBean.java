/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import entity.card.account.CreditCardAccount;
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
    
    private String fromAccountNo;
    private String toLoanAccountNo;
    private BigDecimal amount;
    private List<CustomerDepositAccount> depositAccounts = new ArrayList<>();
    private List<LoanAccount> loanAccounts = new ArrayList<>();
    
    /**
     * Creates a new instance of LoanRepaymentManagedBean
     */
    public LoanRepaymentManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        MainAccount ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        depositAccounts = depositBean.getAllNonFixedCustomerAccounts(ma.getId());
        loanAccounts = loanAccountBean.getActiveLoanAccountListByMainAccountId(ma.getId());
    }
    
    public void transfer() {
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
    
}
