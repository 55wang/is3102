/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import entity.card.account.CardTransaction;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import server.utilities.EnumUtils.CaseStatus;
import server.utilities.EnumUtils.cardOperatorChargebackStatus;

/**
 *
 * @author VIN-S
 */
@Entity
public class CustomerCase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createDate;
    
    // info
    private String title;
    private CaseStatus caseStatus;
    private Boolean isChargeBackCase = false;
    private cardOperatorChargebackStatus cardOperatorResponse;
    
    // mappings
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "customerCase")
    private List<Issue> issues = new ArrayList<>(); 
    @ManyToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;
    @ManyToOne
    private StaffAccount staffAccount;
    @OneToOne
    private CardTransaction chargebackTransaction;
    @OneToOne
    private CardTransaction reverseTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public StaffAccount getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
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
        if (!(object instanceof CustomerCase)) {
            return false;
        }
        CustomerCase other = (CustomerCase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public CaseStatus getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(CaseStatus caseStatus) {
        this.caseStatus = caseStatus;
    }

    @Override
    public String toString() {
        return "entity.customer.Case[ id=" + id + " ]";
    }   

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public CardTransaction getChargebackTransaction() {
        return chargebackTransaction;
    }

    public void setChargebackTransaction(CardTransaction chargebackTransaction) {
        this.chargebackTransaction = chargebackTransaction;
    }

    public Boolean getIsChargeBackCase() {
        return isChargeBackCase;
    }

    public void setIsChargeBackCase(Boolean isChargeBackCase) {
        this.isChargeBackCase = isChargeBackCase;
    }

    public cardOperatorChargebackStatus getCardOperatorResponse() {
        return cardOperatorResponse;
    }

    public void setCardOperatorResponse(cardOperatorChargebackStatus cardOperatorResponse) {
        this.cardOperatorResponse = cardOperatorResponse;
    }

    public CardTransaction getReverseTransaction() {
        return reverseTransaction;
    }

    public void setReverseTransaction(CardTransaction reverseTransaction) {
        this.reverseTransaction = reverseTransaction;
    }
}
