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
import javax.persistence.OneToOne;

/**
 *
 * @author leiyang
 */
@Entity
public class LoanInterestCollection implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    private Integer version = 0;
    private Boolean isHistory = Boolean.FALSE;
    
    @OneToOne(cascade = {CascadeType.MERGE})
    private LoanProduct loanProduct;
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "loanInterestCollection")
    private List<LoanInterest> loanInterests = new ArrayList<>();
    
    public void addLoanInterest(LoanInterest li) {
        loanInterests.add(li);
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
        if (!(object instanceof LoanInterestCollection)) {
            return false;
        }
        LoanInterestCollection other = (LoanInterestCollection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.loan.LoanInterestCollection[ id=" + id + " ]";
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
     * @return the loanInterests
     */
    public List<LoanInterest> getLoanInterests() {
        return loanInterests;
    }

    /**
     * @param loanInterests the loanInterests to set
     */
    public void setLoanInterests(List<LoanInterest> loanInterests) {
        this.loanInterests = loanInterests;
    }
    
}
