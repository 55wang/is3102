/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import utils.EnumUtils.DepositAccountType;

/**
 *
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class DepositProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer version = 0;
    private Boolean isHistory;
    private String description;
    @Column(unique = true, nullable = false)
    private String name;
    private String terms;
    private Integer interestInterval = 1;
    private DepositAccountType type;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();

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
        if (!(object instanceof DepositProduct)) {
            return false;
        }
        DepositProduct other = (DepositProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.dams.account.DepositProduct[ id=" + id + " ]";
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the terms
     */
    public String getTerms() {
        return terms;
    }

    /**
     * @param terms the terms to set
     */
    public void setTerms(String terms) {
        this.terms = terms;
    }

    /**
     * @return the type
     */
    public DepositAccountType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(DepositAccountType type) {
        this.type = type;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the interestInterval
     */
    public Integer getInterestInterval() {
        return interestInterval;
    }

    /**
     * @param interestInterval the interestInterval to set
     */
    public void setInterestInterval(Integer interestInterval) {
        this.interestInterval = interestInterval;
    }
    
}
