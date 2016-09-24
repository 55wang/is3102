/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
import server.utilities.EnumUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils.DepositAccountType;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.Nationality;
import server.utilities.EnumUtils.Occupation;
import server.utilities.EnumUtils.StatusType;
import utils.CommonUtils;
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
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositAccountBean;
    @EJB
    private DepositProductSessionBeanLocal depositProductBean;

    private Customer customer = new Customer();
    private String initialDepositAccount;

    private List<String> IdentityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> NationalityOptions = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> GenderOptions = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> OccupationOptions = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> IncomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);
    // TODO: For resend Email Button
    private Boolean emailSuccessFlag = true;
    private String selectedIdentityType;
    private String selectedNationality;
    private String selectedGender;
    private String selectedOccupation;
    private String selectedIncome;

    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public CustomerApplicationManagedBean() {
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
        customer.setIncome(Income.getEnum(selectedIncome));
        customer.setIdentityType(IdentityType.getEnum(selectedIdentityType));
        customer.setNationality(Nationality.getEnum(selectedNationality));
        customer.setGender(Gender.getEnum(selectedGender));
        customer.setOccupation(Occupation.getEnum(selectedOccupation));

        customer.setMainAccount(new MainAccount());
        MainAccount mainAccount = customer.getMainAccount();
        mainAccount.setStatus(StatusType.PENDING);
        mainAccount.setUserID(generateUserID(customer.getIdentityType(), customer.getIdentityNumber()));
        String randomPwd = generatePwd();
        mainAccount.setPassword(randomPwd);

        newCustomerSessionBean.createCustomer(customer);

        CustomerDepositAccount depostiAccount = new CustomerDepositAccount();
        depostiAccount.setMainAccount(mainAccount);
        if (initialDepositAccount.equals(ConstantUtils.DEMO_CURRENT_DEPOSIT_PRODUCT_NAME)) {
            depostiAccount.setType(DepositAccountType.CURRENT);
            depostiAccount.setProduct(depositProductBean.getDepositProductByName(ConstantUtils.DEMO_CURRENT_DEPOSIT_PRODUCT_NAME));
        } else if (initialDepositAccount.equals(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME)) {
            depostiAccount.setType(DepositAccountType.CUSTOM);
            depostiAccount.setProduct(depositProductBean.getDepositProductByName(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME));
        }

        depositAccountBean.createAccount(depostiAccount);

        try {
            emailServiceSessionBean.sendActivationGmailForCustomer(customer.getEmail(), randomPwd);
            RedirectUtils.redirect("../common/register_successful.xhtml");
        } catch (Exception ex) {
            setEmailSuccessFlag((Boolean) false);
        }

//        emailServiceSessionBean.sendActivationEmailForCustomer(customer.getEmail());
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String generateUserID(IdentityType identityType, String identityNum) {

        if (identityType.equals(IdentityType.NRIC)) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else if (identityType.equals(IdentityType.PASSPORT)) {
            return "c" + identityNum.substring(1);
        } else {
            return "error";
        }
    }

    private String generatePwd() {
        int pwdLen = 10;
        SecureRandom rnd = new SecureRandom();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(pwdLen);
        for (int i = 0; i < pwdLen; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
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
}
