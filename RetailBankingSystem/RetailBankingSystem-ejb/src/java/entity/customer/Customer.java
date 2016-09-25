/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import entity.card.account.CreditCardOrder;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.OneToOne;
import server.utilities.EnumUtils.Citizenship;
import server.utilities.EnumUtils.Education;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.MaritalStatus;
import server.utilities.EnumUtils.Nationality;
import server.utilities.EnumUtils.Occupation;

/**
 *
 * @author VIN-S
 */
@Entity
public class Customer implements Serializable {

    public Customer () {
        
    }
    
    public Customer (CreditCardOrder cco) {
        this.income = cco.getIncome();
        this.firstname = cco.getFirstName();
        this.lastname = cco.getLastName();
        this.email = cco.getEmail();
        this.maritalStatus = cco.getMaritalStatus();
        this.address = cco.getAddress();
        this.education = cco.getEduLevel();
        this.identityNumber = cco.getIdentityNumber();
        this.identityType = cco.getIdentityType();
        this.phone = cco.getPhone();
        this.birthDay = cco.getBirthDay();
        this.gender = cco.getGender();
        this.postalCode = cco.getPostalCode();
        this.occupation = cco.getOccupation();
        this.nationality = cco.getNationality();
        // copy all from cco
    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private IdentityType identityType;

    private String identityNumber;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay;

    private String firstname;

    private String lastname;

    private String address;

    private String postalCode;
    // TODO: Need to make this a unique attributes
    private String email;

    private String phone;

    private Occupation occupation;

    private Income income;

    private Nationality nationality;

    private Gender gender;

    private double creditScore; //0-1000

    private MaritalStatus maritalStatus;

    private Citizenship citizenship;

    private Education education;

    @OneToOne(cascade = {CascadeType.PERSIST})
    private MainAccount mainAccount;
    
    public String getFullName() {
        return this.getFirstname()+ " " + this.getLastname();
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

    public double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(double creditScore) {
        this.creditScore = creditScore;
    }




    /**
     * @return the identityType
     */
    public IdentityType getIdentityType() {
        return identityType;
    }

    /**
     * @param identityType the identityType to set
     */
    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
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
     * @return the citizenship
     */
    public Citizenship getCitizenship() {
        return citizenship;
    }

    /**
     * @param citizenship the citizenship to set
     */
    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
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
     * @return the occupation
     */
    public Occupation getOccupation() {
        return occupation;
    }

    /**
     * @param occupation the occupation to set
     */
    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
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


}
