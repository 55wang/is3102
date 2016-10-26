/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.loan.LoanAccount;
import entity.loan.LoanPaymentBreakdown;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "customerLoanBreakdownManagedBean")
@ViewScoped
public class CustomerLoanBreakdownManagedBean implements Serializable {

    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    private String accountId;
    private LoanAccount loanAccount;
    private List<LoanPaymentBreakdown> breakdowns = new ArrayList<>();
    
    /**
     * Creates a new instance of CustomerLoanBreakdownManagedBean
     */
    public CustomerLoanBreakdownManagedBean() {
    }
    
    public void init() {
        System.out.println("Account id is: " + getAccountId());
        breakdowns = loanAccountBean.getFuturePaymentBreakdownsByLoanAcountNumber(getAccountId());
        setLoanAccount(loanAccountBean.getLoanAccountByAccountNumber(getAccountId()));
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

    /**
     * @return the breakdowns
     */
    public List<LoanPaymentBreakdown> getBreakdowns() {
        return breakdowns;
    }

    /**
     * @param breakdowns the breakdowns to set
     */
    public void setBreakdowns(List<LoanPaymentBreakdown> breakdowns) {
        this.breakdowns = breakdowns;
    }
    
}
