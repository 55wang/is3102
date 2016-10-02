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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    //lockInDuration unit is month
    private Integer lockInDuration;
    private Integer tenure;
    private double penaltyInterestRate;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "loanProduct")
    private List<LoanInterest> loanInterests = new ArrayList<>();

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

    public double getPenaltyInterestRate() {
        return penaltyInterestRate;
    }

    public void setPenaltyInterestRate(double penaltyInterestRate) {
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
    
}
