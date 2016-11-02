/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.wealth;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import static server.utilities.CommonUtils.round;

/**
 *
 * @author VIN-S
 */
@Entity
public class FinancialInstrumentAndWeight implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private FinancialInstrument fi;
    private Double weight;
    private Double currentValuePerShare = 0.0;
    private Double buyingValuePerShare = 0.0;
    private Integer buyingNumberOfShare = 0;
    
    private Double tempValue;

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
        if (!(object instanceof FinancialInstrumentAndWeight)) {
            return false;
        }
        FinancialInstrumentAndWeight other = (FinancialInstrumentAndWeight) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.wealth.FinancialInstrumentAndWeight[ id=" + id + " ]";
    }

    public FinancialInstrument getFi() {
        return fi;
    }

    public void setFi(FinancialInstrument fi) {
        this.fi = fi;
    }

    public Double getWeight() {
        return round(weight, 2);
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCurrentValuePerShare() {
        return currentValuePerShare;
    }

    public void setCurrentValuePerShare(Double currentValuePerShare) {
        this.currentValuePerShare = currentValuePerShare;
    }

    public Double getBuyingValuePerShare() {
        return buyingValuePerShare;
    }

    public void setBuyingValuePerShare(Double buyingValuePerShare) {
        this.buyingValuePerShare = buyingValuePerShare;
    }

    public Integer getBuyingNumberOfShare() {
        return buyingNumberOfShare;
    }

    public void setBuyingNumberOfShare(Integer buyingNumberOfShare) {
        this.buyingNumberOfShare = buyingNumberOfShare;
    }
    
    public Double getCurrentValue() {
        return currentValuePerShare * buyingNumberOfShare;
    }

    public Double getTempValue() {
        if (tempValue==null) {
            setTempValue(getCurrentValue());
        }
        return tempValue;
    }

    public void setTempValue(Double tempValue) {
        this.tempValue = tempValue;
    }
}
