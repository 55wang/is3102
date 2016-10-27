/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.loan.LoanAccount;
import entity.loan.LoanAdjustmentApplication;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import utils.JSUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "loanAdjustmentRequestManagedBean")
@ViewScoped
public class LoanAdjustmentRequestManagedBean implements Serializable {

    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    private String accountId;
    private LoanAccount loanAccount;
    private LoanAdjustmentApplication adjustmentRequest;

    /**
     * Creates a new instance of LoanAdjustmentRequestManagedBean
     */
    public LoanAdjustmentRequestManagedBean() {
    }

    public void init() {
        System.out.println("Account id is: " + getAccountId());
        setLoanAccount(loanAccountBean.getLoanAccountByAccountNumber(getAccountId()));
        System.out.println("Account retrieved is: " + getLoanAccount().getAccountNumber());
        adjustmentRequest = new LoanAdjustmentApplication();
        adjustmentRequest.setLoanAccount(loanAccount);
    }

    public void requestTenureChange() {
        LoanAdjustmentApplication result = loanAccountBean.createLoanAdjustmentApplication(adjustmentRequest);

        if (result != null) {
            adjustmentRequest = new LoanAdjustmentApplication();
            adjustmentRequest.setLoanAccount(loanAccount);
            MessageUtils.displayInfo("Adjustment Submited");
        }
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
     * @return the adjustmentRequest
     */
    public LoanAdjustmentApplication getAdjustmentRequest() {
        return adjustmentRequest;
    }

    /**
     * @param adjustmentRequest the adjustmentRequest to set
     */
    public void setAdjustmentRequest(LoanAdjustmentApplication adjustmentRequest) {
        this.adjustmentRequest = adjustmentRequest;
    }

}
