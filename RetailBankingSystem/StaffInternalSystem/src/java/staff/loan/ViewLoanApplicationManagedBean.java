/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.loan.LoanApplication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "viewLoanApplicationManagedBean")
@ViewScoped
public class ViewLoanApplicationManagedBean implements Serializable {

    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean; 
    
    private List<LoanApplication> myLoanApplications = new ArrayList<>();
    /**
     * Creates a new instance of ViewLoanApplicationManagedBean
     */
    public ViewLoanApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        myLoanApplications = loanAccountBean.getLoanApplicationByStaffUsername(SessionUtils.getStaffUsername());
    }

    /**
     * @return the myLoanApplications
     */
    public List<LoanApplication> getMyLoanApplications() {
        return myLoanApplications;
    }

    /**
     * @param myLoanApplications the myLoanApplications to set
     */
    public void setMyLoanApplications(List<LoanApplication> myLoanApplications) {
        this.myLoanApplications = myLoanApplications;
    }
}
