/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.staff;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author leiyang
 */
@Entity
public class ServiceCharge implements Serializable {
    
    @Id
    private String name;
    
    // info
    @Column(precision=19, scale=4)
    private BigDecimal charges = BigDecimal.ZERO;

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
     * @return the charges
     */
    public BigDecimal getCharges() {
        return charges;
    }

    /**
     * @param charges the charges to set
     */
    public void setCharges(BigDecimal charges) {
        this.charges = charges;
    }
}
