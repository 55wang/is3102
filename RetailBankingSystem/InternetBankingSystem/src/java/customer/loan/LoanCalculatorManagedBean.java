/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.loan;

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
@Named(value = "loanCalculatorManagedBean")
@ViewScoped
public class LoanCalculatorManagedBean implements Serializable {

    @EJB
    private LoanCalculationSessionBeanLocal calculator;
    
    //car
    private final Double carLoanAnnualInterestRate=0.025;
    private Double carLoanAmt=0.0;       
    private Double carLoanMonthlyInstalment;
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
     * Creates a new instance of CarLoanCalculatorManagedBean
     */
    public LoanCalculatorManagedBean() {
    }
    
    
    
    public void calculateCar(){
        
        if (age < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
            return;
        }
        
        if (monthlyIncome < 2000) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_2000);
            return;
        }
        
        setCarLoanAmt(calculator.calculateMaxCarLoanAmt(getCarOpenMarketValue()));
        setCarLoanMonthlyInstalment(calculator.calculateCarMonthlyInstalment(getCarLoanAnnualInterestRate(), Integer.parseInt(getCarTenure()), getCarLoanAmt()));    
        JSUtils.callJSMethod("PF('myWizard').next()");
    }

    public void calculateHDB(){
        if (age < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
            return;
        }
        
        if (monthlyIncome < 1500) {
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
        if (age < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
            return;
        }
        
        if (monthlyIncome < 1500) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_INCOME_1500);
            return;
        }
        setTenure(calculator.calculateMaxPPTenure(getAge(), getMonthlyIncome()));
        setMonthlyInstalment(calculator.calculateMaxPPMonthlyInstalment(getMonthlyIncome(), getOtherLoan()));
        setLoanAmt(calculator.calculateMaxHomeLoanAmt(getMonthlyInstalment(), getTenure()));
        setValue(calculator.homeMarketValue(getLoanAmt(), getNumOfHousingLoan()));
        JSUtils.callJSMethod("PF('myWizard').next()");
    }

    // getters and setters
    public Double getCarLoanAmt() {
        return carLoanAmt;
    }

    public void setCarLoanAmt(Double carLoanAmt) {
        this.carLoanAmt = carLoanAmt;
    }

    public Double getCarLoanMonthlyInstalment() {
        return carLoanMonthlyInstalment;
    }

    public void setCarLoanMonthlyInstalment(Double carLoanMonthlyInstalment) {
        this.carLoanMonthlyInstalment = carLoanMonthlyInstalment;
    }

    public Double getCarOpenMarketValue() {
        return carOpenMarketValue;
    }

    public void setCarOpenMarketValue(Double carOpenMarketValue) {
        this.carOpenMarketValue = carOpenMarketValue;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getOtherLoan() {
        return otherLoan;
    }

    public void setOtherLoan(Double otherLoan) {
        this.otherLoan = otherLoan;
    }

    public Integer getNumOfHousingLoan() {
        return numOfHousingLoan;
    }

    public void setNumOfHousingLoan(Integer numOfHousingLoan) {
        this.numOfHousingLoan = numOfHousingLoan;
    }

    public Double getMonthlyInstalment() {
        return monthlyInstalment;
    }

    public void setMonthlyInstalment(Double monthlyInstalment) {
        this.monthlyInstalment = monthlyInstalment;
    }

    public Double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(Double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getCarLoanAnnualInterestRate() {
        return carLoanAnnualInterestRate;
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
    
    
}
