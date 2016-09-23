/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import entity.customer.MainAccount;
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
import server.utilities.EnumUtils.CardAccountStatus;

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

    private CardAccountStatus CardStatus;
    @Column(precision = 12)// LY:credit card number in decimal??
    private BigDecimal creditCardNum;
    private Integer cvv; // LY: Use Integer instead of int
    private String nameOnCard;
    @Temporal(value = TemporalType.DATE)
    private Date validDate;
    private double transactionMonthlyLimit;
    private double transactionDailyLimit;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    private double minPayDue;
    private double annualInterestRate; //24% annual interest rate
    private double outstandingAmount;
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
        if (!(object instanceof CreditCardAccount)) {
            return false;
        }
        CreditCardAccount other = (CreditCardAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.card.account.CreditCard[ id=" + id + " ]";
    }

    public BigDecimal getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(BigDecimal creditCardNum) {
        this.creditCardNum = creditCardNum;
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

}
