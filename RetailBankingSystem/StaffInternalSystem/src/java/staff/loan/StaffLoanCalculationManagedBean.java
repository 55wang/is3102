/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.loan.LoanCalculationSessionBeanLocal;
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
@Named(value = "staffLoanCalculationManagedBean")
@ViewScoped
public class StaffLoanCalculationManagedBean implements Serializable {

    @EJB
    private LoanCalculationSessionBeanLocal calculator;
    
    //car
    private final Double carLoanAnnualInterestRate=0.025;
    private Double carLoanAmt=0.0;       
    private Double carLoanMonthlyInstalment;
    private Double carMaxMonthlyInstalment;
    private Double carOpenMarketValue;
    private String carTenure;
    
    //HDB or PP
    private Integer age;
    private Integer tenure;
    private Double monthlyIncome;
    private Double otherLoan;
    private Integer numOfHousingLoan;


    private Double monthlyInstalment;
    private Double loanAmt;
    private Double value;
    
    /**
     * Creates a new instance of StaffLoanCalculationManagedBean
     */
    public StaffLoanCalculationManagedBean() {
    }
    
    public void calculateCar(){
        
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
            return;
        }
        
        if (getMonthlyIncome() < 2000) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_2000);
            return;
        }
        
        setCarMaxMonthlyInstalment(calculator.calculateMaxPPMonthlyInstalment(monthlyIncome, otherLoan));
        setCarLoanAmt(calculator.calculateMaxCarLoanAmt(carMaxMonthlyInstalment, tenure));
        setCarOpenMarketValue(calculator.calculateMaxCarPrice(carLoanAmt));
        JSUtils.callJSMethod("PF('myWizard').next()");
    }

    public void calculateHDB(){
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
            return;
        }
        
        if (getMonthlyIncome() < 1500) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_1500);
            return;
        }
        setTenure(calculator.calculateMaxHDBTenure(getAge(), getMonthlyIncome()));
        setMonthlyInstalment(calculator.calculateMaxHDBMonthlyInstalment(getMonthlyIncome(), getOtherLoan()));
        setLoanAmt(calculator.calculateMaxHomeLoanAmt(getMonthlyInstalment(), getTenure()));
        setValue(calculator.homeMarketValue(getLoanAmt(), getNumOfHousingLoan()));
        JSUtils.callJSMethod("PF('myWizard').next()");
    }

    public void calculatePP(){
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
            return;
        }
        
        if (getMonthlyIncome() < 1500) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_1500);
            return;
        }
        setTenure(calculator.calculateMaxPPTenure(getAge(), getMonthlyIncome()));
        setMonthlyInstalment(calculator.calculateMaxPPMonthlyInstalment(getMonthlyIncome(), getOtherLoan()));
        setLoanAmt(calculator.calculateMaxHomeLoanAmt(getMonthlyInstalment(), getTenure()));
        setValue(calculator.homeMarketValue(getLoanAmt(), getNumOfHousingLoan()));
        JSUtils.callJSMethod("PF('myWizard').next()");
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
     * @return the carTenure
     */
    public String getCarTenure() {
        return carTenure;
    }

    /**
     * @param carTenure the carTenure to set
     */
    public void setCarTenure(String carTenure) {
        this.carTenure = carTenure;
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
     * @return the tenure
     */
    public Integer getTenure() {
        return tenure;
    }

    /**
     * @param tenure the tenure to set
     */
    public void setTenure(Integer tenure) {
        this.tenure = tenure;
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
     * @return the loanAmt
     */
    public Double getLoanAmt() {
        return loanAmt;
    }

    /**
     * @param loanAmt the loanAmt to set
     */
    public void setLoanAmt(Double loanAmt) {
        this.loanAmt = loanAmt;
    }

    /**
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }

    public Double getCarMaxMonthlyInstalment() {
        return carMaxMonthlyInstalment;
    }

    public void setCarMaxMonthlyInstalment(Double carMaxMonthlyInstalment) {
        this.carMaxMonthlyInstalment = carMaxMonthlyInstalment;
    }
    
    
    
}
