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
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.loan.LoanApplication;
import entity.loan.LoanProduct;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    private String firstName;
    private String lastName;
    private String name;
    private String phoneNumber;
    private String email;
    private Double otherLoan = 0.0;
    private Double loanAmount;
    private EnumUtils.Nationality nationality;
    private EnumUtils.MaritalStatus maritalStatus;
    private String address;
    private String postalCode;
    private Date birthday;
    private EnumUtils.Industry industry;
    private EnumUtils.Education education;
    private EnumUtils.Gender gender;
    private EnumUtils.EmploymentStatus employmentStatus;
    private Long loanProductId;
    private List<LoanProduct> personalLoanProducts = new ArrayList<>();
    
    private Double personalLoanAnnualInterestRate;//TODO: Make this dynamic
    private Integer personalTenure;
    private Long personalTenureProductId;
    private Double personalLoanAmt = 0.0;
    private Double personalLoanMonthlyInstalment;
    private String upperLimit;
    private EnumUtils.LoanProductType productType;
    private EnumUtils.IdentityType identityType;
    private Date currentDate=new Date();
    
    
    private List<String> identityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> nationalityOptions = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> genderOptions = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> occupationOptions = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> incomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);
    LoanApplication existingApplication = new LoanApplication();
    
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
    
     public void checkAge2(){
        Long ageLong=(new Date().getTime() - this.getBirthday().getTime()) / (24 * 60 * 60 * 1000) / 365;
        Integer age1=ageLong.intValue();
        this.setAge(age1);
        checkAge();
}
    
    public void checkAge(){
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
        } else {
            JSUtils.callJSMethod("PF('myWizard').next();");
        }  
    }
     
     public void changeLoanUpperLimit(ValueChangeEvent e){
        if((Double)e.getNewValue()>=10000.0)
            setUpperLimit("10x monthly income");
        else if((Double)e.getNewValue()<10000.0 && (Double)e.getNewValue()>=2000.0)
            setUpperLimit("4x monthly income");
    }
     
    public void updateUpperLimit(Customer customer){
        if(customer.getActualIncome()>=10000.0)
            setUpperLimit("10x monthly income");
        else if (customer.getActualIncome()<10000.0 && customer.getActualIncome()>=2000.0)
            setUpperLimit("4x monthly income");
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
        
        LoanApplication loanApplication1 = new LoanApplication();
        loanApplication1.setIdentityType(EnumUtils.IdentityType.NRIC);
        try {
            loanApplication1.setBirthDay(birthday);
        } catch (Exception ex) {
        }
        loanApplication1.setAge(age);
        loanApplication1.setActualIncome(monthlyIncome);
        loanApplication1.setFirstname(firstName);
        loanApplication1.setLastname(lastName);
        loanApplication1.setFullName(loanApplication1.getLastname()+loanApplication1.getFirstname());
        loanApplication1.setEmail(email);
        loanApplication1.setPhone(phoneNumber);
        
        loanApplication1.setNationality(nationality);
        loanApplication1.setMaritalStatus(maritalStatus);
        loanApplication1.setAddress(address);
        loanApplication1.setPostalCode(postalCode);
        loanApplication1.setIndustry(industry);
        loanApplication1.setEducation(education);
        loanApplication1.setEmploymentStatus(employmentStatus);
        loanApplication1.setGender(gender);
        loanApplication1.setCategory("New");
        
        loanApplication1.setIdentityNumber(idNumber);
        loanApplication1.setIdentityType(identityType);
        loanApplication1.setOtherCommitment(otherLoan);
        loanApplication1.setRequestedAmount(personalLoanAmt);
        loanApplication1.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        loanApplication1.setLoanProduct(loanProductBean.getLoanProductById(loanProductId));
        loanApplication1.setTenure(loanApplication1.getLoanProduct().getTenure());
        loanApplication1.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        // ejb save and update
        LoanApplication result = loanAccountBean.createLoanApplication(loanApplication1);
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
    
    public void save(Customer thisCustomer){
        
        existingApplication.setAge(thisCustomer.getAge());
        existingApplication.setIdentityNumber(thisCustomer.getIdentityNumber());
        existingApplication.setEmail(thisCustomer.getEmail());
        existingApplication.setPhone(thisCustomer.getPhone());
        existingApplication.setFullName(thisCustomer.getLastname()+thisCustomer.getFirstname());
        existingApplication.setIdentityNumber(thisCustomer.getIdentityNumber());
        existingApplication.setActualIncome(thisCustomer.getActualIncome());
        this.setMonthlyIncome(thisCustomer.getActualIncome());
        updateUpperLimit(thisCustomer);
        JSUtils.callJSMethod("PF('myWizard').next()");
    }
    
    public void existingApplyPersonalLoan(){
        existingApplication.setOtherCommitment(otherLoan);
        existingApplication.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL);
        existingApplication.setRequestedAmount(loanAmount);
        existingApplication.setLoanProduct(loanProductBean.getLoanProductById(loanProductId));
        existingApplication.setTenure(existingApplication.getLoanProduct().getTenure());
        existingApplication.setLoanOfficer(staffAccountSessionBean.getAccountByUsername(ConstantUtils.LOAN_OFFICIER_USERNAME));
        existingApplication.setCategory("New");
        // ejb save and update
        LoanApplication result = loanAccountBean.createLoanApplication(existingApplication);
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

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public LoanApplication getExistingApplication() {
        return existingApplication;
    }

    public void setExistingApplication(LoanApplication existingApplication) {
        this.existingApplication = existingApplication;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EnumUtils.Nationality getNationality() {
        return nationality;
    }

    public void setNationality(EnumUtils.Nationality nationality) {
        this.nationality = nationality;
    }

    public EnumUtils.MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(EnumUtils.MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public EnumUtils.Industry getIndustry() {
        return industry;
    }

    public void setIndustry(EnumUtils.Industry industry) {
        this.industry = industry;
    }

    public EnumUtils.Education getEducation() {
        return education;
    }

    public void setEducation(EnumUtils.Education education) {
        this.education = education;
    }

    public EnumUtils.Gender getGender() {
        return gender;
    }

    public void setGender(EnumUtils.Gender gender) {
        this.gender = gender;
    }

    public EnumUtils.EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EnumUtils.EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public EnumUtils.LoanProductType getProductType() {
        return productType;
    }

    public void setProductType(EnumUtils.LoanProductType productType) {
        this.productType = productType;
    }

    public EnumUtils.IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(EnumUtils.IdentityType identityType) {
        this.identityType = identityType;
    }

    public List<String> getIdentityTypeOptions() {
        return identityTypeOptions;
    }

    public void setIdentityTypeOptions(List<String> identityTypeOptions) {
        this.identityTypeOptions = identityTypeOptions;
    }

    public List<String> getNationalityOptions() {
        return nationalityOptions;
    }

    public void setNationalityOptions(List<String> nationalityOptions) {
        this.nationalityOptions = nationalityOptions;
    }

    public List<String> getGenderOptions() {
        return genderOptions;
    }

    public void setGenderOptions(List<String> genderOptions) {
        this.genderOptions = genderOptions;
    }

    public List<String> getOccupationOptions() {
        return occupationOptions;
    }

    public void setOccupationOptions(List<String> occupationOptions) {
        this.occupationOptions = occupationOptions;
    }

    public List<String> getIncomeOptions() {
        return incomeOptions;
    }

    public void setIncomeOptions(List<String> incomeOptions) {
        this.incomeOptions = incomeOptions;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }


}
