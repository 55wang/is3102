/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.product;

import entity.card.account.CreditCardAccount;
import entity.card.product.PromoProduct;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author wang
 */
@Entity
public class PromoCode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // info
    @Column(unique = true, nullable = false)
    private String promotionCode;

    // mapping
    @ManyToOne(cascade = {CascadeType.MERGE})
    private PromoProduct product;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private CreditCardAccount creditCardAccount;

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
        if (!(object instanceof PromoCode)) {
            return false;
        }
        PromoCode other = (PromoCode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.card.account.PromoCode[ id=" + id + " ]";
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    /**
     * @return the product
     */
    public PromoProduct getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(PromoProduct product) {
        this.product = product;
    }

    /**
     * @return the creditCardAccount
     */
    public CreditCardAccount getCreditCardAccount() {
        return creditCardAccount;
    }

    /**
     * @param creditCardAccount the creditCardAccount to set
     */
    public void setCreditCardAccount(CreditCardAccount creditCardAccount) {
        this.creditCardAccount = creditCardAccount;
    }

}
