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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.LoanAccountStatus;
import utils.RedirectUtils;
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

    private final String STATUS_NEW = LoanAccountStatus.NEW.toString();
    private final String STATUS_INPROGRESS = LoanAccountStatus.INPROGRESS.toString();
    private List<LoanApplication> myLoanApplications = new ArrayList<>();

    /**
     * Creates a new instance of ViewLoanApplicationManagedBean
     */
    public ViewLoanApplicationManagedBean() {
    }

    @PostConstruct
    public void init() {
        myLoanApplications = loanAccountBean.getLoanApplicationByStaffUsername(SessionUtils.getStaffUsername(), EnumUtils.LoanAccountStatus.NEW);
    }

    public void startProcess(LoanApplication la) {
        la.setStatus(EnumUtils.LoanAccountStatus.INPROGRESS);
        loanAccountBean.updateLoanApplication(la);
    }

    public void creatLoanAccount(LoanApplication la) {
        Map<String, String> map = new HashMap<>();
        map.put("applicationId", la.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("create_loan_account.xhtml" + params);
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

    /**
     * @return the STATUS_NEW
     */
    public String getSTATUS_NEW() {
        return STATUS_NEW;
    }

    /**
     * @return the STATUS_INPROGRESS
     */
    public String getSTATUS_INPROGRESS() {
        return STATUS_INPROGRESS;
    }
}
