/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.dams.rules.Interest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import utils.EnumUtils.DepositAccountType;

/**
 *
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class DepositAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// TODO: Need to generate our own account number
    private DepositAccountType type;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    @Column(precision=12, scale=2)
    private BigDecimal balance = new BigDecimal(0);
    @ManyToOne(cascade = CascadeType.PERSIST)
    private MainAccount mainAccount = new MainAccount();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromAccount")
    private List<TransactionRecord> transactions = new ArrayList<>();
    @ManyToOne(cascade = {CascadeType.MERGE})
    private DepositProduct product;
    
    public void addBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    public void removeBalance(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
    
    public void addTransaction(TransactionRecord t) {
        getTransactions().add(t);
    }
    
    public void addInterestsRules(List<Interest> interests) {
        interests.addAll(interests);
    }
    
    public void removeInterestsRules(List<Interest> interests) {
        interests.removeAll(interests);
    }
    
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
        if (!(object instanceof DepositAccount)) {
            return false;
        }
        DepositAccount other = (DepositAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.dams.account.DepositAccount[ id=" + id + " ]";
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
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    /**
     * @return the transactions
     */
    public List<TransactionRecord> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<TransactionRecord> transactions) {
        this.transactions = transactions;
    }

    /**
     * @return the product
     */
    public DepositProduct getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(DepositProduct product) {
        this.product = product;
    }
}
