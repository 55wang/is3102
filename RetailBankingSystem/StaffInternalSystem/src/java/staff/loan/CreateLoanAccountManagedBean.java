/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createLoanAccountManagedBean")
@ViewScoped
public class CreateLoanAccountManagedBean implements Serializable {
    
    @EJB
    private LoanProductSessionBeanLocal loanProductBean;
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;
    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;

    private String mainAccountId;
    private Long selectedLoanProductId;
    private Date paymentStartDate;
    private Double principalAmount;
    
    private List<LoanProduct> loanProducts = new ArrayList<>();
    /**
     * Creates a new instance of CreateLoanAccountManagedBean
     */
    public CreateLoanAccountManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        setLoanProducts(loanProductBean.getAllLoanProduct());
    }
    
    public void creatAccount() {
        MainAccount ma = loginBean.getMainAccountByUserID(mainAccountId);
        CustomerDepositAccount cda = depositBean.getDaytoDayAccountByMainAccount(ma);
        LoanAccount la = new LoanAccount();
        // mapping
        la.setMainAccount(ma);
        la.setDepositAccount(cda);
        la.setLoanOfficer(SessionUtils.getStaff());
        la.setLoanProduct(loanProductBean.getLoanProductById(selectedLoanProductId));
        // info
        la.setPaymentStartDate(getPaymentStartDate());
        la.setPaymentDate(DateUtils.getDayNumber(getPaymentStartDate()));
        la.setMaturityDate(DateUtils.addYearsToDate(getPaymentStartDate(), la.getLoanProduct().getTenure()));
        la.setTenure(la.getLoanProduct().getTenure());
        la.setPrincipal(getPrincipalAmount());
        la.setLoanAccountStatus(EnumUtils.LoanAccountStatus.PENDING);
        
        LoanAccount result = loanAccountBean.createLoanAccount(la);
        if (result != null) {
            MessageUtils.displayInfo("Loan Account Created!");
        } else {
            MessageUtils.displayError("Loan Account Not Created!");
        }
    }

    /**
     * @return the mainAccountId
     */
    public String getMainAccountId() {
        return mainAccountId;
    }

    /**
     * @param mainAccountId the mainAccountId to set
     */
    public void setMainAccountId(String mainAccountId) {
        this.mainAccountId = mainAccountId;
    }

    /**
     * @return the loanProducts
     */
    public List<LoanProduct> getLoanProducts() {
        return loanProducts;
    }

    /**
     * @param loanProducts the loanProducts to set
     */
    public void setLoanProducts(List<LoanProduct> loanProducts) {
        this.loanProducts = loanProducts;
    }

    /**
     * @return the selectedLoanProductId
     */
    public Long getSelectedLoanProductId() {
        return selectedLoanProductId;
    }

    /**
     * @param selectedLoanProductId the selectedLoanProductId to set
     */
    public void setSelectedLoanProductId(Long selectedLoanProductId) {
        this.selectedLoanProductId = selectedLoanProductId;
    }

    /**
     * @return the paymentStartDate
     */
    public Date getPaymentStartDate() {
        return paymentStartDate;
    }

    /**
     * @param paymentStartDate the paymentStartDate to set
     */
    public void setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    /**
     * @return the principalAmount
     */
    public Double getPrincipalAmount() {
        return principalAmount;
    }

    /**
     * @param principalAmount the principalAmount to set
     */
    public void setPrincipalAmount(Double principalAmount) {
        this.principalAmount = principalAmount;
    }

}
