/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.loan.LoanApplication;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "checkLoanApplicationManagedBean")
@ViewScoped
public class CheckLoanApplicationManagedBean implements Serializable {

    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;
    
    private Long applicationId;
    private LoanApplication loanApplication;
    /**
     * Creates a new instance of CheckLoanApplicationManagedBean
     */
    public CheckLoanApplicationManagedBean() {
    }
    
    public void retrieveLoanApplication() {
        setLoanApplication(loanAccountBean.getLoanApplicationById(applicationId));
    }

    /**
     * @return the applicationId
     */
    public Long getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @return the loanApplication
     */
    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    /**
     * @param loanApplication the loanApplication to set
     */
    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }
    
}
