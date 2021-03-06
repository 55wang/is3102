/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import server.utilities.EnumUtils.CreditCardType;
import server.utilities.EnumUtils.PromoType;

/**
 *
 * @author leiyang
 */
@Entity
public class PromoProduct implements Serializable {

    // info
    @Id
    private String name;
    private String description;
    private PromoType type;
    private Double points;
    @Column(length = 4000)
    private String terms;
    private CreditCardType cardType;

    // Amount
    // retrieve rule
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
     * @return the type
     */
    public PromoType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(PromoType type) {
        this.type = type;
    }

    /**
     * @return the points
     */
    public Double getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(Double points) {
        this.points = points;
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
    
        
    public CreditCardType getCardType() {
        return cardType;
    }

    public void setCardType(CreditCardType cardType) {
        this.cardType = cardType;
    }

}
