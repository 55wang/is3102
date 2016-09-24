/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import entity.card.account.CreditCardOrder;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.*;
import utils.CommonUtils;

/**
 *
 * @author wang
 */
@Named(value = "newCardManagedBean")
@ViewScoped
public class NewCardManagedBean implements Serializable {

    private CreditCardOrder cco = new CreditCardOrder();
    private List<String> CreditTypeOptions = CommonUtils.getEnumList(CreditType.class);
    private List<String> ResidentialStatusOptions = CommonUtils.getEnumList(ResidentialStatus.class);
    private List<String> ResidentialTypeOptions = CommonUtils.getEnumList(ResidentialType.class);

    private List<String> EmploymentStatusOptions = CommonUtils.getEnumList(EmploymentStatus.class);
    private List<String> IndustryOptions = CommonUtils.getEnumList(Industry.class);
    private List<String> StatusOptions = CommonUtils.getEnumList(StatusType.class);
    private List<String> GenderOptions = CommonUtils.getEnumList(Gender.class);
    private List<String> NationalityOptions = CommonUtils.getEnumList(Nationality.class);
    private List<String> IdentityTypeOptions = CommonUtils.getEnumList(IdentityType.class);
    private List<String> CitizenOptions = CommonUtils.getEnumList(Citizenship.class);
    private List<String> OccupationOptions = CommonUtils.getEnumList(Occupation.class);
    private List<String> IncomeOptions = CommonUtils.getEnumList(Income.class);
    private List<String> MaritalStatusOptions = CommonUtils.getEnumList(MaritalStatus.class);
    private List<String> EducationOptions = CommonUtils.getEnumList(Education.class);

    private String selectedCreditType;
    private String selectedResidentialStatus;
    private String selectedResidentialType;
    private String selectedEmploymentStatus;
    private String selectedEducation;
    private String selectedOccupation;
    private String selectedIndustry;
    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;

    public NewCardManagedBean() {
    }

    public void saveUpdatedCreditCardOrder() {
        getCco().setCreditType(CreditType.getEnum(getSelectedCreditType()));
        getCco().setResidentialStatus(ResidentialStatus.getEnum(getSelectedResidentialStatus()));
        //continue from here
        
    }

    public CreditCardOrder getCco() {
        return cco;
    }

    public void setCco(CreditCardOrder cco) {
        this.cco = cco;
    }

    public List<String> getCreditTypeOptions() {
        return CreditTypeOptions;
    }

    public void setCreditTypeOptions(List<String> CreditTypeOptions) {
        this.CreditTypeOptions = CreditTypeOptions;
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

    public List<String> getCitizenOptions() {
        return CitizenOptions;
    }

    public void setCitizenOptions(List<String> CitizenOptions) {
        this.CitizenOptions = CitizenOptions;
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

    public String getSelectedCreditType() {
        return selectedCreditType;
    }

    public void setSelectedCreditType(String selectedCreditType) {
        this.selectedCreditType = selectedCreditType;
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
}
