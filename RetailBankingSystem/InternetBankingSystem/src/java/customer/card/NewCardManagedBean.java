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
import utils.CommonUtils;
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

    private List<String> ProductNameOptions = new ArrayList<>();

    private List<String> ResidentialStatusOptions = CommonUtils.getEnumList(ResidentialStatus.class);
    private List<String> ResidentialTypeOptions = CommonUtils.getEnumList(ResidentialType.class);

    private List<String> EmploymentStatusOptions = CommonUtils.getEnumList(EmploymentStatus.class);
    private List<String> IndustryOptions = CommonUtils.getEnumList(Industry.class);
    private List<String> StatusOptions = CommonUtils.getEnumList(StatusType.class);
    private List<String> GenderOptions = CommonUtils.getEnumList(Gender.class);

    private List<String> OccupationOptions = CommonUtils.getEnumList(Occupation.class);
    private List<String> IncomeOptions = CommonUtils.getEnumList(Income.class);
    private List<String> MaritalStatusOptions = CommonUtils.getEnumList(MaritalStatus.class);
    private List<String> EducationOptions = CommonUtils.getEnumList(Education.class);

    private List<String> IdentityTypeOptions = CommonUtils.getEnumList(IdentityType.class);
    private List<String> NationalityOptions = CommonUtils.getEnumList(Nationality.class);
    private List<String> SaluationOptions = CommonUtils.getEnumList(Salutation.class);

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
        setMa(new MainAccount());
        setCustomer(new Customer());
        setCca(new CreditCardAccount());
        List<CreditCardProduct> ccps = newCardProductSessionBean.getListCreditCardProducts();
        for (CreditCardProduct ccp : ccps) {
            ProductNameOptions.add(ccp.getProductName());
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

        setMa(new MainAccount());
        getMa().setUserID(CommonHelper.generateUserID(customer.getIdentityType(), customer.getIdentityNumber()));
        getMa().setStatus(StatusType.NEW);
        mainAccountSessionBean.createMainAccount(getMa());

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
        creditCardOrderSessionBean.updateCreditCardOrder(cco);

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

    public List<String> getResidentialStatusOptions() {
        return ResidentialStatusOptions;
    }

    public void setResidentialStatusOptions(List<String> ResidentialStatusOptions) {
        this.ResidentialStatusOptions = ResidentialStatusOptions;
    }

    public List<String> getResidentialTypeOptions() {
        return ResidentialTypeOptions;
    }

    public void setResidentialTypeOptions(List<String> ResidentialTypeOptions) {
        this.ResidentialTypeOptions = ResidentialTypeOptions;
    }

    public List<String> getEmploymentStatusOptions() {
        return EmploymentStatusOptions;
    }

    public void setEmploymentStatusOptions(List<String> EmploymentStatusOptions) {
        this.EmploymentStatusOptions = EmploymentStatusOptions;
    }

    public List<String> getIndustryOptions() {
        return IndustryOptions;
    }

    public void setIndustryOptions(List<String> IndustryOptions) {
        this.IndustryOptions = IndustryOptions;
    }

    public List<String> getStatusOptions() {
        return StatusOptions;
    }

    public void setStatusOptions(List<String> StatusOptions) {
        this.StatusOptions = StatusOptions;
    }

    public List<String> getGenderOptions() {
        return GenderOptions;
    }

    public void setGenderOptions(List<String> GenderOptions) {
        this.GenderOptions = GenderOptions;
    }

    public List<String> getNationalityOptions() {
        return NationalityOptions;
    }

    public void setNationalityOptions(List<String> NationalityOptions) {
        this.NationalityOptions = NationalityOptions;
    }

    public List<String> getIdentityTypeOptions() {
        return IdentityTypeOptions;
    }

    public void setIdentityTypeOptions(List<String> IdentityTypeOptions) {
        this.IdentityTypeOptions = IdentityTypeOptions;
    }

    public List<String> getOccupationOptions() {
        return OccupationOptions;
    }

    public void setOccupationOptions(List<String> OccupationOptions) {
        this.OccupationOptions = OccupationOptions;
    }

    public List<String> getIncomeOptions() {
        return IncomeOptions;
    }

    public void setIncomeOptions(List<String> IncomeOptions) {
        this.IncomeOptions = IncomeOptions;
    }

    public List<String> getMaritalStatusOptions() {
        return MaritalStatusOptions;
    }

    public void setMaritalStatusOptions(List<String> MaritalStatusOptions) {
        this.MaritalStatusOptions = MaritalStatusOptions;
    }

    public List<String> getEducationOptions() {
        return EducationOptions;
    }

    public void setEducationOptions(List<String> EducationOptions) {
        this.EducationOptions = EducationOptions;
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

    public List<String> getSaluationOptions() {
        return SaluationOptions;
    }

    public void setSaluationOptions(List<String> SaluationOptions) {
        this.SaluationOptions = SaluationOptions;
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

    public List<String> getProductNameOptions() {
        return ProductNameOptions;
    }

    public void setProductNameOptions(List<String> ProductNameOptions) {
        this.ProductNameOptions = ProductNameOptions;
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

    /**
     * @return the existingCustomer
     */
    public Customer getExistingCustomer() {
        return existingCustomer;
    }

    /**
     * @param existingCustomer the existingCustomer to set
     */
    public void setExistingCustomer(Customer existingCustomer) {
        this.existingCustomer = existingCustomer;
    }


}
