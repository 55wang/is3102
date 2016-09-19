/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.staff;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author wang
 */
@Entity
public class Role implements Serializable {
    
    @Id
    private String roleName;
    private Boolean superUserRight = false;
    private Boolean customerAccessRight = false;
    private Boolean depositAccessRight = false;
    private Boolean cardAccessRight = false;
    private Boolean loanAccessRight = false;
    private Boolean billAccessRight = false;
    private Boolean wealthAccessRight = false;
    private Boolean portfolioAccessRight = false;
    private Boolean analyticsAccessRight = false;
//    @OneToMany(cascade={CascadeType.MERGE}, mappedBy="role")
//    private List<StaffAccount> staffAccounts = new ArrayList<>();
    
    public Role() {
        
    }
    
    public Role(String name) {
        this.roleName = name;
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

//    public List<StaffAccount> getStaffAccounts() {
//        return staffAccounts;
//    }
//
//    public void setStaffAccounts(List<StaffAccount> staffAccounts) {
//        this.staffAccounts = staffAccounts;
//    }
//    
//    public void addStaffAccount(StaffAccount sa) {
//        this.staffAccounts.add(sa);
//    }
//   
//    public void removeStaffAccount(StaffAccount sa) {
//        this.staffAccounts.remove(sa);
//    }
    @Override
    public String toString() {
        return this.roleName;
    }
}
