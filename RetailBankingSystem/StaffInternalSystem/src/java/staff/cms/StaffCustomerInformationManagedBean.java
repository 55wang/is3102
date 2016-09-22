/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.IdentityType;
import utils.CommonUtils;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "staffCustomerInformationManagedBean")
@ViewScoped
public class StaffCustomerInformationManagedBean implements Serializable {

    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    private List<Customer> customers;
    private String searchText;
//    private static Customer selectedCustomer;
    private List<String> selectStatuses = CommonUtils.getEnumList(EnumUtils.StatusType.class);
    private List<String> selectGenders = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> selectNationalities = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> selectIdentityTypes = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> selectCitizenships = CommonUtils.getEnumList(EnumUtils.Citizenship.class);
    private List<String> selectOccupations = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> selectIncome = CommonUtils.getEnumList(EnumUtils.Income.class);
    private List<String> selectMaritalStatus = CommonUtils.getEnumList(EnumUtils.MaritalStatus.class);
    private List<String> selectEducation = CommonUtils.getEnumList(EnumUtils.Education.class);

    /**
     * Creates a new instance of StaffCustomerInformationManagedBean
     */
    public StaffCustomerInformationManagedBean() {
    }

    public void search() {
        if (searchText.isEmpty()) {
            setCustomers(getCustomerProfileSessionBean().retrieveActivatedCustomers());
        } else {
            setCustomers(getCustomerProfileSessionBean().searchCustomerByIdentityNumber(searchText));
        }

    }

    public void saveUpdatedCustomerInformation(Customer cusotmer) {
     
        Customer result = getCustomerProfileSessionBean().saveProfile(cusotmer);

        if (result == null) {
            MessageUtils.displayError("Customer not found!");
        } else {
            MessageUtils.displayInfo("Cusotmer information is updated!");
                         RedirectUtils.redirect("staff-view-customer.xhtml");

        }

    }

//    public void goToEditPage(Customer customer) {
//        selectedCustomer = customer;
////        selectedStatus;
////    private String selectedGender;
////    private String selectedNationality;
//        selectedIdentityType = selectedCustomer.getIdentityType().getValue();
//        System.out.print("++++++++++++++++++++"+selectedIdentityType);
////    private String selectedCitizenship;
////    private String selectedOccupation;
////    private String selectedIncome;
////    private String selectedMaritalStatus;
//        RedirectUtils.redirect("staff-edit-customer.xhtml");
//
//    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers the customers to set
     */
    @PostConstruct
    public void setCustomers() {
        this.setCustomers(getCustomerProfileSessionBean().retrieveActivatedCustomers());

    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * @return the selectStatuses
     */
    public List<String> getSelectStatuses() {
        return selectStatuses;
    }

    /**
     * @param selectStatuses the selectStatuses to set
     */
    public void setSelectStatuses(List<String> selectStatuses) {
        this.selectStatuses = selectStatuses;
    }

    /**
     * @return the selectGenders
     */
    public List<String> getSelectGenders() {
        return selectGenders;
    }

    /**
     * @param selectGenders the selectGenders to set
     */
    public void setSelectGenders(List<String> selectGenders) {
        this.selectGenders = selectGenders;
    }

    /**
     * @return the selectNationalities
     */
    public List<String> getSelectNationalities() {
        return selectNationalities;
    }

    /**
     * @param selectNationalities the selectNationalities to set
     */
    public void setSelectNationalities(List<String> selectNationalities) {
        this.selectNationalities = selectNationalities;
    }

    /**
     * @return the selectIdentityTypes
     */
    public List<String> getSelectIdentityTypes() {
        return selectIdentityTypes;
    }

    /**
     * @param selectIdentityTypes the selectIdentityTypes to set
     */
    public void setSelectIdentityTypes(List<String> selectIdentityTypes) {
        this.selectIdentityTypes = selectIdentityTypes;
    }

    /**
     * @return the selectCitizenships
     */
    public List<String> getSelectCitizenships() {
        return selectCitizenships;
    }

    /**
     * @param selectCitizenships the selectCitizenships to set
     */
    public void setSelectCitizenships(List<String> selectCitizenships) {
        this.selectCitizenships = selectCitizenships;
    }

    /**
     * @return the selectOccupations
     */
    public List<String> getSelectOccupations() {
        return selectOccupations;
    }

    /**
     * @param selectOccupations the selectOccupations to set
     */
    public void setSelectOccupations(List<String> selectOccupations) {
        this.selectOccupations = selectOccupations;
    }

    /**
     * @return the selectIncome
     */
    public List<String> getSelectIncome() {
        return selectIncome;
    }

    /**
     * @param selectIncome the selectIncome to set
     */
    public void setSelectIncome(List<String> selectIncome) {
        this.selectIncome = selectIncome;
    }



    /**
     * @return the customerProfileSessionBean
     */
    public CustomerProfileSessionBeanLocal getCustomerProfileSessionBean() {
        return customerProfileSessionBean;
    }

    /**
     * @param customerProfileSessionBean the customerProfileSessionBean to set
     */
    public void setCustomerProfileSessionBean(CustomerProfileSessionBeanLocal customerProfileSessionBean) {
        this.customerProfileSessionBean = customerProfileSessionBean;
    }

    /**
     * @param customers the customers to set
     */
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    /**
     * @return the selectMaritalStatus
     */
    public List<String> getSelectMaritalStatus() {
        return selectMaritalStatus;
    }

    /**
     * @param selectMaritalStatus the selectMaritalStatus to set
     */
    public void setSelectMaritalStatus(List<String> selectMaritalStatus) {
        this.selectMaritalStatus = selectMaritalStatus;
    }

    /**
     * @return the selectEducation
     */
    public List<String> getSelectEducation() {
        return selectEducation;
    }

    /**
     * @param selectEducation the selectEducation to set
     */
    public void setSelectEducation(List<String> selectEducation) {
        this.selectEducation = selectEducation;
    }


}
