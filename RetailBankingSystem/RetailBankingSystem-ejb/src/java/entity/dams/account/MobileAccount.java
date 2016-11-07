/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.common.PayMeRequest;
import entity.common.TransactionRecord;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import server.utilities.PincodeGenerationUtils;

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
    @Column(unique = true, nullable = false)
    private String referralCode = PincodeGenerationUtils.generateRandom(false, 6);
    
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "toAccount")
    private List<PayMeRequest> receivedRequest = new ArrayList<>();
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "fromAccount")
    private List<PayMeRequest> sentRequest = new ArrayList<>();
    
    // REMARK: Depreciated, use ejb to get, easier and more accurate
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

    /**
     * @return the referralCode
     */
    public String getReferralCode() {
        return referralCode;
    }

    /**
     * @param referralCode the referralCode to set
     */
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    /**
     * @return the receivedRequest
     */
    public List<PayMeRequest> getReceivedRequest() {
        return receivedRequest;
    }

    /**
     * @param receivedRequest the receivedRequest to set
     */
    public void setReceivedRequest(List<PayMeRequest> receivedRequest) {
        this.receivedRequest = receivedRequest;
    }

    /**
     * @return the sentRequest
     */
    public List<PayMeRequest> getSentRequest() {
        return sentRequest;
    }

    /**
     * @param sentRequest the sentRequest to set
     */
    public void setSentRequest(List<PayMeRequest> sentRequest) {
        this.sentRequest = sentRequest;
    }
}
