/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.dams;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.NotNull;
import org.primefaces.event.FlowEvent;
import server.utilities.EnumUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils.DepositAccountType;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.Nationality;
import server.utilities.EnumUtils.StatusType;
import server.utilities.PincodeGenerationUtils;
import server.utilities.CommonUtils;
import util.exception.common.DuplicateMainAccountExistException;
import util.exception.dams.DuplicateDepositAccountException;
import utils.JSUtils;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerApplicationManagedBean")
@ViewScoped
public class CustomerApplicationManagedBean implements Serializable {

    @EJB
    private UtilsSessionBeanLocal utilsSessionBean;

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositAccountBean;
    @EJB
    private DepositProductSessionBeanLocal depositProductBean;

    private Customer customer = new Customer();
    @NotNull(message = "Deposit Product may not be null")
    private String initialDepositAccount;

    private List<String> identityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> nationalityOptions = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> genderOptions = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> occupationOptions = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> incomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);
    // TODO: For resend Email Button
    private Boolean emailSuccessFlag = true;
    @NotNull(message = "Identity type is required.")
    private String selectedIdentityType;
    private String selectedNationality;
    private String selectedGender;
    private String selectedOccupation;
    private String selectedIncome;
    private Date currentDate = new Date();

    private final DepositAccountType FIXED_DEPOSIT_PRODUCT = DepositAccountType.FIXED;

    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public CustomerApplicationManagedBean() {
    }

    public void validateInputTwoThenNext() {

        if (utilsSessionBean.checkIdentityNumberIsUnique(customer.getIdentityNumber()) == false) {
            MessageUtils.displayError("Identity Number is registered!");
        } else {
            JSUtils.callJSMethod("PF('myWizard').next();");
        }
    }

    public void validateInputThreeThenNext() {

        if (utilsSessionBean.checkEmailIsUnique(customer.getEmail()) == false) {
            MessageUtils.displayError("Email is registered!");
        } else if (utilsSessionBean.checkPhoneIsUnique(customer.getPhone()) == false) {
            MessageUtils.displayError("Phone is registered!");
        } else {
            JSUtils.callJSMethod("PF('myWizard').next();");
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("CustomerApplicationManagedBean @PostContruct");
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void save() {

        try {

            MainAccount mainAccount = new MainAccount();
            mainAccount.setStatus(StatusType.PENDING);
            mainAccount.setUserID(generateUserID(IdentityType.NRIC, customer.getIdentityNumber()));
            String randomPwd = PincodeGenerationUtils.generatePwd();
            mainAccount.setPassword(randomPwd);

            mainAccount = mainAccountSessionBean.createMainAccount(mainAccount);

            customer.setIncome(Income.getEnum(selectedIncome));
            customer.setNationality(Nationality.getEnum(selectedNationality));
            customer.setGender(Gender.getEnum(selectedGender));
            customer.setMainAccount(mainAccount);
            newCustomerSessionBean.createCustomer(customer);

            CustomerDepositAccount depostiAccount = new CustomerDepositAccount();
            depostiAccount.setMainAccount(mainAccount);
            if (initialDepositAccount.equals(ConstantUtils.DEMO_CURRENT_DEPOSIT_PRODUCT_NAME)) {
                depostiAccount.setType(DepositAccountType.CURRENT);
            } else if (initialDepositAccount.equals(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME)) {
                depostiAccount.setType(DepositAccountType.CUSTOM);
            }

            depostiAccount.setProduct(depositProductBean.getDepositProductByName(initialDepositAccount));

            depositAccountBean.createAccount(depostiAccount);

            try {
                emailServiceSessionBean.sendActivationGmailForCustomer(customer.getEmail(), randomPwd);
                RedirectUtils.redirect("../common/register_successful.xhtml");
            } catch (Exception ex) {
                setEmailSuccessFlag((Boolean) false);
            }

        } catch (DuplicateMainAccountExistException | DuplicateDepositAccountException e) {
            System.out.println("DuplicateMainAccountExistException | DuplicateDepositAccountException thrown at CustomerApplicationManagedBean.java save()");
            MessageUtils.displayError("Not saved!");
        }
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String generateUserID(IdentityType identityType, String identityNum) {

        if (identityType.equals(IdentityType.NRIC)) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else {
            return "error";
        }
    }

    /**
     * @return the initialDepositAccount
     */
    public String getInitialDepositAccount() {
        return initialDepositAccount;
    }

    /**
     * @param initialDepositAccount the initialDepositAccount to set
     */
    public void setInitialDepositAccount(String initialDepositAccount) {
        this.initialDepositAccount = initialDepositAccount;
    }

    /**
     * @return the emailSuccessFlag
     */
    public Boolean getEmailSuccessFlag() {
        return emailSuccessFlag;
    }

    /**
     * @param emailSuccessFlag the emailSuccessFlag to set
     */
    public void setEmailSuccessFlag(Boolean emailSuccessFlag) {
        this.emailSuccessFlag = emailSuccessFlag;
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
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
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
     * @return the FIXED_DEPOSIT_PRODUCT
     */
    public DepositAccountType getFIXED_DEPOSIT_PRODUCT() {
        return FIXED_DEPOSIT_PRODUCT;
    }

}
