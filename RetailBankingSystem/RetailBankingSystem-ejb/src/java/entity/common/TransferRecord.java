/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.common;

import javax.persistence.Entity;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.TransferPurpose;

/**
 *
 * @author leiyang
 */
@Entity
public class TransferRecord extends TransactionRecord {
    
    private TransferPurpose purpose;
    private Boolean isSettled = false;
    
    private String swiftCode;
    private String toBankCode;
    private String toBranchCode;
    // info
    private String name;
    private EnumUtils.PayeeType type;
    private String accountNumber;
    private String fromName;
    private String myInitial;

    /**
     * @return the purpose
     */
    public TransferPurpose getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(TransferPurpose purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the isSettled
     */
    public Boolean getIsSettled() {
        return isSettled;
    }

    /**
     * @param isSettled the isSettled to set
     */
    public void setIsSettled(Boolean isSettled) {
        this.isSettled = isSettled;
    }

    /**
     * @return the toBankCode
     */
    public String getToBankCode() {
        return toBankCode;
    }

    /**
     * @param toBankCode the toBankCode to set
     */
    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    /**
     * @return the toBranchCode
     */
    public String getToBranchCode() {
        return toBranchCode;
    }

    /**
     * @param toBranchCode the toBranchCode to set
     */
    public void setToBranchCode(String toBranchCode) {
        this.toBranchCode = toBranchCode;
    }

    /**
     * @return the swiftCode
     */
    public String getSwiftCode() {
        return swiftCode;
    }

    /**
     * @param swiftCode the swiftCode to set
     */
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public EnumUtils.PayeeType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(EnumUtils.PayeeType type) {
        this.type = type;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the fromName
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * @param fromName the fromName to set
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * @return the myInitial
     */
    public String getMyInitial() {
        return myInitial;
    }

    /**
     * @param myInitial the myInitial to set
     */
    public void setMyInitial(String myInitial) {
        this.myInitial = myInitial;
    }
}
