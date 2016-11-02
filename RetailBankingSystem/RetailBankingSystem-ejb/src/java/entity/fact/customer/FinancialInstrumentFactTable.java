/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.fact.customer;

import entity.wealth.FinancialInstrument;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wang
 */
@Entity
public class FinancialInstrumentFactTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(value = TemporalType.DATE)
    private Date creationDate;

    private Double instrumentValueChange;

    @ManyToOne(cascade = CascadeType.MERGE)
    private FinancialInstrument fi;

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
        if (!(object instanceof FinancialInstrumentFactTable)) {
            return false;
        }
        FinancialInstrumentFactTable other = (FinancialInstrumentFactTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.fact.customer.FinancialInstrumentFactTable[ id=" + id + " ]";
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public FinancialInstrument getFi() {
        return fi;
    }

    public void setFi(FinancialInstrument fi) {
        this.fi = fi;
    }

    public Double getInstrumentValueChange() {
        return instrumentValueChange;
    }

    public void setInstrumentValueChange(Double instrumentValueChange) {
        this.instrumentValueChange = instrumentValueChange;
    }

}
