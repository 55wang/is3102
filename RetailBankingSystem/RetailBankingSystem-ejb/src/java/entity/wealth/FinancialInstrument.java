/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.wealth;

import entity.fact.customer.FinancialInstrumentFactTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import static server.utilities.CommonUtils.round;
import server.utilities.EnumUtils.FinancialInstrumentClass;

/**
 *
 * @author VIN-S
 */
@Entity
public class FinancialInstrument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private FinancialInstrumentClass name;
    private String description;
    private Double standardDeviation;
    private Double expectedReturn;
    private String ETFName;
    private String ETFLink;
    
    @OneToMany(mappedBy = "fi")
    private List<FinancialInstrumentFactTable> fif = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FinancialInstrument other = (FinancialInstrument) obj;
        if (this.name != other.name) {
            return false;
        }
        return true;
    }

    public FinancialInstrumentClass getName() {
        return name;
    }

    public void setName(FinancialInstrumentClass name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getStandardDeviation() {
        return round(standardDeviation,3);
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Double getExpectedReturn() {
        return round(expectedReturn,3);
    }

    public void setExpectedReturn(Double expectedReturn) {
        this.expectedReturn = expectedReturn;
    }

    public List<FinancialInstrumentFactTable> getFif() {
        return fif;
    }

    public void setFif(List<FinancialInstrumentFactTable> fif) {
        this.fif = fif;
    }

    public String getETFName() {
        return ETFName;
    }

    public void setETFName(String ETFName) {
        this.ETFName = ETFName;
    }

    public String getETFLink() {
        return ETFLink;
    }

    public void setETFLink(String ETFLink) {
        this.ETFLink = ETFLink;
    }

}
