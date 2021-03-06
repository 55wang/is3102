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
@Named(value = "carLoanApplicationManagedBean")
@ViewScoped
public class CarLoanApplicationManagedBean implements Serializable {
    
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
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
    private Double otherLoan = 0.0;
    private Double loanAmount;
    private Long loanProductId;
    private List<LoanProduct> carLoanProducts = new ArrayList<>();
    
    private Double carLoanAnnualInterestRate;
    private Double carLoanAmt=0.0;     
    private Double maxCarLoanAmt;
    private Double maxCarLoanMonthlyInstalment;
    private Double carLoanMonthlyInstalment=0.0;
    private Double carOpenMarketValue;
    private Integer carTenure;
    private Integer carMaxTenure;
    private Long carTenureProductId;
    private Double LTV;
    private Date birthDay;
    private EnumUtils.IdentityType identityType;
    private Date currentDate=new Date();
    
    
    private List<String> identityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);

    
    /**
     * Creates a new instance of CarLoanApplicationManagedBean
     */
    public CarLoanApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        carLoanProducts = loanProductBean.getAllCarLoanProduct();
        if (carLoanProducts.size() > 0 && carLoanProducts.get(0).getLoanInterestCollection().getLoanInterests().size() > 0) {
            carLoanAnnualInterestRate = carLoanProducts.get(0).getLoanInterestCollection().getLoanInterests().get(0).getInterestRate();
            setCarMaxTenure(carLoanProducts.get(0).getTenure());
        }
    }
    
     public void checkAge2(){
        Long ageLong=(new Date().getTime() - this.getBirthDay().getTime()) / (24 * 60 * 60 * 1000) / 365;
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
    
    public void changeLoanAmt(ValueChangeEvent e){
        setCarLoanAmt((Double)e.getNewValue());
        setCarLoanMonthlyInstalment(calculator.calculateCarMonthlyInstalment(getCarLoanAnnualInterestRate(), getCarTenure(), getCarLoanAmt()));   
        
    }
    
    public void changeLTV(ValueChangeEvent e){
        if((Double)e.getNewValue()<=20000.0)
            setLTV(0.7);
        if((Double)e.getNewValue()>20000.0)
            setLTV(0.6);
   
    }
      
    public void calculateCar(){
        if (getMonthlyIncome() < 2000) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_2000);
            return;
        }
        
        setMaxCarLoanMonthlyInstalment(calculator.calculateMaxPPMonthlyInstalment(monthlyIncome, otherLoan));   
        setMaxCarLoanAmt(carOpenMarketValue*LTV);
        
        JSUtils.callJSMethod("PF('myWizard').next()");
    }
    
    public void applyCarLoan() {
        if (loanAmount > carLoanAmt) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_LOAN_LIMIT);
            return;
        }
        
        LoanApplication newApplication = new LoanApplication();
        newApplication.setIdentityNumber(idNumber);
        newApplication.setBirthDay(birthDay);
        newApplication.setAge(age);
        newApplication.setEmail(email);
        newApplication.setActualIncome(monthlyIncome);
        newApplication.setLastname(lastName);
        newApplication.setFirstname(firstName);
        newApplication.setFullName(newApplication.getLastname()+" "+newApplication.getFirstname());
        newApplication.setIdentityType(identityType);
        newApplication.setIdentityNumber(idNumber);
        newApplication.setOtherCommitment(otherLoan);
        newApplication.setPhone(phoneNumber);
        newApplication.setProductType(EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR);
        newApplication.setRequestedAmount(loanAmount);
        newApplication.setMarketValue(carOpenMarketValue);
        newApplication.setTenure(carTenure);
        newApplication.setLoanProduct(loanProductBean.getLoanProductById(loanProductId));
        newApplication.setCategory("New");
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
        LoanProduct lp = loanProductBean.getLoanProductById(getCarTenureProductId());
        if (lp.getLoanInterestCollection().getLoanInterests().size() > 0) {
            carLoanAnnualInterestRate = lp.getLoanInterestCollection().getLoanInterests().get(0).getInterestRate();
        }
        setCarTenure(lp.getTenure());
        loanProductId = getCarTenureProductId();
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
     * @return the carLoanProducts
     */
    public List<LoanProduct> getCarLoanProducts() {
        return carLoanProducts;
    }

    /**
     * @param carLoanProducts the carLoanProducts to set
     */
    public void setCarLoanProducts(List<LoanProduct> carLoanProducts) {
        this.carLoanProducts = carLoanProducts;
    }

    /**
     * @return the carLoanAnnualInterestRate
     */
    public Double getCarLoanAnnualInterestRate() {
        return carLoanAnnualInterestRate;
    }

    /**
     * @return the carLoanAmt
     */
    public Double getCarLoanAmt() {
        return carLoanAmt;
    }

    /**
     * @param carLoanAmt the carLoanAmt to set
     */
    public void setCarLoanAmt(Double carLoanAmt) {
        this.carLoanAmt = carLoanAmt;
    }

    /**
     * @return the carLoanMonthlyInstalment
     */
    public Double getCarLoanMonthlyInstalment() {
        return carLoanMonthlyInstalment;
    }

    /**
     * @param carLoanMonthlyInstalment the carLoanMonthlyInstalment to set
     */
    public void setCarLoanMonthlyInstalment(Double carLoanMonthlyInstalment) {
        this.carLoanMonthlyInstalment = carLoanMonthlyInstalment;
    }

    /**
     * @return the carOpenMarketValue
     */
    public Double getCarOpenMarketValue() {
        return carOpenMarketValue;
    }

    /**
     * @param carOpenMarketValue the carOpenMarketValue to set
     */
    public void setCarOpenMarketValue(Double carOpenMarketValue) {
        this.carOpenMarketValue = carOpenMarketValue;
    }

    /**
     * @return the carTenureProductId
     */
    public Long getCarTenureProductId() {
        return carTenureProductId;
    }

    /**
     * @param carTenureProductId the carTenureProductId to set
     */
    public void setCarTenureProductId(Long carTenureProductId) {
        this.carTenureProductId = carTenureProductId;
    }

    /**
     * @return the carTenure
     */
    public Integer getCarTenure() {
        return carTenure;
    }

    /**
     * @param carTenure the carTenure to set
     */
    public void setCarTenure(Integer carTenure) {
        this.carTenure = carTenure;
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

    public Double getMaxCarLoanMonthlyInstalment() {
        return maxCarLoanMonthlyInstalment;
    }

    public void setMaxCarLoanMonthlyInstalment(Double maxCarLoanMonthlyInstalment) {
        this.maxCarLoanMonthlyInstalment = maxCarLoanMonthlyInstalment;
    }

    public Double getMaxCarLoanAmt() {
        return maxCarLoanAmt;
    }

    public void setMaxCarLoanAmt(Double maxCarLoanAmt) {
        this.maxCarLoanAmt = maxCarLoanAmt;
    }

    public Double getLTV() {
        return LTV;
    }

    public void setLTV(Double LTV) {
        this.LTV = LTV;
    }

    public Integer getCarMaxTenure() {
        return carMaxTenure;
    }

    public void setCarMaxTenure(Integer carMaxTenure) {
        this.carMaxTenure = carMaxTenure;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
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

    private Object getBirthday() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
  
}
