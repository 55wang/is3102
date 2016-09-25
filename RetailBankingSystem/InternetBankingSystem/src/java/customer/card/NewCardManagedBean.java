/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.NewCardProductSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.card.account.CreditCardOrder;
import entity.card.account.CreditCardProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.*;
import utils.CommonUtils;
import utils.RedirectUtils;

/**
 *
 * @author wang
 */
@Named(value = "newCardManagedBean")
@ViewScoped
public class NewCardManagedBean implements Serializable {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;

    @EJB
    private NewCardProductSessionBeanLocal newCardProductSessionBean;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;

    private CreditCardOrder cco = new CreditCardOrder();
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
    private String selectedOccupation;
    private String selectedIndustry;
    private String selectedIdentityType;
    private String selectedNationality;
    private String selectedSalutation;
    private String selectedGender;
    private String selectedIncome;

    @PostConstruct
    public void retrieveProducts() {
        List<CreditCardProduct> ccps = newCardProductSessionBean.getAllCreditCardProducts();
        for (CreditCardProduct ccp : ccps) {
            ProductNameOptions.add(ccp.getProductName());
        }
    }

    public NewCardManagedBean() {
    }

    public void saveUpdatedCreditCardOrder() {
        System.out.println("inside saveupdatedCreditcardorder");

        cco.setResidentialStatus(ResidentialStatus.getEnum(getSelectedResidentialStatus()));
        cco.setResidentialType(ResidentialType.getEnum(getSelectedResidentialType()));
        cco.setEmploymentStatus(EmploymentStatus.getEnum(getSelectedEmploymentStatus()));
        cco.setEduLevel(Education.getEnum(getSelectedEducation()));
        cco.setOccupation(Occupation.getEnum(getSelectedOccupation()));
        cco.setIndustry(Industry.getEnum(getSelectedIndustry()));
        cco.setIncome(Income.getEnum(getSelectedIncome()));

        cco.setGender(Gender.getEnum(getSelectedGender()));
        cco.setIdentityType(IdentityType.getEnum(getSelectedIdentityType()));
        cco.setNationality(Nationality.getEnum(getSelectedNationality()));
        cco.setSaluation(Salutation.getEnum(getSelectedSalutation()));

        cco.setApplicationStatus(ApplicationStatus.NEW);

        cco.setCreditCardProduct(newCardProductSessionBean.getSingleCreditCardProduct(selectedProductName));
        cardAcctSessionBean.createCardOrder(cco);
        emailServiceSessionBean.sendCreditCardApplicationNotice(cco.getEmail());
        RedirectUtils.redirect("/InternetBankingSystem/common/application_success.xhtml");

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

    public String getSelectedOccupation() {
        return selectedOccupation;
    }

    public void setSelectedOccupation(String selectedOccupation) {
        this.selectedOccupation = selectedOccupation;
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

    public String getSelectedSalutation() {
        return selectedSalutation;
    }

    public void setSelectedSalutation(String selectedSalutation) {
        this.selectedSalutation = selectedSalutation;
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
}
