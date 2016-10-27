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
@Named(value = "ppLoanApplicationManagedBean")
@ViewScoped
public class PpLoanApplicationManagedBean implements Serializable {
    
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
    private Integer housingTenure;
    private Long loanProductId;
    private List<LoanProduct> ppLoanProducts = new ArrayList<>();
    
    private Integer maxHousingTenure;
    private Double maxLoanAmount;
    private Integer numOfHousingLoan = 0;
    private Double monthlyInstalment;
    private Double maxMarketValue;
    private Double marketValue;
    
    /**
     * Creates a new instance of HdbLoanApplicationManagedBean
     */
    public PpLoanApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        setPpLoanProducts(loanProductBean.getAllPPLoanProduct());
    }
    
    public void checkAge(){
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);           
        } else {
            JSUtils.callJSMethod("PF('myWizard').next();");
        }
        
    }
    public void calculatePP(){
        
        if (getMonthlyIncome() < 1500) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_1500);
            return;
        }
        setMaxHousingTenure(calculator.calculateMaxPPTenure(getAge(), getMonthlyIncome()));
        setMonthlyInstalment(calculator.calculateMaxPPMonthlyInstalment(getMonthlyIncome(), getOtherLoan()));
        setMaxLoanAmount(calculator.calculateMaxHomeLoanAmt(getMonthlyInstalment(), getMaxHousingTenure()));
        setMaxMarketValue(calculator.homeMarketValue(getMaxLoanAmount(), getNumOfHousingLoan()));
        JSUtils.callJSMethod("PF('myWizard').next()");
    }
    
    public void applyPPLoan() {
        if (loanAmount > maxLoanAmount) {
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
        newApplication.setOtherHousingLoan(numOfHousingLoan);
        newApplication.setPhone(phoneNumber);
        newApplication.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PRIVATE_HOUSE);
        newApplication.setRequestedAmount(loanAmount);
        newApplication.setMarketValue(marketValue);
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
     * @return the numOfHousingLoan
     */
    public Integer getNumOfHousingLoan() {
        return numOfHousingLoan;
    }

    /**
     * @param numOfHousingLoan the numOfHousingLoan to set
     */
    public void setNumOfHousingLoan(Integer numOfHousingLoan) {
        this.numOfHousingLoan = numOfHousingLoan;
    }

    /**
     * @return the monthlyInstalment
     */
    public Double getMonthlyInstalment() {
        return monthlyInstalment;
    }

    /**
     * @param monthlyInstalment the monthlyInstalment to set
     */
    public void setMonthlyInstalment(Double monthlyInstalment) {
        this.monthlyInstalment = monthlyInstalment;
    }

    /**
     * @return the marketValue
     */
    public Double getMarketValue() {
        return marketValue;
    }

    /**
     * @param marketValue the marketValue to set
     */
    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
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

    /**
     * @return the maxHousingTenure
     */
    public Integer getMaxHousingTenure() {
        return maxHousingTenure;
    }

    /**
     * @param maxHousingTenure the maxHousingTenure to set
     */
    public void setMaxHousingTenure(Integer maxHousingTenure) {
        this.maxHousingTenure = maxHousingTenure;
    }

    /**
     * @return the maxLoanAmount
     */
    public Double getMaxLoanAmount() {
        return maxLoanAmount;
    }

    /**
     * @param maxLoanAmount the maxLoanAmount to set
     */
    public void setMaxLoanAmount(Double maxLoanAmount) {
        this.maxLoanAmount = maxLoanAmount;
    }

    /**
     * @return the housingTenure
     */
    public Integer getHousingTenure() {
        return housingTenure;
    }

    /**
     * @param housingTenure the housingTenure to set
     */
    public void setHousingTenure(Integer housingTenure) {
        this.housingTenure = housingTenure;
    }

    /**
     * @return the maxMarketValue
     */
    public Double getMaxMarketValue() {
        return maxMarketValue;
    }

    /**
     * @param maxMarketValue the maxMarketValue to set
     */
    public void setMaxMarketValue(Double maxMarketValue) {
        this.maxMarketValue = maxMarketValue;
    }

    /**
     * @return the ppLoanProducts
     */
    public List<LoanProduct> getPpLoanProducts() {
        return ppLoanProducts;
    }

    /**
     * @param ppLoanProducts the ppLoanProducts to set
     */
    public void setPpLoanProducts(List<LoanProduct> ppLoanProducts) {
        this.ppLoanProducts = ppLoanProducts;
    }
    
}
