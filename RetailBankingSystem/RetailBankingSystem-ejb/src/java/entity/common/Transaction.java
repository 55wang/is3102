/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.common;

import entity.dams.account.DepositAccount;
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
    private DepositAccount fromAccount;
    @ManyToOne(cascade={CascadeType.PERSIST})
    private DepositAccount toAccount;// can be null
    private String referenceNumber;
    private String actionType;
    private Boolean credit;
    private BigDecimal amount;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the fromAccount
     */
    public DepositAccount getFromAccount() {
        return fromAccount;
    }

    /**
     * @param fromAccount the fromAccount to set
     */
    public void setFromAccount(DepositAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    /**
     * @return the toAccount
     */
    public DepositAccount getToAccount() {
        return toAccount;
    }

    /**
     * @param toAccount the toAccount to set
     */
    public void setToAccount(DepositAccount toAccount) {
        this.toAccount = toAccount;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber toAccount set
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
     * @param actionType the actionType toAccount set
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
     * @param credit the credit toAccount set
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
     * @param amount the amount toAccount set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
