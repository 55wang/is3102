/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import entity.customer.Customer;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.CommonUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "formDemoManagedBean")
@ViewScoped
public class formDemoManagedBean implements Serializable {

    private String selectedEnum;
    private List<String> selectEnums = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private Customer customer;
    private String selectedIncome;
    private List<String> incomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);


    /**
     * Creates a new instance of formDemoManagedBean
     */
    public formDemoManagedBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("Form Demo Construct");
        setCustomer(new Customer());
    }

    public void print() {
        System.out.println("Printing result");
        System.out.println(selectedIncome);
    }

    /**
     * @return the selectedEnum
     */
    public String getSelectedEnum() {
        return selectedEnum;
    }

    /**
     * @param selectedEnum the selectedEnum to set
     */
    public void setSelectedEnum(String selectedEnum) {
        this.selectedEnum = selectedEnum;
    }

    /**
     * @return the selectEnums
     */
    public List<String> getSelectEnums() {
        return selectEnums;
    }

    /**
     * @param selectEnums the selectEnums to set
     */
    public void setSelectEnums(List<String> selectEnums) {
        this.selectEnums = selectEnums;
    }

    /**
     * @return the incomeOptions
     */
    public List<String> getIncomeOptions() {
        return incomeOptions;
    }

    /**
     * @param incomeOptions the incomeOptions to set
     */
    public void setIncomeOptions(List<String> incomeOptions) {
        this.incomeOptions = incomeOptions;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the selectedIncome
     */
    public String getSelectedIncome() {
        return selectedIncome;
    }

    /**
     * @param selectedIncome the selectedIncome to set
     */
    public void setSelectedIncome(String selectedIncome) {
        this.selectedIncome = selectedIncome;
    }



  
 

}
