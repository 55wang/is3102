/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.wealth;

import java.io.Serializable;
import java.util.Date;
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
public class MovingAverage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Temporal(value = TemporalType.DATE)
    private Date creationDate;
    
    @ManyToOne
    private Portfolio portfolio;
    
    private Double avgValue;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Double getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(Double avgValue) {
        this.avgValue = avgValue;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
