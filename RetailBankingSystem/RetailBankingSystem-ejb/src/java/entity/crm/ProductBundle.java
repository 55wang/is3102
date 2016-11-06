/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.crm;

import entity.card.product.CreditCardProduct;
import entity.dams.account.DepositProduct;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author wang
 */
@Entity
public class ProductBundle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private DepositProduct dp;
    @OneToOne
    private CreditCardProduct ccp;
    // @OneToOne
    // private LoanProduct lp;
    
    
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
        if (!(object instanceof ProductBundle)) {
            return false;
        }
        ProductBundle other = (ProductBundle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.crm.ProductBundle[ id=" + id + " ]";
    }

    public DepositProduct getDp() {
        return dp;
    }

    public void setDp(DepositProduct dp) {
        this.dp = dp;
    }

    public CreditCardProduct getCcp() {
        return ccp;
    }

    public void setCcp(CreditCardProduct ccp) {
        this.ccp = ccp;
    }
    
}
