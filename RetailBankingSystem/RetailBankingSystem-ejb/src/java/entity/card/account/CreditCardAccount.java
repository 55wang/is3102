/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.card.account;

import entity.card.order.CreditCardOrder;
import entity.card.product.PromoCode;
import entity.card.product.CreditCardProduct;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EnumUtils.*;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class CreditCardAccount implements Serializable {

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public CreditCardAccount() {
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date closeDate = new Date();

    // info
    @Temporal(value = TemporalType.DATE)
    private Date validDate = DateUtils.getDateForNextNthYear(5);
    private CardAccountStatus CardStatus = CardAccountStatus.PENDING;
    @Column(unique = true)
    private String creditCardNum;
    private String cvv;
    private String nameOnCard;

    private Double transactionMonthlyLimit = 1000.0;
    private Double transactionDailyLimit = 500.0;
    private Double creditLimit = 0.0;

    private Double annualInterestRate = 0.24; //24% annual Integererest rate

    private Double currentMonthAmount = 0.0;
    private Double outstandingAmount = 0.0;
    private Double latePaymentFee = 0.0; //store late payment fee
    private Double minPayDue = 0.0; //store minPayDue Amount

    private Double cashBackAmount = 0.0;
    private Double merlionMiles = 0.0;
    private Double merlionPoints = 0.0;

    // for bad credit record
    private Integer numOfLatePayment = 0; //count of total late payment
    private Integer numOf_30_59_LatePayment = 0; //count of 30-59 days overdue
    private Integer numOf_60_89_LatePayment = 0; //count of 60-89 days overdue
    private Integer numOf_90_LatePayment = 0; //count of >= 90 days overdue
    @Temporal(value = TemporalType.DATE)
    private Date lastLatePaymentDate;

    @Temporal(value = TemporalType.DATE)
    private Date cutOffDate; //set date when cut off
    @Temporal(value = TemporalType.DATE)
    private Date billDate; //set bill date after cut off
    @Temporal(value = TemporalType.DATE)
    private Date overDueDate; //set overdue date after bill sent

    // mappings 
    @ManyToOne(cascade = CascadeType.MERGE)
    private MainAccount mainAccount;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private CreditCardProduct creditCardProduct;
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "creditCardAccount")
    private List<PromoCode> promoCode = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "creditCardAccount")
    private List<CardTransaction> cardTransactions = new ArrayList<>();
    @OneToOne(cascade = {CascadeType.MERGE})
    private CreditCardOrder creditCardOrder;

    public Double retrievePrePaidAmount() {
        if (getOutstandingAmount() > 0.0) {
            return 0.0;
        } else {
            return 0.0 - getOutstandingAmount();
        }
    }

    public Double getRemainingCreditLimit() {
        if (getOutstandingAmount() > 0.0) {
            return getCreditLimit() - getCurrentMonthAmount();
        } else {
            return getCreditLimit() - getCurrentMonthAmount() - getOutstandingAmount();
        }
    }

    public Double calculateMinPayDue() {
        Double minPay = 50.0;
        if (minPay >= (getOutstandingAmount() / 100.0)) {
            if (minPay >= getOutstandingAmount()) {
                return getOutstandingAmount();
            }
            return minPay;
        } else {
            return (getOutstandingAmount() / 100.0);
        }
    }

    public Double calculateCurrentMonthlyInterest() {
        return getOutstandingAmount() * (getAnnualInterestRate() / 12);
    }

    public String getPartialHiddenAccountNumber() {
        return this.creditCardNum.substring(
                this.creditCardNum.length() - 4,
                this.creditCardNum.length()
        );
    }

    public Integer getOverDueDays() {
        if (this.overDueDate != null) {
            Calendar today = Calendar.getInstance();
            Calendar overDueCalDate = DateUtils.dateToCalender(this.overDueDate);
            return DateUtils.dayDifference(today, overDueCalDate);
        } else {
            return 0;
        }
    }

    public void payOutstandingAmount(BigDecimal amount) {
        outstandingAmount = outstandingAmount - amount.doubleValue();
    }

    @Override
    public String toString() {
        return "CreditCardAccount{" + "id=" + id + ", mainAccount=" + mainAccount + ", creditCardProduct=" + creditCardProduct + ", promoCode=" + promoCode + ", cardTransactions=" + cardTransactions + ", creditCardOrder=" + creditCardOrder + ", CardStatus=" + CardStatus + ", creditCardNum=" + creditCardNum + ", cvv=" + cvv + ", nameOnCard=" + nameOnCard + ", validDate=" + validDate + ", transactionMonthlyLimit=" + transactionMonthlyLimit + ", transactionDailyLimit=" + transactionDailyLimit + ", creditLimit=" + creditLimit + ", creationDate=" + getCreationDate() + ", annualInterestRate=" + annualInterestRate + ", outstandingAmount=" + outstandingAmount + ", cashBackAmount=" + cashBackAmount + ", merlionMiles=" + merlionMiles + ", merlionPoints=" + merlionPoints + ", numOfLatePayment=" + numOfLatePayment + ", numOf_30_59_LatePayment=" + numOf_30_59_LatePayment + ", numOf_60_89_LatePayment=" + numOf_60_89_LatePayment + ", numOf_90_LatePayment=" + '}';
    }

    public void deductPoints(Double amount) {
        this.setMerlionPoints((Double) (this.getMerlionPoints() - amount));
    }

    public void deductMiles(Double amount) {
        this.setMerlionMiles((Double) (this.getMerlionMiles() - amount));
    }

    public void addPromoCode(PromoCode pc) {
        this.promoCode.add(pc);
    }

    public void addCurrentMonthAmountToOutstandingAmount() {
        this.outstandingAmount += currentMonthAmount;
    }

    public Double addAmountToCurrentMonthAmount(Double amount) {
        this.currentMonthAmount += amount;
        return currentMonthAmount;
    }

    public void addTransactions(CardTransaction ct) {
        cardTransactions.add(ct);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(Double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public Integer getNumOfLatePayment() {
        return numOfLatePayment;
    }

    public void setNumOfLatePayment(Integer numOfLatePayment) {
        this.numOfLatePayment = numOfLatePayment;
    }

    public Integer getNumOf_30_59_LatePayment() {
        return numOf_30_59_LatePayment;
    }

    public void setNumOf_30_59_LatePayment(Integer numOf_30_59_LatePayment) {
        this.numOf_30_59_LatePayment = numOf_30_59_LatePayment;
    }

    public Integer getNumOf_60_89_LatePayment() {
        return numOf_60_89_LatePayment;
    }

    public void setNumOf_60_89_LatePayment(Integer numOf_60_89_LatePayment) {
        this.numOf_60_89_LatePayment = numOf_60_89_LatePayment;
    }

    public Integer getNumOf_90_LatePayment() {
        return numOf_90_LatePayment;
    }

    public void setNumOf_90_LatePayment(Integer numOf_90_LatePayment) {
        this.numOf_90_LatePayment = numOf_90_LatePayment;
    }

    public Double getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(Double cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }

    public Double getMerlionMiles() {
        return merlionMiles;
    }

    public void setMerlionMiles(Double merlionMiles) {
        this.merlionMiles = merlionMiles;
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

    public Double getTransactionDailyLimit() {
        return transactionDailyLimit;
    }

    public void setTransactionDailyLimit(Double transactionDailyLimit) {
        this.transactionDailyLimit = transactionDailyLimit;
    }

    public Double getTransactionMonthlyLimit() {
        return transactionMonthlyLimit;
    }

    public void setTransactionMonthlyLimit(Double transactionMonthlyLimit) {
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

    public List<CardTransaction> getCardTransactions() {
        return cardTransactions;
    }

    public void setCardTransactions(List<CardTransaction> cardTransactions) {
        this.cardTransactions = cardTransactions;
    }

    public CreditCardOrder getCreditCardOrder() {
        return creditCardOrder;
    }

    public void setCreditCardOrder(CreditCardOrder creditCardOrder) {
        this.creditCardOrder = creditCardOrder;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    /**
     * @return the cvv
     */
    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * @return the merlionPoints
     */
    public Double getMerlionPoints() {
        return merlionPoints;
    }

    /**
     * @param merlionPoints the merlionPoints to set
     */
    public void setMerlionPoints(Double merlionPoints) {
        this.merlionPoints = merlionPoints;
    }

    public Double getLatePaymentFee() {
        return latePaymentFee;
    }

    public void setLatePaymentFee(Double latePaymentFee) {
        this.latePaymentFee = latePaymentFee;
    }

    public Double getInterestAmount() {
        return calculateCurrentMonthlyInterest();
    }

    public Double getCurrentMonthAmount() {
        return currentMonthAmount;
    }

    public void setCurrentMonthAmount(Double currentMonthAmount) {
        this.currentMonthAmount = currentMonthAmount;
    }

    public Double getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public Date getOverDueDate() {
        return overDueDate;
    }

    public void setOverDueDate(Date overDueDate) {
        this.overDueDate = overDueDate;
    }

    public Date getCreationDate() { //card creatuion date
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(Date cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Double getMinPayDue() {
        return minPayDue;
    }

    public void setMinPayDue(Double minPayDue) {
        this.minPayDue = minPayDue;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Date getLastLatePaymentDate() {
        return lastLatePaymentDate;
    }

    public void setLastLatePaymentDate(Date lastLatePaymentDate) {
        this.lastLatePaymentDate = lastLatePaymentDate;
    }

}
