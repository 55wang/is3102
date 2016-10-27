/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanCalculationSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.loan.LoanApplication;
import entity.loan.LoanProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import utils.JSUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "personalLoanApplicationManagedBean")
@ViewScoped
public class PersonalLoanApplicationManagedBean implements Serializable {
    
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private LoanCalculationSessionBeanLocal calculator;
    @EJB
    private LoanProductSessionBeanLocal loanProductBean;
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;
    
    private Long applicationNumber;
    
    private Integer age;
    private Double monthlyIncome;
    private String idNumber;
    private String name;
    private String phoneNumber;
    private String email;
    private Double otherLoan = 0.0;
    private Double loanAmount;
    private Long loanProductId;
    private List<LoanProduct> personalLoanProducts = new ArrayList<>();
    
    private Double personalLoanAnnualInterestRate;//TODO: Make this dynamic
    private Integer personalTenure;
    private Long personalTenureProductId;
    private Double personalLoanAmt = 0.0;
    private Double personalLoanMonthlyInstalment;
    /**
     * Creates a new instance of PersonalLoanApplicationManagedBean
     */
    public PersonalLoanApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        personalLoanProducts = loanProductBean.getAllPersonalLoanProduct();
        if (personalLoanProducts.size() > 0 && personalLoanProducts.get(0).getLoanInterestCollection().getLoanInterests().size() > 0) {
            personalLoanAnnualInterestRate = personalLoanProducts.get(0).getLoanInterestCollection().getLoanInterests().get(0).getInterestRate();
            personalTenure = personalLoanProducts.get(0).getTenure();
        }
        
    }
    
    public void checkAge(){
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
        } else {
            JSUtils.callJSMethod("PF('myWizard').next();");
        }  
    }
    
    public void calculatePersonal(){

        if (getMonthlyIncome() < 2000) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_2000);
            return;
        }
        
        personalLoanAmt = calculator.calculateMaxPersonalLoanAmt(monthlyIncome, otherLoan);
        personalLoanMonthlyInstalment = calculator.calculatePersonalMonthlyInstalment(getPersonalLoanAnnualInterestRate(), getPersonalTenure(), personalLoanAmt);
        
        JSUtils.callJSMethod("PF('myWizard').next()");
    }
    
    public void applyPersonalLoan() {
        if (loanAmount > personalLoanAmt) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_LOAN_LIMIT);
            return;
        }
        
        LoanApplication newApplication = new LoanApplication();
        newApplication.setAge(age);
        newApplication.setIdNumber(idNumber);
        newApplication.setEmail(email);
        newApplication.setIncome(monthlyIncome);
        newApplication.setName(name);
        newApplication.setOtherCommitment(otherLoan);
        newApplication.setPhone(phoneNumber);
        newApplication.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        newApplication.setRequestedAmount(loanAmount);
        newApplication.setLoanProduct(loanProductBean.getLoanProductById(loanProductId));
        newApplication.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        // ejb save and update
        LoanApplication result = loanAccountBean.createLoanApplication(newApplication);
        if (result != null) {
            setApplicationNumber(result.getId());
            emailServiceSessionBean.sendLoanApplicationNoticeToStaff(result);
            emailServiceSessionBean.sendLoanApplicationNoticeToCustomer(result.getEmail());
            JSUtils.callJSMethod("PF('myWizard').next()");
        }
        
    }
    
    public void handleSelectTenure() {
        LoanProduct lp = loanProductBean.getLoanProductById(getPersonalTenureProductId());
        if (lp.getLoanInterestCollection().getLoanInterests().size() > 0) {
            personalLoanAnnualInterestRate = lp.getLoanInterestCollection().getLoanInterests().get(0).getInterestRate();
        }
        setPersonalTenure(lp.getTenure());
        loanProductId = getPersonalTenureProductId();
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the monthlyIncome
     */
    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * @param monthlyIncome the monthlyIncome to set
     */
    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    /**
     * @return the idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber the idNumber to set
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the otherLoan
     */
    public Double getOtherLoan() {
        return otherLoan;
    }

    /**
     * @param otherLoan the otherLoan to set
     */
    public void setOtherLoan(Double otherLoan) {
        this.otherLoan = otherLoan;
    }

    /**
     * @return the loanAmount
     */
    public Double getLoanAmount() {
        return loanAmount;
    }

    /**
     * @param loanAmount the loanAmount to set
     */
    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    /**
     * @return the loanProductId
     */
    public Long getLoanProductId() {
        return loanProductId;
    }

    /**
     * @param loanProductId the loanProductId to set
     */
    public void setLoanProductId(Long loanProductId) {
        this.loanProductId = loanProductId;
    }

    /**
     * @return the personalLoanProducts
     */
    public List<LoanProduct> getPersonalLoanProducts() {
        return personalLoanProducts;
    }

    /**
     * @param personalLoanProducts the personalLoanProducts to set
     */
    public void setPersonalLoanProducts(List<LoanProduct> personalLoanProducts) {
        this.personalLoanProducts = personalLoanProducts;
    }

    /**
     * @return the personalLoanAmt
     */
    public Double getPersonalLoanAmt() {
        return personalLoanAmt;
    }

    /**
     * @param personalLoanAmt the personalLoanAmt to set
     */
    public void setPersonalLoanAmt(Double personalLoanAmt) {
        this.personalLoanAmt = personalLoanAmt;
    }

    /**
     * @return the personalLoanMonthlyInstalment
     */
    public Double getPersonalLoanMonthlyInstalment() {
        return personalLoanMonthlyInstalment;
    }

    /**
     * @param personalLoanMonthlyInstalment the personalLoanMonthlyInstalment to set
     */
    public void setPersonalLoanMonthlyInstalment(Double personalLoanMonthlyInstalment) {
        this.personalLoanMonthlyInstalment = personalLoanMonthlyInstalment;
    }

    /**
     * @return the personalLoanAnnualInterestRate
     */
    public Double getPersonalLoanAnnualInterestRate() {
        return personalLoanAnnualInterestRate;
    }

    /**
     * @return the personalTenureProductId
     */
    public Long getPersonalTenureProductId() {
        return personalTenureProductId;
    }

    /**
     * @param personalTenureProductId the personalTenureProductId to set
     */
    public void setPersonalTenureProductId(Long personalTenureProductId) {
        this.personalTenureProductId = personalTenureProductId;
    }

    /**
     * @return the personalTenure
     */
    public Integer getPersonalTenure() {
        return personalTenure;
    }

    /**
     * @param personalTenure the personalTenure to set
     */
    public void setPersonalTenure(Integer personalTenure) {
        this.personalTenure = personalTenure;
    }

    /**
     * @return the applicationNumber
     */
    public Long getApplicationNumber() {
        return applicationNumber;
    }

    /**
     * @param applicationNumber the applicationNumber to set
     */
    public void setApplicationNumber(Long applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
}
