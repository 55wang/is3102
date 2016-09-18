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
import javax.persistence.Temporal;

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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getNumOfDependents() {
        return numOfDependents;
    }

    public void setNumOfDependents(int numOfDependents) {
        this.numOfDependents = numOfDependents;
    }

    public enum salutation {

        DR {
                    @Override
                    public String toString() {
                        return "DR";
                    }
                },
        MDM {
                    @Override
                    public String toString() {
                        return "MDM";
                    }
                },
        MR {
                    @Override
                    public String toString() {
                        return "MR";
                    }
                },
        MRS {
                    @Override
                    public String toString() {
                        return "MRS";
                    }
                },
        MS {
                    @Override
                    public String toString() {
                        return "MS";
                    }
                }
    }

    private String firstName;
    private String lastName;
    private String nameOnCard;
    private String phone;
    private String nationality;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay;

    public enum gender {

        MALE {
                    @Override
                    public String toString() {
                        return "MALE";
                    }
                },
        FEMALE {
                    @Override
                    public String toString() {
                        return "FEMALE";
                    }
                }
    }

    private double income;
    private double credit_limit;

    public enum martialStatus {

        SINGLE {
                    @Override
                    public String toString() {
                        return "SINGLE";
                    }
                },
        MARRIED {
                    @Override
                    public String toString() {
                        return "MARRIED";
                    }
                },
        DIVORCED {
                    @Override
                    public String toString() {
                        return "DIVORCED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                }
    }

    private int numOfDependents;
    private String Address;

    public enum residentialStatus {

        CONDO_APART {
                    @Override
                    public String toString() {
                        return "CONDO_APART";
                    }
                },
        HDB {
                    @Override
                    public String toString() {
                        return "HDB";
                    }
                },
        LANDED {
                    @Override
                    public String toString() {
                        return "LANDED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                },
    }

    public enum residentialType {

        EMPLOYER {
                    @Override
                    public String toString() {
                        return "EMPLOYER";
                    }
                },
        MORTGAGED {
                    @Override
                    public String toString() {
                        return "MORTGAGED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                },
        PARENTS {
                    @Override
                    public String toString() {
                        return "PARENTS";
                    }
                },
        RENTED {
                    @Override
                    public String toString() {
                        return "RENTED";
                    }
                },
        SELF_OWNED {
                    @Override
                    public String toString() {
                        return "SELF_OWNED";
                    }
                }
    }

    public enum eduLevel {

        UNIVERSITY_GRAD {
                    @Override
                    public String toString() {
                        return "UNIVERSITY_GRAD";
                    }
                },
        DIPLOMA_HOLDER {
                    @Override
                    public String toString() {
                        return "DIPLOMA_HOLDER";
                    }
                },
        TECHNICAL {
                    @Override
                    public String toString() {
                        return "TECHNICAL";
                    }
                },
        A_LEVEL {
                    @Override
                    public String toString() {
                        return "A_LEVEL";
                    }
                },
        SECONDARY {
                    @Override
                    public String toString() {
                        return "SECONDARY";
                    }
                },
        PRIMARY {
                    @Override
                    public String toString() {
                        return "PRIMARY";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                }
    }

    public enum employmentStatus {

        VARIABLE_COMMISSION {
                    @Override
                    public String toString() {
                        return "VARIABLE_COMMISSION";
                    }
                },
        EMPLOYEE {
                    @Override
                    public String toString() {
                        return "EMPLOYEE";
                    }
                },
        SELF_EMPLOYED {
                    @Override
                    public String toString() {
                        return "SELF_EMPLOYED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                }

    }
    private String occupation;

    public enum industry {

        BUILDING_CONSTRUCTION {
                    @Override
                    public String toString() {
                        return "BUILDING_CONSTRUCTION";
                    }
                },
        BANKING_FINANCE {
                    @Override
                    public String toString() {
                        return "BANKING_FINANCE";
                    }
                },
        IT_TELCO {
                    @Override
                    public String toString() {
                        return "IT_TELCO";
                    }
                },
        GOVERNMENT {
                    @Override
                    public String toString() {
                        return "GOVERNMENT";
                    }
                },
        MANUFACTURING {
                    @Override
                    public String toString() {
                        return "MANUFACTURING";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                },
        SHIPPING_TRANSPORT {
                    @Override
                    public String toString() {
                        return "SHIPPING_TRANSPORT";
                    }
                },
        ENTERTAINMENT {
                    @Override
                    public String toString() {
                        return "ENTERTAINMENT";
                    }
                },
        HOTEL_RESTAURANT {
                    @Override
                    public String toString() {
                        return "HOTEL_RESTAURANT";
                    }
                },
        RETAIL {
                    @Override
                    public String toString() {
                        return "RETAIL";
                    }
                },
        TRAVEL_RELATED {
                    @Override
                    public String toString() {
                        return "TRAVEL_RELATED";
                    }
                }
    }

    public enum position {

        SENIOR_MANAGEMENT {
                    @Override
                    public String toString() {
                        return "SENIOR_MANAGEMENT";
                    }
                },
        PROFESSIONAL {
                    @Override
                    public String toString() {
                        return "PROFESSIONAL";
                    }
                },
        MANAGERIAL {
                    @Override
                    public String toString() {
                        return "MANAGERIAL";
                    }
                },
        EXECUTIVE {
                    @Override
                    public String toString() {
                        return "EXECUTIVE";
                    }
                },
        SALES {
                    @Override
                    public String toString() {
                        return "SALES";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                },
        DIRECTOR {
                    @Override
                    public String toString() {
                        return "DIRECTOR";
                    }
                },
        SUPERVISOR {
                    @Override
                    public String toString() {
                        return "SUPERVISOR";
                    }
                },
        TEACHER_LECTURER {
                    @Override
                    public String toString() {
                        return "TEACHER_LECTURER";
                    }
                },
        DILPOMAT {
                    @Override
                    public String toString() {
                        return "DILPOMAT";
                    }
                }
    }
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

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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
}
