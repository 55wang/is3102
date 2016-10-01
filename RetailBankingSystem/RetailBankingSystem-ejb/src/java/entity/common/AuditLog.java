/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.common;

import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import server.utilities.EncryptMethods;

/**
 *
 * @author wang
 */
@Entity
public class AuditLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    private String activityLog; //readable function name
    private String functionName;
    private String functionInput;
    private String functionOutput;
    private String ipAddress;
    
    // mapping
    @ManyToOne(cascade = {CascadeType.MERGE})
    private StaffAccount staffAccount;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;

    @Override
    public String toString() {
        return "AuditLog{" + "id=" + id + ", creationDate=" + creationDate + ", activityLog=" + activityLog + ", functionName=" + functionName + ", functionInput=" + getFunctionInput() + ", functionOutput=" + getFunctionOutput() + ", ipAddress=" + ipAddress + ", staffAccount=" + staffAccount + ", mainAccount=" + mainAccount + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityLog() {
        return EncryptMethods.AESDecrypt(activityLog);
//        return activityLog;
    }

    public void setActivityLog(String activityLog) {
        this.activityLog = EncryptMethods.AESEncrypt(activityLog);
    }

    public StaffAccount getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
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

    public String getFunctionName() {
        return EncryptMethods.AESDecrypt(functionName);
    }

    public void setFunctionName(String functionName) {
        this.functionName = EncryptMethods.AESEncrypt(functionName);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the functionInput
     */
    public String getFunctionInput() {
        return functionInput;
    }

    /**
     * @param functionInput the functionInput to set
     */
    public void setFunctionInput(String functionInput) {
        this.functionInput = functionInput;
    }

    /**
     * @return the functionOutput
     */
    public String getFunctionOutput() {
        return functionOutput;
    }

    /**
     * @param functionOutput the functionOutput to set
     */
    public void setFunctionOutput(String functionOutput) {
        this.functionOutput = functionOutput;
    }

}
