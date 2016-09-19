/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.customer.MainAccount;
import entity.common.Transaction;
import entity.dams.rules.DepositRule;
import entity.embedded.CumulatedInterest;
import entity.embedded.TransferLimits;
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
import utils.EnumUtils.DepositAccountType;

/**
 *  http://www.moneysense.gov.sg/Understanding-Financial-Products/Banking-and-Cash/Banking.aspx
 * @author leiyang
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
// instanceof must place at the very end
public class DepositAccount implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// TODO: Need to generate our own account number
    private String name = null;// Only Customized Account has a name
    private String description;
    private String terms;
    private DepositAccountType type;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();
    @Embedded
    private TransferLimits transferLimits = new TransferLimits();
    @Embedded
    private CumulatedInterest cumulatedInterest;
    @Column(precision=12, scale=2)
    private BigDecimal balance = new BigDecimal(0);
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "fromAccount")
    private List<Transaction> transactions = new ArrayList<>();
    
    @ManyToOne(cascade = CascadeType.MERGE)
    private MainAccount mainAccount = new MainAccount();
    
    @ManyToOne(cascade = {CascadeType.MERGE})
    private DepositRule depositRule;
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

    /**
     * @return the transferLimits
     */
    public TransferLimits getTransferLimits() {
        return transferLimits;
    }

    /**
     * @param transferLimits the transferLimits to set
     */
    public void setTransferLimits(TransferLimits transferLimits) {
        this.transferLimits = transferLimits;
    }

    /**
     * @return the depositRule
     */
    public DepositRule getDepositRule() {
        return depositRule;
    }

    /**
     * @param depositRule the depositRule to set
     */
    public void setDepositRule(DepositRule depositRule) {
        this.depositRule = depositRule;
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
}
