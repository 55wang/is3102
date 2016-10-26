/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.loan.LoanAccount;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.RedirectUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "customerLoanAccountDetailManagedBean")
@ViewScoped
public class CustomerLoanAccountDetailManagedBean implements Serializable {

    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    private String accountId;
    private LoanAccount loanAccount;
    
    /**
     * Creates a new instance of CustomerLoanAccountDetailManagedBean
     */
    public CustomerLoanAccountDetailManagedBean() {
    }
    
    public void findLoanAccount() {
        loanAccount = loanAccountBean.getLoanAccountByAccountNumber(accountId);
    }
    
    public void viewBreakdowns() {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getLoanAccount().getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("customer_loan_repayment_breakdown.xhtml" + params);
    }
    
    public void viewHistory() {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getLoanAccount().getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("customer_loan_payment_history.xhtml" + params);
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
