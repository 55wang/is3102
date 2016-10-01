/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.staff;

import entity.common.AuditLog;
import entity.customer.CustomerCase;
import entity.embedded.StaffInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @Embedded
    private StaffInfo staffInfo = new StaffInfo();
    
    // mapping
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Role role; // Role already consist of list of permissions
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "staffAccount")
    private List<AuditLog> auditLog = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "sender")
    private List<Conversation> senderConversation = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "receiver")
    private List<Conversation> receiverConversation = new ArrayList<>();
    @OneToMany(mappedBy = "staffAccount")
    private List<CustomerCase> cases = new ArrayList<>();

    public String getNameLabel() {
        return this.getFirstName().substring(0, 1).toUpperCase() + this.getLastName().substring(0, 1).toUpperCase();
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaffAccount)) {
            return false;
        }
        StaffAccount other = (StaffAccount) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StaffAccount{" + "username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password + ", email=" + email + ", status=" + status + ", staffInfo=" + staffInfo + ", role=" + role + ", auditLog=" + auditLog + ", senderConversation=" + senderConversation + ", receiverConversation=" + receiverConversation + ", cases=" + cases + '}';
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
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
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

    /**
     * @return the staffInfo
     */
    public StaffInfo getStaffInfo() {
        return staffInfo;
    }

    /**
     * @param staffInfo the staffInfo to set
     */
    public void setStaffInfo(StaffInfo staffInfo) {
        this.staffInfo = staffInfo;
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
}
