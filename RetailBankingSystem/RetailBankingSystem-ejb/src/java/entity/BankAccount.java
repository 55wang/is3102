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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author leiyang
 */
@Entity
public abstract class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;// generated
    private String name;
    private String type; // CURRENT, SAVING, FIXED, MOBILE, LOAN
    private BigDecimal balance;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Interest> rules = new ArrayList<Interest>();
    // TODO: Other likely fields
    // billing address 
    // transaction history
    // payment history
    // 
    
    public void addInterestRules(Interest i) {
        rules.add(i);
    }
    public void removeInterestRules(Interest i) {
        rules.remove(i);
    }
    
    public void addBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    public void removeBalance(BigDecimal amount) {
        balance = balance.subtract(amount);
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
    
}
