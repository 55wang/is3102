/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.Education;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.MaritalStatus;
import server.utilities.EnumUtils.Nationality;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import util.exception.cms.CustomerNotExistException;
import util.exception.cms.UpdateCustomerException;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "staffCustomerInformationManagedBean")
@ViewScoped
public class StaffCustomerInformationManagedBean implements Serializable {

    @EJB
    private UtilsSessionBeanLocal utilsSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailBean;

    private List<Customer> customers;
    private String searchText;
    private static Customer selectedCustomer = new Customer();
    private List<String> statusOptions = CommonUtils.getEnumList(EnumUtils.StatusType.class);
    private List<String> genderOptions = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> nationalityOptions = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> identityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> citizenOptions = CommonUtils.getEnumList(EnumUtils.Citizenship.class);
    private List<String> occupationOptions = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> incomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);
    private List<String> maritalStatusOptions = CommonUtils.getEnumList(EnumUtils.MaritalStatus.class);
    private List<String> educationOptions = CommonUtils.getEnumList(EnumUtils.Education.class);
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

    /**
     * @param customers the customers to set
     */
    @PostConstruct
    public void setCustomers() {
        this.setCustomers(getCustomerProfileSessionBean().retrieveActivatedCustomers());
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter create_customer_information.xhtml");
        a.setFunctionName("StaffCustomerInformationCaseManagedBean @PostConstruct setCustomers()");
        a.setFunctionInput("Getting all customer information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void search() {
        if (searchText.isEmpty()) {
            setCustomers(getCustomerProfileSessionBean().retrieveActivatedCustomers());
        } else {
            try {
                Customer c = getCustomerProfileSessionBean().searchCustomerByIdentityNumber(searchText);
                customers = new ArrayList<>();
                customers.add(c);
                setCustomers(customers);
            } catch (CustomerNotExistException e) {
                MessageUtils.displayInfo("No customer found!");
            }
        }
    }

    public void saveUpdatedCustomerInformation() {
        selectedCustomer.setGender(Gender.getEnum(selectedGender));
        selectedCustomer.setNationality(Nationality.getEnum(selectedNationality));
        selectedCustomer.setIncome(Income.getEnum(selectedIncome));
        selectedCustomer.setMaritalStatus(MaritalStatus.getEnum(selectedMaritalStatus));
        selectedCustomer.setEducation(Education.getEnum(selectedEducation));

        if (utilsSessionBean.checkUpdatedEmailIsUnique(selectedCustomer) == false) {
            MessageUtils.displayInfo("Email is registered!");

        } else if (utilsSessionBean.checkUpdatedPhoneIsUnique(selectedCustomer) == false) {
            MessageUtils.displayInfo("Phone is registered!");

        } else {
            try {
                Customer result = customerProfileSessionBean.updateCustomer(selectedCustomer);
                MessageUtils.displayInfo("Profile successfully updated!");
                emailBean.sendUpdatedProfile(selectedCustomer.getEmail());
                RedirectUtils.redirect("staff_view_customer.xhtml");
            } catch (UpdateCustomerException e) {
                MessageUtils.displayError("Update is unsuccessful, please check your input.");
            }
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
        if (customer.getIncome() != null) {
            setSelectedIncome(customer.getIncome().toString());
        }
        if (customer.getMaritalStatus() != null) {
            setSelectedMaritalStatus(customer.getMaritalStatus().toString());
        }
        if (customer.getEducation() != null) {
            setSelectedEducation(customer.getEducation().toString());
        }

        RedirectUtils.redirect(ConstantUtils.STAFF_CMS_STAFF_EDIT_CUSTOMER);
    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
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

    /**
     * @return the statusOptions
     */
    public List<String> getStatusOptions() {
        return statusOptions;
    }

    /**
     * @param statusOptions the statusOptions to set
     */
    public void setStatusOptions(List<String> statusOptions) {
        this.statusOptions = statusOptions;
    }

    /**
     * @return the genderOptions
     */
    public List<String> getGenderOptions() {
        return genderOptions;
    }

    /**
     * @param genderOptions the genderOptions to set
     */
    public void setGenderOptions(List<String> genderOptions) {
        this.genderOptions = genderOptions;
    }

    /**
     * @return the nationalityOptions
     */
    public List<String> getNationalityOptions() {
        return nationalityOptions;
    }

    /**
     * @param nationalityOptions the nationalityOptions to set
     */
    public void setNationalityOptions(List<String> nationalityOptions) {
        this.nationalityOptions = nationalityOptions;
    }

    /**
     * @return the identityTypeOptions
     */
    public List<String> getIdentityTypeOptions() {
        return identityTypeOptions;
    }

    /**
     * @param identityTypeOptions the identityTypeOptions to set
     */
    public void setIdentityTypeOptions(List<String> identityTypeOptions) {
        this.identityTypeOptions = identityTypeOptions;
    }

    /**
     * @return the citizenOptions
     */
    public List<String> getCitizenOptions() {
        return citizenOptions;
    }

    /**
     * @param citizenOptions the citizenOptions to set
     */
    public void setCitizenOptions(List<String> citizenOptions) {
        this.citizenOptions = citizenOptions;
    }

    /**
     * @return the occupationOptions
     */
    public List<String> getOccupationOptions() {
        return occupationOptions;
    }

    /**
     * @param occupationOptions the occupationOptions to set
     */
    public void setOccupationOptions(List<String> occupationOptions) {
        this.occupationOptions = occupationOptions;
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
     * @return the maritalStatusOptions
     */
    public List<String> getMaritalStatusOptions() {
        return maritalStatusOptions;
    }

    /**
     * @param maritalStatusOptions the maritalStatusOptions to set
     */
    public void setMaritalStatusOptions(List<String> maritalStatusOptions) {
        this.maritalStatusOptions = maritalStatusOptions;
    }

    /**
     * @return the educationOptions
     */
    public List<String> getEducationOptions() {
        return educationOptions;
    }

    /**
     * @param educationOptions the educationOptions to set
     */
    public void setEducationOptions(List<String> educationOptions) {
        this.educationOptions = educationOptions;
    }
}
