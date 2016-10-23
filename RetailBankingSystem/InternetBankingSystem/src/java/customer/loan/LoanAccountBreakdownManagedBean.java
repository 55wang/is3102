/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

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
@Named(value = "loanAccountBreakdownManagedBean")
@ViewScoped
public class LoanAccountBreakdownManagedBean implements Serializable {
    
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    private String accountId;
    private LoanAccount loanAccount;
    /**
     * Creates a new instance of LoanAccountBreakdownManagedBean
     */
    public LoanAccountBreakdownManagedBean() {
    }
    
    public void init() {
        System.out.println("Account id is: " + getAccountId());
        setLoanAccount(loanAccountBean.getLoanAccountByAccountNumber(getAccountId()));
        System.out.println("Account retrieved is: " + loanAccount.getAccountNumber());
        System.out.println("Breakdown size: " + loanAccount.getLoanPaymentBreakdown().size());
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
