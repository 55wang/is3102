/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Entity
public class LoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    
    
    //personal info
    private EnumUtils.IdentityType identityType;
    private String identityNumber;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay;
    private Integer age;
    private String firstname;
    private String lastname;
    private String fullName;
    private String phone;
    private String email;
    private String Category;
    
    
    // general info
    private EnumUtils.Nationality nationality;
    private EnumUtils.MaritalStatus maritalStatus;
    private String address;
    private String postalCode;
    private EnumUtils.Industry industry;
    private EnumUtils.Education education;
    private EnumUtils.ResidentialStatus residentialStatus;
    private EnumUtils.ResidentialType residentialType;
    private EnumUtils.EmploymentStatus employmentStatus;
    private Double actualIncome = 0.0;
    private Double savingPerMonth;
    private EnumUtils.Gender gender;
    
    // loan info

    private Double otherCommitment;
    private Double requestedAmount;
    private Double marketValue;
    private Integer otherHousingLoan;
    private Integer tenure;
    private EnumUtils.LoanProductType productType;
    private EnumUtils.LoanAccountStatus status = EnumUtils.LoanAccountStatus.NEW;
    
    // mapping
    @ManyToOne(cascade = {CascadeType.MERGE})
    private StaffAccount loanOfficer;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanProduct loanProduct;
    @ManyToOne
    private MainAccount mainAccount;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoanApplication)) {
            return false;
        }
        LoanApplication other = (LoanApplication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.loan.LoanApplication[ id=" + id + " ]";
    }

    /**
     * @return the name
     */
   

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the income
     */
    

    /**
     * @return the otherCommitment
     */
    public Double getOtherCommitment() {
        return otherCommitment;
    }

    /**
     * @param otherCommitment the otherCommitment to set
     */
    public void setOtherCommitment(Double otherCommitment) {
        this.otherCommitment = otherCommitment;
    }

    /**
     * @return the otherHousingLoan
     */
    public Integer getOtherHousingLoan() {
        return otherHousingLoan;
    }

    /**
     * @param otherHousingLoan the otherHousingLoan to set
     */
    public void setOtherHousingLoan(Integer otherHousingLoan) {
        this.otherHousingLoan = otherHousingLoan;
    }

    /**
     * @return the loanOfficer
     */
    public StaffAccount getLoanOfficer() {
        return loanOfficer;
    }

    /**
     * @param loanOfficer the loanOfficer to set
     */
    public void setLoanOfficer(StaffAccount loanOfficer) {
        this.loanOfficer = loanOfficer;
    }

    /**
     * @return the productType
     */
    public EnumUtils.LoanProductType getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(EnumUtils.LoanProductType productType) {
        this.productType = productType;
    }

    /**
     * @return the requestedAmount
     */
    public Double getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * @param requestedAmount the requestedAmount to set
     */
    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    /**
     * @return the marketValue
     */
    public Double getMarketValue() {
        return marketValue;
    }

    /**
     * @param marketValue the marketValue to set
     */
    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public EnumUtils.IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(EnumUtils.IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public EnumUtils.Nationality getNationality() {
        return nationality;
    }

    public void setNationality(EnumUtils.Nationality nationality) {
        this.nationality = nationality;
    }

    public EnumUtils.MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(EnumUtils.MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public EnumUtils.Industry getIndustry() {
        return industry;
    }

    public void setIndustry(EnumUtils.Industry industry) {
        this.industry = industry;
    }

    public EnumUtils.Education getEducation() {
        return education;
    }

    public void setEducation(EnumUtils.Education education) {
        this.education = education;
    }

    public EnumUtils.ResidentialStatus getResidentialStatus() {
        return residentialStatus;
    }

    public void setResidentialStatus(EnumUtils.ResidentialStatus residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public EnumUtils.ResidentialType getResidentialType() {
        return residentialType;
    }

    public void setResidentialType(EnumUtils.ResidentialType residentialType) {
        this.residentialType = residentialType;
    }

    public EnumUtils.EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EnumUtils.EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Double getActualIncome() {
        return actualIncome;
    }

    public void setActualIncome(Double actualIncome) {
        this.actualIncome = actualIncome;
    }

    public Double getSavingPerMonth() {
        return savingPerMonth;
    }

    public void setSavingPerMonth(Double savingPerMonth) {
        this.savingPerMonth = savingPerMonth;
    }

    public EnumUtils.Gender getGender() {
        return gender;
    }

    public void setGender(EnumUtils.Gender gender) {
        this.gender = gender;
    }



    /**
     * @return the loanProduct
     */
    public LoanProduct getLoanProduct() {
        return loanProduct;
    }

    /**
     * @param loanProduct the loanProduct to set
     */
    public void setLoanProduct(LoanProduct loanProduct) {
        this.loanProduct = loanProduct;
    }

    /**
     * @return the status
     */
    public EnumUtils.LoanAccountStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(EnumUtils.LoanAccountStatus status) {
        this.status = status;
    }

    /**
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getTenure() {
        return tenure;
    }

    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    
    
    
}
