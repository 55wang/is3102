/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.customer.MainAccount;
import entity.common.Transaction;
import entity.embedded.CumulatedInterest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

/**
 *  http://www.moneysense.gov.sg/Understanding-Financial-Products/Banking-and-Cash/Banking.aspx
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class DepositAccount implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// TODO: Need to generate our own account number
    private String name;
    private String type; // CURRENT, SAVING, FIXED, MOBILE, LOAN
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    @Embedded
    private CumulatedInterest cumulatedInterest;
    @Column(precision=12, scale=2)
    private BigDecimal balance = new BigDecimal(0);
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "fromAccount")
    private List<Transaction> transactions = new ArrayList<>();
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private MainAccount mainAccount = new MainAccount();
    // TODO: Other likely fields
    // billing address 
    // transaction history
    // payment history
    // Cheque
    
    public void addBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    public void removeBalance(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
    
    public void addTransaction(Transaction t) {
        getTransactions().add(t);
    }
    
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }
    /**
     * @return the cumulatedInterest
     */
    public CumulatedInterest getCumulatedInterest() {
        return cumulatedInterest;
    }

    /**
     * @param cumulatedInterest the cumulatedInterest to set
     */
    public void setCumulatedInterest(CumulatedInterest cumulatedInterest) {
        this.cumulatedInterest = cumulatedInterest;
    }
    
    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }
    
    /**
     * @return the transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
