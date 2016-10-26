/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardProductSessionBeanLocal;
import ejb.session.card.CreditCardOrderSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.card.order.CreditCardOrder;
import entity.card.product.CreditCardProduct;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.CommonUtils;
import utils.RedirectUtils;
import server.utilities.EnumUtils.*;
import server.utilities.CommonHelper;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "newCardManagedBean")
@ViewScoped
public class NewCardManagedBean implements Serializable {

    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;

    @EJB
    private CardProductSessionBeanLocal newCardProductSessionBean;

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;

    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;

    @EJB
    private CreditCardOrderSessionBeanLocal creditCardOrderSessionBean;

    private CreditCardOrder cco;
    private MainAccount ma;
    private Customer customer;
    private Customer existingCustomer;
    private CreditCardAccount cca;

    private List<String> productNameOptions = new ArrayList<>();

    private List<String> residentialStatusOptions = CommonUtils.getEnumList(ResidentialStatus.class);
    private List<String> residentialTypeOptions = CommonUtils.getEnumList(ResidentialType.class);

    private List<String> employmentStatusOptions = CommonUtils.getEnumList(EmploymentStatus.class);
    private List<String> industryOptions = CommonUtils.getEnumList(Industry.class);
    private List<String> statusOptions = CommonUtils.getEnumList(StatusType.class);
    private List<String> genderOptions = CommonUtils.getEnumList(Gender.class);

    private List<String> occupationOptions = CommonUtils.getEnumList(Occupation.class);
    private List<String> incomeOptions = CommonUtils.getEnumList(Income.class);
    private List<String> maritalStatusOptions = CommonUtils.getEnumList(MaritalStatus.class);
    private List<String> educationOptions = CommonUtils.getEnumList(Education.class);

    private List<String> identityTypeOptions = CommonUtils.getEnumList(IdentityType.class);
    private List<String> nationalityOptions = CommonUtils.getEnumList(Nationality.class);
    private List<String> saluationOptions = CommonUtils.getEnumList(Salutation.class);

    private String selectedProductName;
    private String selectedResidentialStatus;
    private String selectedResidentialType;
    private String selectedEmploymentStatus;
    private String selectedEducation;
    private String selectedIndustry;
    private String selectedIdentityType;
    private String selectedNationality;
    private String selectedGender;
    private String selectedIncome;
    private Date currentDate = new Date();

    @PostConstruct
    public void retrieveProducts() {
        cco = new CreditCardOrder();
        ma = new MainAccount();
        setCustomer(new Customer());
        setCca(new CreditCardAccount());
        List<CreditCardProduct> ccps = newCardProductSessionBean.getListCreditCardProducts();
        for (CreditCardProduct ccp : ccps) {
            getProductNameOptions().add(ccp.getProductName());
        }
    }

    public NewCardManagedBean() {
    }

    public void saveUpdatedCreditCardOrderForNewCustomer() {
        System.out.println("inside saveupdatedCreditcardorder");
        //persist
        customer.setResidentialStatus(ResidentialStatus.getEnum(getSelectedResidentialStatus()));
        customer.setResidentialType(ResidentialType.getEnum(getSelectedResidentialType()));
        customer.setEmploymentStatus(EmploymentStatus.getEnum(getSelectedEmploymentStatus()));
        customer.setEducation(Education.getEnum(getSelectedEducation()));
        customer.setIndustry(Industry.getEnum(getSelectedIndustry()));
        customer.setIncome(Income.getEnum(getSelectedIncome()));
        customer.setIdentityType(IdentityType.getEnum(getSelectedIdentityType()));
        customer.setGender(Gender.getEnum(getSelectedGender()));
        customer.setNationality(Nationality.getEnum(getSelectedNationality()));
        newCustomerSessionBean.createCustomer(customer);

        ma = new MainAccount();
        mainAccountSessionBean.createMainAccount(ma);

        cca.setCardStatus(CardAccountStatus.NEW);
        cardAcctSessionBean.createCardAccount(cca);

        cco.setApplicationStatus(ApplicationStatus.NEW);
        creditCardOrderSessionBean.createCardOrder(cco);

        //update relations
        getCustomer().setMainAccount(ma);
        newCustomerSessionBean.updateCustomer(customer);

        cca.setMainAccount(ma);
        cca.setCreditCardProduct(newCardProductSessionBean.getCreditCardProductByProductName(selectedProductName));
        cca.setCreditCardOrder(cco);
        cardAcctSessionBean.updateCreditCardAccount(cca);

        cco.setMainAccount(ma);
        cco.setCreditCardAccount(cca);
        creditCardOrderSessionBean.updateCreditCardOrder(cco);

        ma.setUserID(CommonHelper.generateUserID(customer.getIdentityType(), customer.getIdentityNumber()));
        ma.setStatus(StatusType.NEW);
        ma.setCustomer(customer);
        mainAccountSessionBean.updateMainAccount(ma);

        emailServiceSessionBean.sendCreditCardApplicationNotice(customer.getEmail());
        RedirectUtils.redirect("/InternetBankingSystem/common/application_success.xhtml");

    }

    public void saveUpdatedCreditCardOrderForExistingCustomer() {
        System.out.println("inside saveupdatedCreditcardorder");

        //persist
        cca.setCardStatus(CardAccountStatus.NEW);
        cardAcctSessionBean.createCardAccount(cca);

        cco.setApplicationStatus(ApplicationStatus.NEW);
        creditCardOrderSessionBean.createCardOrder(cco);

        cca.setMainAccount(existingCustomer.getMainAccount());
        cca.setCreditCardProduct(newCardProductSessionBean.getCreditCardProductByProductName(selectedProductName));
        cca.setCreditCardOrder(cco);
        cardAcctSessionBean.updateCreditCardAccount(cca);

        cco.setMainAccount(existingCustomer.getMainAccount());
        cco.setCreditCardAccount(cca);
        creditCardOrderSessionBean.updateCreditCardOrder(cco);

        emailServiceSessionBean.sendCreditCardApplicationNotice(existingCustomer.getEmail());
        RedirectUtils.redirect("https://localhost:8181/InternetBankingSystem/personal_cards/credit_card_summary.xhtml");

    }

    public void setExistingCustomerForCardApplication() {
        existingCustomer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
    }

    public CreditCardOrder getCco() {
        return cco;
    }

    public void setCco(CreditCardOrder cco) {
        this.cco = cco;
    }

    public String getSelectedResidentialStatus() {
        return selectedResidentialStatus;
    }

    public void setSelectedResidentialStatus(String selectedResidentialStatus) {
        this.selectedResidentialStatus = selectedResidentialStatus;
    }

    public String getSelectedResidentialType() {
        return selectedResidentialType;
    }

    public void setSelectedResidentialType(String selectedResidentialType) {
        this.selectedResidentialType = selectedResidentialType;
    }

    public String getSelectedEmploymentStatus() {
        return selectedEmploymentStatus;
    }

    public void setSelectedEmploymentStatus(String selectedEmploymentStatus) {
        this.selectedEmploymentStatus = selectedEmploymentStatus;
    }

    public String getSelectedEducation() {
        return selectedEducation;
    }

    public void setSelectedEducation(String selectedEducation) {
        this.selectedEducation = selectedEducation;
    }

    public String getSelectedIndustry() {
        return selectedIndustry;
    }

    public void setSelectedIndustry(String selectedIndustry) {
        this.selectedIndustry = selectedIndustry;
    }

    public CardAcctSessionBeanLocal getCardAcctSessionBean() {
        return cardAcctSessionBean;
    }

    public void setCardAcctSessionBean(CardAcctSessionBeanLocal cardAcctSessionBean) {
        this.cardAcctSessionBean = cardAcctSessionBean;
    }

    public String getSelectedIdentityType() {
        return selectedIdentityType;
    }

    public void setSelectedIdentityType(String selectedIdentityType) {
        this.selectedIdentityType = selectedIdentityType;
    }

    public String getSelectedNationality() {
        return selectedNationality;
    }

    public void setSelectedNationality(String selectedNationality) {
        this.selectedNationality = selectedNationality;
    }

    public String getSelectedGender() {
        return selectedGender;
    }

    public void setSelectedGender(String selectedGender) {
        this.selectedGender = selectedGender;
    }

    public String getSelectedIncome() {
        return selectedIncome;
    }

    public void setSelectedIncome(String selectedIncome) {
        this.selectedIncome = selectedIncome;
    }

    public String getSelectedProductName() {
        return selectedProductName;
    }

    public void setSelectedProductName(String selectedProductName) {
        this.selectedProductName = selectedProductName;
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

    public CreditCardAccount getCca() {
        return cca;
    }

    public void setCca(CreditCardAccount cca) {
        this.cca = cca;
    }

    public MainAccount getMa() {
        return ma;
    }

    public void setMa(MainAccount ma) {
        this.ma = ma;
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

    public Customer getExistingCustomer() {
        return existingCustomer;
    }

    public void setExistingCustomer(Customer existingCustomer) {
        this.existingCustomer = existingCustomer;
    }

    public List<String> getProductNameOptions() {
        return productNameOptions;
    }

    /**
     * @param productNameOptions the productNameOptions to set
     */
    public void setProductNameOptions(List<String> productNameOptions) {
        this.productNameOptions = productNameOptions;
    }

    /**
     * @return the residentialStatusOptions
     */
    public List<String> getResidentialStatusOptions() {
        return residentialStatusOptions;
    }

    /**
     * @param residentialStatusOptions the residentialStatusOptions to set
     */
    public void setResidentialStatusOptions(List<String> residentialStatusOptions) {
        this.residentialStatusOptions = residentialStatusOptions;
    }

    /**
     * @return the residentialTypeOptions
     */
    public List<String> getResidentialTypeOptions() {
        return residentialTypeOptions;
    }

    /**
     * @param residentialTypeOptions the residentialTypeOptions to set
     */
    public void setResidentialTypeOptions(List<String> residentialTypeOptions) {
        this.residentialTypeOptions = residentialTypeOptions;
    }

    /**
     * @return the employmentStatusOptions
     */
    public List<String> getEmploymentStatusOptions() {
        return employmentStatusOptions;
    }

    /**
     * @param employmentStatusOptions the employmentStatusOptions to set
     */
    public void setEmploymentStatusOptions(List<String> employmentStatusOptions) {
        this.employmentStatusOptions = employmentStatusOptions;
    }

    /**
     * @return the industryOptions
     */
    public List<String> getIndustryOptions() {
        return industryOptions;
    }

    /**
     * @param industryOptions the industryOptions to set
     */
    public void setIndustryOptions(List<String> industryOptions) {
        this.industryOptions = industryOptions;
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
     * @return the saluationOptions
     */
    public List<String> getSaluationOptions() {
        return saluationOptions;
    }

    /**
     * @param saluationOptions the saluationOptions to set
     */
    public void setSaluationOptions(List<String> saluationOptions) {
        this.saluationOptions = saluationOptions;
    }
}
