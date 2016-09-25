/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import server.utilities.EnumUtils.*;

/**
 *
 * @author wang
 */
@Entity
public class CreditCardOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CreditCardProduct creditCardProduct;

    private Salutation saluation;
    private String firstName;
    private String lastName;
    private String nameOnCard;
    private String phone;
    private Nationality nationality;
    private Citizenship citizenship;
    private String identityNumber;
    private IdentityType identityType;
    private String email;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay;

    private double creditScore;
    private String bureaCreditScore;
    private Gender gender;
    private Income income;
    private double credit_limit;
    private MaritalStatus maritalStatus;
    private int numOfDependents;
    private String Address;
    private String postalCode;

    private ResidentialStatus residentialStatus;

    private ResidentialType residentialType;

    private Education eduLevel;

    private EmploymentStatus employmentStatus;

    private Occupation occupation;

    private Industry industry;

    private String company;
    @Lob
    private Blob cpf;
    @Lob
    private Blob identityCert; //nric or passport
    @Lob
    private Blob employmentPass;
    @Lob
    private Blob paySlip;
    @Lob
    private Blob incomeTax;
    @Lob
    private Blob otherCert; //for anything else
    private String resultNotes; //for staff to reply to customer

    private ApplicationStatus applicationStatus;

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
        if (!(object instanceof CreditCardOrder)) {
            return false;
        }
        CreditCardOrder other = (CreditCardOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.card.account.CreditCard[ id=" + id + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public double getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(double credit_limit) {
        this.credit_limit = credit_limit;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Blob getCpf() {
        return cpf;
    }

    public void setCpf(Blob cpf) {
        this.cpf = cpf;
    }

    public Blob getIdentityCert() {
        return identityCert;
    }

    public void setIdentityCert(Blob identityCert) {
        this.identityCert = identityCert;
    }

    public Blob getEmploymentPass() {
        return employmentPass;
    }

    public void setEmploymentPass(Blob employmentPass) {
        this.employmentPass = employmentPass;
    }

    public Blob getPaySlip() {
        return paySlip;
    }

    public void setPaySlip(Blob paySlip) {
        this.paySlip = paySlip;
    }

    public Blob getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(Blob incomeTax) {
        this.incomeTax = incomeTax;
    }

    public Blob getOtherCert() {
        return otherCert;
    }

    public void setOtherCert(Blob otherCert) {
        this.otherCert = otherCert;
    }

    public int getNumOfDependents() {
        return numOfDependents;
    }

    public void setNumOfDependents(int numOfDependents) {
        this.numOfDependents = numOfDependents;
    }

    public String getResultNotes() {
        return resultNotes;
    }

    public void setResultNotes(String resultNotes) {
        this.resultNotes = resultNotes;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Salutation getSaluation() {
        return saluation;
    }

    public void setSaluation(Salutation saluation) {
        this.saluation = saluation;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Citizenship getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
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

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Education getEduLevel() {
        return eduLevel;
    }

    public void setEduLevel(Education eduLevel) {
        this.eduLevel = eduLevel;
    }

    public double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(double creditScore) {
        this.creditScore = creditScore;
    }

    public String getBureaCreditScore() {
        return bureaCreditScore;
    }

    public void setBureaCreditScore(String bureaCreditScore) {
        this.bureaCreditScore = bureaCreditScore;
    }

    public CreditCardProduct getCreditCardProduct() {
        return creditCardProduct;
    }

    public void setCreditCardProduct(CreditCardProduct creditCardProduct) {
        this.creditCardProduct = creditCardProduct;
    }

}
