/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

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
@Named(value = "loanAccountDetailsManagedBean")
@ViewScoped
public class LoanAccountDetailsManagedBean implements Serializable {
    
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    private String accountId;
    private LoanAccount loanAccount;
    
    /**
     * Creates a new instance of LoanAccountDetailsManagedBean
     */
    public LoanAccountDetailsManagedBean() {
    }
    
    public void init() {
        System.out.println("Account id is: " + getAccountId());
        setLoanAccount(loanAccountBean.getLoanAccountByAccountNumber(getAccountId()));
        System.out.println("Account retrieved is: " + getLoanAccount().getAccountNumber());
    }
    
    public void viewBreakdowns() {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", loanAccount.getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("loan_account_breakdown.xhtml" + params);
    }
    
    public void viewHistory() {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", loanAccount.getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("loan_account_history.xhtml" + params);
    }
    
    public void lumSumPayment() {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", loanAccount.getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("loan_lumsum_payment.xhtml" + params);
    }
    
    public void requestTenureChange() {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", loanAccount.getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("loan_request_tenure_change.xhtml" + params);
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
