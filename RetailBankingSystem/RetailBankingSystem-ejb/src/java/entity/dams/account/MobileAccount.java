/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.common.TransactionRecord;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class MobileAccount extends DepositAccount {
    
    // info
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    private String password;
    
    // function
    public TransactionRecord getLatestTransaction() {
        System.out.println("Current Account number of transactions: " + this.getTransactions().size());
        TransactionRecord transaction = this.getTransactions().size() > 0 ? this.getTransactions().get(0) : null;
        for (TransactionRecord t : this.getTransactions()) {
            if (t.getCreationDate().compareTo(transaction.getCreationDate()) > 0) {
                transaction = t;
            }
        }
        return transaction;
    }
    
    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
