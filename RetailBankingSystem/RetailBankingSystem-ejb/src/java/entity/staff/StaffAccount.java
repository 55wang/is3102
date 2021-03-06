/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.staff;

import entity.common.AuditLog;
import entity.crm.MarketingCampaign;
import entity.customer.CustomerCase;
import entity.customer.WealthManagementSubscriber;
import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import entity.wealth.InvestplanCommunication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.StatusType;

/**
 *
 * @author wang
 */
@Entity
public class StaffAccount implements Serializable {

    // info
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    @Column(unique = true)
    private String email;
    private StatusType status = StatusType.PENDING;
    private String address;
    private String postalCode;
    private String phone;
    private EnumUtils.Nationality nationality;
    private EnumUtils.Gender gender;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay;

    // mapping
    @ManyToMany(mappedBy = "staffAccounts")
    private List<Role> roles = new ArrayList<>(); // Role already consist of list of permissions
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "staffAccount")
    private List<AuditLog> auditLog = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "sender")
    private List<Conversation> senderConversation = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "receiver")
    private List<Conversation> receiverConversation = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "rm")
    private List<InvestplanCommunication> investplanCommunications = new ArrayList<>();
    @OneToMany(mappedBy = "staffAccount")
    private List<CustomerCase> cases = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "relationshipManager")
    private List<WealthManagementSubscriber> wealthManagementSubscribers = new ArrayList<>();
    @OneToMany(mappedBy = "staffAccount")
    private List<MarketingCampaign> marketingCampaign = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "loanOfficer")
    private List<LoanAccount> loanAccounts = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "loanOfficer")
    private List<LoanApplication> loanApplications = new ArrayList<>();

    public String getNameLabel() {
        return this.getFirstName().substring(0, 1).toUpperCase() + this.getLastName().substring(0, 1).toUpperCase();
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public void addRole(Role r) {
        roles.add(r);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.username);
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
        final StaffAccount other = (StaffAccount) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "StaffAccount{" + "username=" + getUsername() + ", firstName=" + getFirstName() + ", lastName=" + getLastName() + ", password=" + getPassword() + ", email=" + getEmail() + ", status=" + getStatus() + ", address=" + getAddress() + ", postalCode=" + getPostalCode() + ", phone=" + getPhone() + ", nationality=" + getNationality() + ", gender=" + getGender() + ", birthDay=" + getBirthDay() + ", roles=" + roles + ", auditLog=" + auditLog + ", senderConversation=" + senderConversation + ", receiverConversation=" + receiverConversation + ", investplanCommunications=" + investplanCommunications + ", cases=" + cases + ", wealthManagementSubscribers=" + wealthManagementSubscribers + ", marketingCampaign=" + marketingCampaign + ", loanAccounts=" + loanAccounts + ", loanApplications=" + loanApplications + '}';
    }
    
    

    // Getter and Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the senderConversation
     */
    public List<Conversation> getSenderConversation() {
        return senderConversation;
    }

    /**
     * @param senderConversation the senderConversation to set
     */
    public void setSenderConversation(List<Conversation> senderConversation) {
        this.senderConversation = senderConversation;
    }

    /**
     * @return the receiverConversation
     */
    public List<Conversation> getReceiverConversation() {
        return receiverConversation;
    }

    /**
     * @param receiverConversation the receiverConversation to set
     */
    public void setReceiverConversation(List<Conversation> receiverConversation) {
        this.receiverConversation = receiverConversation;
    }

    /**
     * @return the auditLog
     */
    public List<AuditLog> getAuditLog() {
        return auditLog;
    }

    /**
     * @param auditLog the auditLog to set
     */
    public void setAuditLog(List<AuditLog> auditLog) {
        this.auditLog = auditLog;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public List<CustomerCase> getCases() {
        return cases;
    }

    public void setCases(List<CustomerCase> cases) {
        this.cases = cases;
    }

    /**
     * @return the status
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    public List<WealthManagementSubscriber> getWealthManagementSubscribers() {
        return wealthManagementSubscribers;
    }

    public void setWealthManagementSubscribers(List<WealthManagementSubscriber> wealthManagementSubscribers) {
        this.wealthManagementSubscribers = wealthManagementSubscribers;
    }

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<MarketingCampaign> getMarketingCampaign() {
        return marketingCampaign;
    }

    public void setMarketingCampaign(List<MarketingCampaign> marketingCampaign) {
        this.marketingCampaign = marketingCampaign;
    }

    /**
     * @return the loanAccounts
     */
    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    /**
     * @param loanAccounts the loanAccounts to set
     */
    public void setLoanAccounts(List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
    }

    /**
     * @return the loanApplications
     */
    public List<LoanApplication> getLoanApplications() {
        return loanApplications;
    }

    /**
     * @param loanApplications the loanApplications to set
     */
    public void setLoanApplications(List<LoanApplication> loanApplications) {
        this.loanApplications = loanApplications;
    }

    public List<InvestplanCommunication> getInvestplanCommunications() {
        return investplanCommunications;
    }

    public void setInvestplanCommunications(List<InvestplanCommunication> investplanCommunications) {
        this.investplanCommunications = investplanCommunications;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the nationality
     */
    public EnumUtils.Nationality getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(EnumUtils.Nationality nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the gender
     */
    public EnumUtils.Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(EnumUtils.Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the birthDay
     */
    public Date getBirthDay() {
        return birthDay;
    }

    /**
     * @param birthDay the birthDay to set
     */
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
