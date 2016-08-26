/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leiyang
 */
@Entity
public class Transaction implements Serializable {

    public enum ActionType {
        INITIAL {
            @Override
            public String toString() {
                return "INITIAL DEPOSIT";
            }
        },
        DEPOSIT {
            @Override
            public String toString() {
                return "DEPOSIT";
            }
        },
        WITHDRAW {
            @Override
            public String toString() {
                return "WITHDRAW";
            }
        },
        CHEQUE {
            @Override
            public String toString() {
                return "CHEQUE";
            }
        },
        TRANSFER {
            @Override
            public String toString() {
                return "TRANSFER";
            }
        },
        LOCALTRANSFER {
            @Override
            public String toString() {
                return "LOCAL TRANSFER";
            }
        },
        INTERBANKTRANSFER {
            @Override
            public String toString() {
                return "INTER BANK TRANSFER";
            }
        },
        OVERSEASTRANSFER {
            @Override
            public String toString() {
                return "OVERSEAS TRANSFER";
            }
        },
        BILL {
            @Override
            public String toString() {
                return "BILL";
            }
        }
    }
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade={CascadeType.PERSIST})
    private BankAccount payer;
    @ManyToOne(cascade={CascadeType.PERSIST})
    private BankAccount payee;// can be null
    private String referenceNumber;
    private String actionType;
    private Boolean credit;
    private BigDecimal amount;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the payer
     */
    public BankAccount getPayer() {
        return payer;
    }

    /**
     * @param payer the payer to set
     */
    public void setPayer(BankAccount payer) {
        this.payer = payer;
    }

    /**
     * @return the payee
     */
    public BankAccount getPayee() {
        return payee;
    }

    /**
     * @param payee the payee to set
     */
    public void setPayee(BankAccount payee) {
        this.payee = payee;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the Action
     */
    public String getAction() {
        return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public void setAction(String actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the credit
     */
    public Boolean getCredit() {
        return credit;
    }

    /**
     * @param credit the credit to set
     */
    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return creationDate;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date creationDate) {
        this.creationDate = creationDate;
    }
}
