/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.product;

import entity.card.account.CreditCardAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import server.utilities.EnumUtils.CreditCardType;

/**
 *
 * @author wang
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CreditCardProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // info
    @Column(unique = true, nullable = false)
    private String productName;
    private Double minSpendingAmount = 0.0;
    private CreditCardType cartType;
    
    // mapping
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "creditCardProduct")
    private List<CreditCardAccount> creditCardAcounts = new ArrayList<>();

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getMinSpendingAmount() {
        return minSpendingAmount;
    }

    public void setMinSpendingAmount(Double minSpendingAmount) {
        this.minSpendingAmount = minSpendingAmount;
    }

    public List<CreditCardAccount> getCreditCardAcounts() {
        return creditCardAcounts;
    }

    public void setCreditCardAcounts(List<CreditCardAccount> creditCardAcounts) {
        this.creditCardAcounts = creditCardAcounts;
    }

    /**
     * @return the cartType
     */
    public CreditCardType getCartType() {
        return cartType;
    }

    /**
     * @param cartType the cartType to set
     */
    public void setCartType(CreditCardType cartType) {
        this.cartType = cartType;
    }

}
