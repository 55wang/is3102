/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import entity.crm.CustomerGroup;
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
import javax.persistence.OneToOne;
import server.utilities.EnumUtils.Education;
import server.utilities.EnumUtils.EmploymentStatus;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.Industry;
import server.utilities.EnumUtils.MaritalStatus;
import server.utilities.EnumUtils.Nationality;
import server.utilities.EnumUtils.ResidentialStatus;
import server.utilities.EnumUtils.ResidentialType;

@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //personal info
    private IdentityType identityType;
    @Column(unique = true)
    private String identityNumber;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay;
    private String firstname;
    private String lastname;
    private String fullName;
    private String phone;
    @Column(unique = true)
    private String email;

    // general info
    private Nationality nationality;
    private MaritalStatus maritalStatus;
    private String address;
    private String postalCode;
    private Industry industry;
    private Education education;
    private ResidentialStatus residentialStatus;
    private ResidentialType residentialType;
    private EmploymentStatus employmentStatus;
    private Income income;
    private Double actualIncome;
    private Gender gender;

    // credit
    private Double creditScore;
    private String BureaCreditScore;

    // mapping
    @OneToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;
    @ManyToOne
    private CustomerGroup customerGroup;

    public Integer getAge() {
        Integer age = (int) (System.currentTimeMillis() - getBirthDay().getTime()) / (24 * 60 * 60 * 1000);
        return age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + id + " ]";
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public Double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Double creditScore) {
        this.creditScore = creditScore;
    }

    /**
     * @return the maritalStatus
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus the maritalStatus to set
     */
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @return the nationality
     */
    public Nationality getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the education
     */
    public Education getEducation() {
        return education;
    }

    /**
     * @param education the education to set
     */
    public void setEducation(Education education) {
        this.education = education;
    }

    /**
     * @return the income
     */
    public Income getIncome() {
        return income;
    }

    /**
     * @param income the income to set
     */
    public void setIncome(Income income) {
        this.income = income;
    }

    /**
     * @return the birthDay
     */
    public Date getBirthDay() {
        return birthDay;
    }

    /**
     * @param birthDay the birthDay to set
     */
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public ResidentialStatus getResidentialStatus() {
        return residentialStatus;
    }

    public void setResidentialStatus(ResidentialStatus residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public ResidentialType getResidentialType() {
        return residentialType;
    }

    public void setResidentialType(ResidentialType residentialType) {
        this.residentialType = residentialType;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getBureaCreditScore() {
        return BureaCreditScore;
    }

    public void setBureaCreditScore(String BureaCreditScore) {
        this.BureaCreditScore = BureaCreditScore;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public CustomerGroup getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
    }

    /**
     * @return the actualIncome
     */
    public Double getActualIncome() {
        return actualIncome;
    }

    /**
     * @param actualIncome the actualIncome to set
     */
    public void setActualIncome(Double actualIncome) {
        this.actualIncome = actualIncome;
    }
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Customer() {
    }

}
