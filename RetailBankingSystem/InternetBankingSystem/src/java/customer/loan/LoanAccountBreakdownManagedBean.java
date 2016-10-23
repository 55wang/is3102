/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

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
@Named(value = "loanAccountBreakdownManagedBean")
@ViewScoped
public class LoanAccountBreakdownManagedBean implements Serializable {
    
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    private String accountId;
    private List<LoanPaymentBreakdown> breakdowns = new ArrayList<>();
    /**
     * Creates a new instance of LoanAccountBreakdownManagedBean
     */
    public LoanAccountBreakdownManagedBean() {
    }
    
    public void init() {
        System.out.println("Account id is: " + getAccountId());
        
        breakdowns = loanAccountBean.getFuturePaymentBreakdownsByLoanAcountNumber(getAccountId());
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
