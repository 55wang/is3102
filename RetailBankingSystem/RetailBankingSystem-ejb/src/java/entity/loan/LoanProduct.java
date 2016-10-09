/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author qiuxiaqing
 */
@Entity
public class LoanProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productType;
//    @Column(unique = true, nullable = false)
    private String productName;
    //lockInDuration unit is month
    private Integer lockInDuration;
    //tenure unit is month
    private Integer tenure;
    //penalty rate is annual rate
    private Double penaltyInterestRate;
    //loan interest is annual rate 
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "loanProduct")
    private List<LoanInterest> loanInterests = new ArrayList<>();

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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public List<LoanInterest> getLoanInterests() {
        return loanInterests;
    }

    public void setLoanInterests(List<LoanInterest> loanInterests) {
        this.loanInterests = loanInterests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
