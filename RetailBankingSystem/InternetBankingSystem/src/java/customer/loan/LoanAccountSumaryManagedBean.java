/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.common.LoginSessionBeanLocal;
import entity.customer.MainAccount;
import entity.loan.LoanAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "loanAccountSumaryManagedBean")
@ViewScoped
public class LoanAccountSumaryManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    
    private List<LoanAccount> loanAccounts = new ArrayList<>();
    
    /**
     * Creates a new instance of LoanAccountSumaryManagedBean
     */
    public LoanAccountSumaryManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        MainAccount ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        setLoanAccounts(ma.getLoanAccounts());
    }
    
    public void viewBreakdowns(LoanAccount la) {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", la.getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("loan_account_breakdown.xhtml" + params);
    }
    
    public void viewHistory(LoanAccount la) {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", la.getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("loan_account_history.xhtml" + params);
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
