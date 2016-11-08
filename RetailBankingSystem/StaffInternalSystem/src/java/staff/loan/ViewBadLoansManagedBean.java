/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author litong
 */
@Named(value = "viewBadLoansManagedBean")
@ViewScoped
public class ViewBadLoansManagedBean implements Serializable{
    
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    
    private List<LoanAccount> badLoans = new ArrayList<>();
    /**
     * Creates a new instance of ViewBadLoansManagedBean
     */
    public ViewBadLoansManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        badLoans = loanAccountBean.getLoanAccountByStaffUsernameAndStatus(SessionUtils.getStaffUsername(), EnumUtils.LoanAccountStatus.SUSPENDED);
    }

    public void closeBadLoanAccount(LoanAccount la){
        la.setLoanAccountStatus(EnumUtils.LoanAccountStatus.CLOSED);
        loanAccountBean.updateLoanAccount(la);
        RedirectUtils.redirect("bad_loans.xhtml");
    }

    public List<LoanAccount> getBadLoans() {
        return badLoans;
    }

    public void setBadLoans(List<LoanAccount> badLoans) {
        this.badLoans = badLoans;
    }
    
    
    
}
