/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

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
public class LoanInterest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private Integer version = 0;
    private Boolean isHistory = Boolean.FALSE;
    // means ith month to jth month
    private Integer startMonth; // 1 - 12, 13 - 24, 25- 36, 37 - -1
    private Integer endMonth;
    // annual rate
    private Double interestRate;
    private Boolean fhr18 = false;
    private EnumUtils.LoanProductType productType;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanInterestCollection loanInterestCollection;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanExternalInterest loanExternalInterest;// not used by personal loan and car loan
    
    // getter and setter

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * @return the loanInterestCollection
     */
    public LoanInterestCollection getLoanInterestCollection() {
        return loanInterestCollection;
    }

    /**
     * @param loanInterestCollection the loanInterestCollection to set
     */
    public void setLoanInterestCollection(LoanInterestCollection loanInterestCollection) {
        this.loanInterestCollection = loanInterestCollection;
    }

    /**
     * @return the startMonth
     */
    public Integer getStartMonth() {
        return startMonth;
    }

    /**
     * @param startMonth the startMonth to set
     */
    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * @return the endMonth
     */
    public Integer getEndMonth() {
        return endMonth;
    }

    /**
     * @param endMonth the endMonth to set
     */
    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
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
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the isHistory
     */
    public Boolean getIsHistory() {
        return isHistory;
    }

    /**
     * @param isHistory the isHistory to set
     */
    public void setIsHistory(Boolean isHistory) {
        this.isHistory = isHistory;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the loanExternalInterest
     */
    public LoanExternalInterest getLoanExternalInterest() {
        return loanExternalInterest;
    }

    /**
     * @param loanExternalInterest the loanExternalInterest to set
     */
    public void setLoanExternalInterest(LoanExternalInterest loanExternalInterest) {
        this.loanExternalInterest = loanExternalInterest;
    }

    /**
     * @return the fhr18
     */
    public Boolean getFhr18() {
        return fhr18;
    }

    /**
     * @param fhr18 the fhr18 to set
     */
    public void setFhr18(Boolean fhr18) {
        this.fhr18 = fhr18;
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

}
