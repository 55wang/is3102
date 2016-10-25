/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    
    // info
    private String name;
    private String idNumber;
    private String email;
    private String phone;
    private Integer age;
    private Double income;
    private Double otherCommitment;
    private Double requestedAmount;
    private Double marketValue;
    private Integer otherHousingLoan;
    private EnumUtils.LoanProductType productType;
    
    // mapping
    @ManyToOne(cascade = {CascadeType.MERGE})
    private StaffAccount loanOfficer;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanProduct loanProduct;
    
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
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

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
    public Double getIncome() {
        return income;
    }

    /**
     * @param income the income to set
     */
    public void setIncome(Double income) {
        this.income = income;
    }

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

    /**
     * @return the idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber the idNumber to set
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
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
    
}
