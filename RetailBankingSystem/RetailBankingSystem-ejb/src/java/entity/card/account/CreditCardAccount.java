/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import server.utilities.EnumUtils.*;

/**
 *
 * @author wang
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
// LY: Can this be instanciated?? If cannot, use abstract class instead
// LY: What if Bank needs to create a new credit card type with attractive offers?
public class CreditCardAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private MainAccount mainAccount;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private CreditCardProduct creditCardProduct;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "creditCardAccount")
    private List<PromoCode> promoCode = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "creditCardAccount")
    private List<CardTransaction> cardTransactions = new ArrayList<>();

    private CardNetwork cardNetwork;
    private CardAccountStatus CardStatus;
    @Column(unique = true)
    private String creditCardNum;
    private Integer cvv; // LY: Use Integer instead of int
    private String nameOnCard;
    @Temporal(value = TemporalType.DATE)
    private Date validDate;
    private double transactionMonthlyLimit = 1000.0;
    private double transactionDailyLimit = 500.0;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    private double minPayDue;
    private double annualInterestRate; //24% annual interest rate
    private double outstandingAmount = 0.0;
    private double cashBackAmount;
    private double merlionMiles;
    private double merlionPoints;
    //for bad credit record
    private int numOfLatePayment; //count of total late payment
    private int numOf_30_59_LatePayment; //count of 30-59 days overdue
    private int numOf_60_89_LatePayment; //count of 60-89 days overdue
    private int numOf_90_LatePayment; //count of >= 90 days overdue

    @Temporal(value = TemporalType.DATE)
    private Date overDueDuration;
    
    public void addOutstandingAmount(double amount) {
        this.outstandingAmount += amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final CreditCardAccount other = (CreditCardAccount) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.creditCardNum, other.creditCardNum)) {
            return false;
        }
        if (!Objects.equals(this.cvv, other.cvv)) {
            return false;
        }
        if (!Objects.equals(this.nameOnCard, other.nameOnCard)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "CreditCardAccount{" + "id=" + id + ", mainAccount=" + mainAccount + ", creditCardProduct=" + creditCardProduct + ", promoCode=" + promoCode + ", cardTransactions=" + cardTransactions + ", cardNetwork=" + cardNetwork + ", CardStatus=" + CardStatus + ", creditCardNum=" + creditCardNum + ", cvv=" + cvv + ", nameOnCard=" + nameOnCard + ", validDate=" + validDate + ", transactionMonthlyLimit=" + transactionMonthlyLimit + ", transactionDailyLimit=" + transactionDailyLimit + ", creationDate=" + creationDate + ", minPayDue=" + minPayDue + ", annualInterestRate=" + annualInterestRate + ", outstandingAmount=" + outstandingAmount + ", cashBackAmount=" + cashBackAmount + ", merlionMiles=" + merlionMiles + ", merlionPoints=" + merlionPoints + ", numOfLatePayment=" + numOfLatePayment + ", numOf_30_59_LatePayment=" + numOf_30_59_LatePayment + ", numOf_60_89_LatePayment=" + numOf_60_89_LatePayment + ", numOf_90_LatePayment=" + numOf_90_LatePayment + ", overDueDuration=" + overDueDuration + '}';
    }
    
    

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getMinPayDue() {
        return minPayDue;
    }

    public void setMinPayDue(double minPayDue) {
        this.minPayDue = minPayDue;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public double getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public int getNumOfLatePayment() {
        return numOfLatePayment;
    }

    public void setNumOfLatePayment(int numOfLatePayment) {
        this.numOfLatePayment = numOfLatePayment;
    }

    public int getNumOf_30_59_LatePayment() {
        return numOf_30_59_LatePayment;
    }

    public void setNumOf_30_59_LatePayment(int numOf_30_59_LatePayment) {
        this.numOf_30_59_LatePayment = numOf_30_59_LatePayment;
    }

    public int getNumOf_60_89_LatePayment() {
        return numOf_60_89_LatePayment;
    }

    public void setNumOf_60_89_LatePayment(int numOf_60_89_LatePayment) {
        this.numOf_60_89_LatePayment = numOf_60_89_LatePayment;
    }

    public int getNumOf_90_LatePayment() {
        return numOf_90_LatePayment;
    }

    public void setNumOf_90_LatePayment(int numOf_90_LatePayment) {
        this.numOf_90_LatePayment = numOf_90_LatePayment;
    }

    public Date getOverDueDuration() {
        return overDueDuration;
    }

    public void setOverDueDuration(Date overDueDuration) {
        this.overDueDuration = overDueDuration;
    }

    public double getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(double cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }

    public double getMerlionMiles() {
        return merlionMiles;
    }

    public void setMerlionMiles(double merlionMiles) {
        this.merlionMiles = merlionMiles;
    }

    public double getMerlionPoints() {
        return merlionPoints;
    }

    public void setMerlionPoints(double merlionPoints) {
        this.merlionPoints = merlionPoints;
    }

    public CreditCardProduct getCreditCardProduct() {
        return creditCardProduct;
    }

    public void setCreditCardProduct(CreditCardProduct creditCardProduct) {
        this.creditCardProduct = creditCardProduct;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public CardAccountStatus getCardStatus() {
        return CardStatus;
    }

    public void setCardStatus(CardAccountStatus CardStatus) {
        this.CardStatus = CardStatus;
    }

    public double getTransactionDailyLimit() {
        return transactionDailyLimit;
    }

    public void setTransactionDailyLimit(double transactionDailyLimit) {
        this.transactionDailyLimit = transactionDailyLimit;
    }

    public double getTransactionMonthlyLimit() {
        return transactionMonthlyLimit;
    }

    public void setTransactionMonthlyLimit(double transactionMonthlyLimit) {
        this.transactionMonthlyLimit = transactionMonthlyLimit;
    }

    public List<PromoCode> getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(List<PromoCode> promoCode) {
        this.promoCode = promoCode;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    public CardNetwork getCardNetwork() {
        return cardNetwork;
    }

    public void setCardNetwork(CardNetwork cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public List<CardTransaction> getCardTransactions() {
        return cardTransactions;
    }

    public void setCardTransactions(List<CardTransaction> cardTransactions) {
        this.cardTransactions = cardTransactions;
    }

}
