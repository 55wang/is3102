/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.loan.LoanAccount;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "customerLoanHistoryManagedBean")
@ViewScoped
public class CustomerLoanHistoryManagedBean implements Serializable {

    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    private String accountId;
    private LoanAccount loanAccount;
    
    /**
     * Creates a new instance of CustomerLoanHistoryManagedBean
     */
    public CustomerLoanHistoryManagedBean() {
    }
    
    public void init() {
        System.out.println("Account id is: " + getAccountId());
        setLoanAccount(loanAccountBean.getLoanAccountByAccountNumber(getAccountId()));
        System.out.println("Account retrieved is: " + getLoanAccount().getAccountNumber());
    }

    /**
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the loanAccount
     */
    public LoanAccount getLoanAccount() {
        return loanAccount;
    }

    /**
     * @param loanAccount the loanAccount to set
     */
    public void setLoanAccount(LoanAccount loanAccount) {
        this.loanAccount = loanAccount;
    }
    
}
