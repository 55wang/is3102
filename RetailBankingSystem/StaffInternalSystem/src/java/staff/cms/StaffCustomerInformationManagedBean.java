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
import server.utilities.EnumUtils.Citizenship;
import server.utilities.EnumUtils.Education;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.MaritalStatus;
import server.utilities.EnumUtils.Nationality;
import server.utilities.EnumUtils.Occupation;
import server.utilities.EnumUtils.StatusType;
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
    private static Customer selectedCustomer = new Customer();
    private List<String> StatusOptions = CommonUtils.getEnumList(EnumUtils.StatusType.class);
    private List<String> GenderOptions = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> NationalityOptions = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> IdentityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> CitizenOptions = CommonUtils.getEnumList(EnumUtils.Citizenship.class);
    private List<String> OccupationOptions = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> IncomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);
    private List<String> MaritalStatusOptions = CommonUtils.getEnumList(EnumUtils.MaritalStatus.class);
    private List<String> EducationOptions = CommonUtils.getEnumList(EnumUtils.Education.class);
    private static String selectedStatus;
    private static String selectedGender;
    private static String selectedNationality;
    private static String selectedIdentityType;
    private static String selectedCitizenship;
    private static String selectedOccupation;
    private static String selectedIncome;
    private static String selectedMaritalStatus;
    private static String selectedEducation;

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

    public void saveUpdatedCustomerInformation() {
        selectedCustomer.setGender(Gender.getEnum(selectedGender));
        selectedCustomer.setNationality(Nationality.getEnum(selectedNationality));
        selectedCustomer.setIdentityType(IdentityType.getEnum(selectedIdentityType));
        selectedCustomer.setCitizenship(Citizenship.getEnum(selectedCitizenship));
        selectedCustomer.setOccupation(Occupation.getEnum(selectedOccupation));
        selectedCustomer.setIncome(Income.getEnum(selectedIncome));
        selectedCustomer.setMaritalStatus(MaritalStatus.getEnum(selectedMaritalStatus));
        selectedCustomer.setEducation(Education.getEnum(selectedEducation));

        Customer result = getCustomerProfileSessionBean().saveProfile(selectedCustomer);

        if (result == null) {
            MessageUtils.displayError("Customer not found!");
        } else {
            MessageUtils.displayInfo("Cusotmer information is updated!");
            RedirectUtils.redirect("staff-edit-customer.xhtml");

        }

    }

    public void goToEditPage(Customer customer) {
        setSelectedCustomer(customer);
        if (customer.getGender() != null) {
            setSelectedGender(customer.getGender().toString());
        }
        if (customer.getNationality() != null) {
            setSelectedNationality(customer.getNationality().toString());
        }
        if (customer.getIdentityType() != null) {
            setSelectedIdentityType(customer.getIdentityType().toString());
        }
        if (customer.getCitizenship() != null) {
            setSelectedCitizenship(customer.getCitizenship().toString());
        }
        if (customer.getIncome() != null) {
            setSelectedIncome(customer.getIncome().toString());
        }
        if (customer.getMaritalStatus() != null) {
            setSelectedMaritalStatus(customer.getMaritalStatus().toString());
        }
        if (customer.getEducation() != null) {
            setSelectedEducation(customer.getEducation().toString());
        }

        RedirectUtils.redirect("staff-edit-customer.xhtml");

    }

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
     * @return the StatusOptions
     */
    public List<String> getStatusOptions() {
        return StatusOptions;
    }

    /**
     * @param StatusOptions the StatusOptions to set
     */
    public void setStatusOptions(List<String> StatusOptions) {
        this.StatusOptions = StatusOptions;
    }

    /**
     * @return the GenderOptions
     */
    public List<String> getGenderOptions() {
        return GenderOptions;
    }

    /**
     * @param GenderOptions the GenderOptions to set
     */
    public void setGenderOptions(List<String> GenderOptions) {
        this.GenderOptions = GenderOptions;
    }

    /**
     * @return the NationalityOptions
     */
    public List<String> getNationalityOptions() {
        return NationalityOptions;
    }

    /**
     * @param NationalityOptions the NationalityOptions to set
     */
    public void setNationalityOptions(List<String> NationalityOptions) {
        this.NationalityOptions = NationalityOptions;
    }

    /**
     * @return the IdentityTypeOptions
     */
    public List<String> getIdentityTypeOptions() {
        return IdentityTypeOptions;
    }

    /**
     * @param IdentityTypeOptions the IdentityTypeOptions to set
     */
    public void setIdentityTypeOptions(List<String> IdentityTypeOptions) {
        this.IdentityTypeOptions = IdentityTypeOptions;
    }

    /**
     * @return the CitizenOptions
     */
    public List<String> getCitizenOptions() {
        return CitizenOptions;
    }

    /**
     * @param CitizenOptions the CitizenOptions to set
     */
    public void setCitizenOptions(List<String> CitizenOptions) {
        this.CitizenOptions = CitizenOptions;
    }

    /**
     * @return the OccupationOptions
     */
    public List<String> getOccupationOptions() {
        return OccupationOptions;
    }

    /**
     * @param OccupationOptions the OccupationOptions to set
     */
    public void setOccupationOptions(List<String> OccupationOptions) {
        this.OccupationOptions = OccupationOptions;
    }

    /**
     * @return the IncomeOptions
     */
    public List<String> getIncomeOptions() {
        return IncomeOptions;
    }

    /**
     * @param IncomeOptions the IncomeOptions to set
     */
    public void setIncomeOptions(List<String> IncomeOptions) {
        this.IncomeOptions = IncomeOptions;
    }

    /**
     * @return the MaritalStatusOptions
     */
    public List<String> getMaritalStatusOptions() {
        return MaritalStatusOptions;
    }

    /**
     * @param MaritalStatusOptions the MaritalStatusOptions to set
     */
    public void setMaritalStatusOptions(List<String> MaritalStatusOptions) {
        this.MaritalStatusOptions = MaritalStatusOptions;
    }

    /**
     * @return the EducationOptions
     */
    public List<String> getEducationOptions() {
        return EducationOptions;
    }

    /**
     * @param EducationOptions the EducationOptions to set
     */
    public void setEducationOptions(List<String> EducationOptions) {
        this.EducationOptions = EducationOptions;
    }

    /**
     * @return the selectedStatus
     */
    public String getSelectedStatus() {
        return selectedStatus;
    }

    /**
     * @param selectedStatus the selectedStatus to set
     */
    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    /**
     * @return the selectedGender
     */
    public String getSelectedGender() {
        return selectedGender;
    }

    /**
     * @param selectedGender the selectedGender to set
     */
    public void setSelectedGender(String selectedGender) {
        this.selectedGender = selectedGender;
    }

    /**
     * @return the selectedNationality
     */
    public String getSelectedNationality() {
        return selectedNationality;
    }

    /**
     * @param selectedNationality the selectedNationality to set
     */
    public void setSelectedNationality(String selectedNationality) {
        this.selectedNationality = selectedNationality;
    }

    /**
     * @return the selectedIdentityType
     */
    public String getSelectedIdentityType() {
        return selectedIdentityType;
    }

    /**
     * @param selectedIdentityType the selectedIdentityType to set
     */
    public void setSelectedIdentityType(String selectedIdentityType) {
        this.selectedIdentityType = selectedIdentityType;
    }

    /**
     * @return the selectedCitizenship
     */
    public String getSelectedCitizenship() {
        return selectedCitizenship;
    }

    /**
     * @param selectedCitizenship the selectedCitizenship to set
     */
    public void setSelectedCitizenship(String selectedCitizenship) {
        this.selectedCitizenship = selectedCitizenship;
    }

    /**
     * @return the selectedOccupation
     */
    public String getSelectedOccupation() {
        return selectedOccupation;
    }

    /**
     * @param selectedOccupation the selectedOccupation to set
     */
    public void setSelectedOccupation(String selectedOccupation) {
        this.selectedOccupation = selectedOccupation;
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

    /**
     * @return the selectedMaritalStatus
     */
    public String getSelectedMaritalStatus() {
        return selectedMaritalStatus;
    }

    /**
     * @param selectedMaritalStatus the selectedMaritalStatus to set
     */
    public void setSelectedMaritalStatus(String selectedMaritalStatus) {
        this.selectedMaritalStatus = selectedMaritalStatus;
    }

    /**
     * @return the selectedEducation
     */
    public String getSelectedEducation() {
        return selectedEducation;
    }

    /**
     * @param selectedEducation the selectedEducation to set
     */
    public void setSelectedEducation(String selectedEducation) {
        this.selectedEducation = selectedEducation;
    }

    /**
     * @return the selectedCustomer
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * @param aSelectedCustomer the selectedCustomer to set
     */
    public void setSelectedCustomer(Customer aSelectedCustomer) {
        selectedCustomer = aSelectedCustomer;
    }
}
