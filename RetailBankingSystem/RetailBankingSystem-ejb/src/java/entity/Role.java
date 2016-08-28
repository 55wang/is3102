/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author wang
 */
@Entity
public class Role implements Serializable {
    
    public enum Permission {
        SUPERUSER, 
        CUSTOMER,
        DEPOSIT,
        CARD,
        LOAN,
        BILL,
        WEALTH,
        PORTFOLIO,
        ANALYTICS
    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
    private Boolean superUserRight;
    private Boolean customerAccessRight;
    private Boolean depositAccessRight;
    private Boolean cardAccessRight;
    private Boolean loanAccessRight;
    private Boolean billAccessRight;
    private Boolean wealthAccessRight;
    private Boolean portfolioAccessRight;
    private Boolean analyticsAccessRight;
//    @ManyToMany(cascade={CascadeType.PERSIST})
//    private Set<StaffAccount> staffAccounts = new HashSet<StaffAccount>();
    
    

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
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Role[ id=" + id + " ]";
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getDepositAccessRight() {
        return depositAccessRight;
    }

    public void setDepositAccessRight(Boolean depositAccessRight) {
        this.depositAccessRight = depositAccessRight;
    }

    public Boolean getCardAccessRight() {
        return cardAccessRight;
    }

    public void setCardAccessRight(Boolean cardAccessRight) {
        this.cardAccessRight = cardAccessRight;
    }

    public Boolean getSuperUserRight() {
        return superUserRight;
    }

    public void setSuperUserRight(Boolean superUserRight) {
        this.superUserRight = superUserRight;
    }

    public Boolean getCustomerAccessRight() {
        return customerAccessRight;
    }

    public void setCustomerAccessRight(Boolean customerAccessRight) {
        this.customerAccessRight = customerAccessRight;
    }

    public Boolean getLoanAccessRight() {
        return loanAccessRight;
    }

    public void setLoanAccessRight(Boolean loanAccessRight) {
        this.loanAccessRight = loanAccessRight;
    }

    public Boolean getBillAccessRight() {
        return billAccessRight;
    }

    public void setBillAccessRight(Boolean billAccessRight) {
        this.billAccessRight = billAccessRight;
    }

    public Boolean getWealthAccessRight() {
        return wealthAccessRight;
    }

    public void setWealthAccessRight(Boolean wealthAccessRight) {
        this.wealthAccessRight = wealthAccessRight;
    }

    public Boolean getPortfolioAccessRight() {
        return portfolioAccessRight;
    }

    public void setPortfolioAccessRight(Boolean portfolioAccessRight) {
        this.portfolioAccessRight = portfolioAccessRight;
    }

    public Boolean getAnalyticsAccessRight() {
        return analyticsAccessRight;
    }

    public void setAnalyticsAccessRight(Boolean analyticsAccessRight) {
        this.analyticsAccessRight = analyticsAccessRight;
    }

//    public Set<StaffAccount> getStaffAccounts() {
//        return staffAccounts;
//    }
//
//    public void setStaffAccounts(Set<StaffAccount> staffAccounts) {
//        this.staffAccounts = staffAccounts;
//    }
   
}
