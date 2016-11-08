/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.embedded.CumulatedInterest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils.DepositAccountType;
import server.utilities.EnumUtils.StatusType;

/**
 *
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class DepositAccount implements Serializable {
    
    @Id
    private String accountNumber;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date closeDate = new Date();
    
    // info
    private DepositAccountType type;
    private StatusType status = StatusType.PENDING;
    @Column(precision=18, scale=4)
    private BigDecimal balance = BigDecimal.ZERO;
    @Embedded
    private CumulatedInterest cumulatedInterest = new CumulatedInterest();
    
    // mapping
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromAccount")
    private List<TransactionRecord> transactions = new ArrayList<>();
    @ManyToOne(cascade = {CascadeType.MERGE})
    private DepositProduct product;
    @ManyToOne(cascade = CascadeType.MERGE)
    private MainAccount mainAccount;
    
    public void addBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    public void removeBalance(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
    
    public void addTransaction(TransactionRecord t) {
        getTransactions().add(t);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.getAccountNumber());
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
        final DepositAccount other = (DepositAccount) obj;
        return true;
    }
    
    

    @Override
    public String toString() {
        return "DepositAccount{" + "accountNumber=" + getAccountNumber() + ", type=" + type + ", status=" + status + ", creationDate=" + creationDate + ", balance=" + balance + ", mainAccount=" + mainAccount + '}';
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
     * @return the status
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }
}
