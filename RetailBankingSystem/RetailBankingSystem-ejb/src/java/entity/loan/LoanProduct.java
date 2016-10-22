/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import server.utilities.EnumUtils.LoanProductType;

/**
 *
 * @author leiyang
 */
@Entity
public class LoanProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LoanProductType productType;
    @Column(unique = true, nullable = false)
    private String productName;
    //lockInDuration unit is month
    private Integer lockInDuration;
    //tenure unit is month
    private Integer tenure;
    //penalty rate is annual rate
    private Double penaltyInterestRate;
    //loan interest is annual rate 
    @OneToOne(cascade = {CascadeType.MERGE}, mappedBy = "loanProduct")
    private LoanInterestCollection loanInterestCollection;
    @OneToOne(cascade = {CascadeType.MERGE}, mappedBy = "loanProduct")
    private LoanExternalInterest loanExternalInterest;// not used by personal loan and car loan

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoanProduct)) {
            return false;
        }
        LoanProduct other = (LoanProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.loan.LoanProduct[ id=" + id + " ]";
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getLockInDuration() {
        return lockInDuration;
    }

    public void setLockInDuration(Integer lockInDuration) {
        this.lockInDuration = lockInDuration;
    }

    public Integer getTenure() {
        return tenure;
    }

    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    public Double getPenaltyInterestRate() {
        return penaltyInterestRate;
    }

    public void setPenaltyInterestRate(Double penaltyInterestRate) {
        this.penaltyInterestRate = penaltyInterestRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
     * @return the productType
     */
    public LoanProductType getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(LoanProductType productType) {
        this.productType = productType;
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

}
