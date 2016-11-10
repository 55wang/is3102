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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import utils.JSUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "hdbLoanApplicationManagedBean")
@ViewScoped
public class HdbLoanApplicationManagedBean implements Serializable {
    
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
    private String identificationNumber;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
    private Date birthday;
    private Double otherLoan = 0.0;
    private Double loanAmount;
    private Integer housingTenure;
    private Long loanProductId;
    private List<LoanProduct> hdbLoanProducts = new ArrayList<>();
    
    private Integer maxHousingTenure;
    private Double maxLoanAmount;
    private Integer numOfHousingLoan = 0;
    private Double monthlyInstalment;
    private Double maxMarketValue;
    private Double marketValue=0.0;
    private Double LTV;
    private Integer tenure;
    private EnumUtils.IdentityType identityType;
    private Date currentDate=new Date();
    
    private List<String> identityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> categoryOptions = Arrays.asList("New","Refinance");
    private String category;
    /**
     * Creates a new instance of HdbLoanApplicationManagedBean
     */
    public HdbLoanApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        hdbLoanProducts = loanProductBean.getAllHDBLoanProduct();
    }
    
    public void checkAge(){
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
        } else {
            JSUtils.callJSMethod("PF('myWizard').next();");
        }
    }
    
     public void checkAge2(){
        Long ageLong=(new Date().getTime() - this.getBirthday().getTime()) / (24 * 60 * 60 * 1000) / 365;
        Integer age1=ageLong.intValue();
        this.setAge(age1);
        checkAge();
}
    
    public void changeLTV(ValueChangeEvent e){
        if((Integer)e.getNewValue()==0)
            setLTV(0.8);
        else if((Integer)e.getNewValue()==1)
            setLTV(0.5);
        else if((Integer)e.getNewValue()>=2)
            setLTV(0.4);
   
    }
    
    public void changeMarketValue(ValueChangeEvent e){
        setMarketValue((Double)e.getNewValue()/LTV);
    }
    
    public void calculateHDB(){
   
        if (getMonthlyIncome() < 1500) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_1500);
            return;
        }
        setMaxHousingTenure(calculator.calculateMaxHDBTenure(getAge(), getMonthlyIncome()));
        setMonthlyInstalment(calculator.calculateMaxHDBMonthlyInstalment(getMonthlyIncome(), getOtherLoan()));
        setMaxLoanAmount(calculator.calculateMaxHomeLoanAmt(getMonthlyInstalment(), getMaxHousingTenure()));
        setMaxMarketValue(calculator.homeMarketValue(getMaxLoanAmount(), getNumOfHousingLoan()));
        JSUtils.callJSMethod("PF('myWizard').next()");
    }
    
    public void applyHDBLoan() {
        if (loanAmount > maxLoanAmount) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_LOAN_LIMIT);
            return;
        }
        
        if(tenure>maxHousingTenure){
            MessageUtils.displayError(ConstantUtils.EXCEED_MAX_HDB_TENURE);
            return;
        }
        
        LoanApplication newApplication = new LoanApplication();
        newApplication.setIdentityType(identityType);
        newApplication.setIdentityNumber(getIdentificationNumber());
        newApplication.setEmail(email);
        newApplication.setBirthDay(birthday);
        newApplication.setAge(age);
        newApplication.setActualIncome(monthlyIncome);
        newApplication.setLastname(lastName);
        newApplication.setFirstname(firstName);
        newApplication.setFullName(newApplication.getLastname()+" "+newApplication.getFirstname());
        newApplication.setOtherCommitment(otherLoan);
        newApplication.setOtherHousingLoan(numOfHousingLoan);
        newApplication.setPhone(phoneNumber);
        newApplication.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB);
        newApplication.setRequestedAmount(loanAmount);
        newApplication.setMarketValue(marketValue);
        newApplication.setLoanProduct(loanProductBean.getLoanProductById(loanProductId));
        newApplication.setTenure(tenure);
        newApplication.setCategory(category);
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
     * @return the hdbLoanProducts
     */
    public List<LoanProduct> getHdbLoanProducts() {
        return hdbLoanProducts;
    }

    /**
     * @param hdbLoanProducts the hdbLoanProducts to set
     */
    public void setHdbLoanProducts(List<LoanProduct> hdbLoanProducts) {
        this.hdbLoanProducts = hdbLoanProducts;
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

    public Double getLTV() {
        return LTV;
    }

    public void setLTV(Double LTV) {
        this.LTV = LTV;
    }

    public Integer getTenure() {
        return tenure;
    }

    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public EnumUtils.IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(EnumUtils.IdentityType identityType) {
        this.identityType = identityType;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public List<String> getIdentityTypeOptions() {
        return identityTypeOptions;
    }

    public void setIdentityTypeOptions(List<String> identityTypeOptions) {
        this.identityTypeOptions = identityTypeOptions;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return the identificationNumber
     */
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    /**
     * @param identificationNumber the identificationNumber to set
     */
    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public List<String> getCategoryOptions() {
        return categoryOptions;
    }

    public void setCategoryOptions(List<String> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
  
}
