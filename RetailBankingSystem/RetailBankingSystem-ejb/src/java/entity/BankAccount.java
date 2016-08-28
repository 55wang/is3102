/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author leiyang
 */
@Entity
public abstract class BankAccount implements Serializable {
    public enum AccountType {
        CURRENT {
            public String toString() {
                return "CURRENT";
            }
        },
        SAVING {
            public String toString() {
                return "SAVING";
            }
        },
        FIXED {
            public String toString() {
                return "FIXED";
            }
        },
        MOBILE {
            public String toString() {
                return "MOBILE";
            }
        },
        LOAN {
            public String toString() {
                return "LOAN";
            }
        }
    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// generated
    private String name;
    private String type; // CURRENT, SAVING, FIXED, MOBILE, LOAN
    @Column(precision=12, scale=2)
    private BigDecimal balance = new BigDecimal(0);
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Interest> rules = new ArrayList<Interest>();
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "fromAccount")
    private List<Transaction> transactions = new ArrayList<Transaction>();
    @ManyToOne(cascade = CascadeType.PERSIST)
    private MainAccount mainAccount = new MainAccount();
    // TODO: Other likely fields
    // billing address 
    // transaction history
    // payment history
    // Cheque
    
    public void addInterestRules(Interest i) {
        rules.add(i);
    }
    
    public void removeInterestRules(Interest i) {
        rules.remove(i);
    }
    
    public void addInterestsRules(List<Interest> interests) {
        rules.addAll(interests);
    }
    
    public void removeInterestsRules(List<Interest> interests) {
        rules.removeAll(interests);
    }
    
    public void addBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    public void removeBalance(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
    
    public void addTransaction(Transaction t) {
        transactions.add(t);
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

    /**
     * @return the rules
     */
    public List<Interest> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(List<Interest> rules) {
        this.rules = rules;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

}
