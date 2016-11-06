/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocal.swift;

/**
 *
 * @author leiyang
 */
public class SwiftMessage {

    // http://www.sepaforcorporates.com/swift-for-corporates/read-swift-message-structure/
    // https://docs.oracle.com/cd/E21454_01/html/821-2582/ahcbi.html#ahccn
    // https://docs.oracle.com/cd/E64763_01/html/MS/MS13_SWIFT_Messages.htm#Rar_toc325460218
    // block 1 -- Basic Header Block

    private String applicationId = "F"; // FIN
    private String serviceId = "01"; // FIN
    private String selfBIC = "12345678"; // 8 degit
    private String selfLogicalTerminalCode = "Z";
    private String selfBranchCode = "007";
    private String sessionNumber = "1234";
    private String sequenceNumber = "567890";
    // end of block 1

    // block 2 -- Application Header Block
    private String mode = "I"; // I is input - sender, O is output - receiver
    private String messageType = "101";
    private String otherBIC = "12345678"; // 8 degit
    private String otherLogicalTerminalCode = "Z";
    private String otherBranchCode = "007";
    private String messagePriority = "U"; // U - Urgent, N - Normal, S - System
    private String deliveryMonitoring = "3"; // optional
    private String nonDeliveryNotificationPeriod = "003"; // optional
    // end of block 2

    // block 3 -- User Header Block
    private String bankPriorityCode = "ABCD";
    private String messageUserReference = "ABCDEFGHABCDEFGH";// up to 16 characters
    // end of block 3

    // block 4 -- Text Block
    private String message = ":20:1106210404010000\n"
            + ":28D:1/1\n"
            + ":50H:/PL64114010100000123456001001\n"
            + "ORDERING CUSTOMER NAME1\n"
            + "ORDERING CUSTOMER NAME2\n"
            + "STREET\n"
            + "CITY\n"
            + ":52A:BREXPLPWXXX\n"
            + ":30:110621\n"
            + ":21:2011062100000002\n"
            + ":32B:PLN369,85\n"
            + ":57A:\n"
            + ":59:/ 56101010100166232222000000\n"
            + "URZAD SKARFBOWY\n"
            + "STREET\n"
            + "CITY\n"
            + ":70:/TI/N1070002939/OKR/12M01/SFP/VAT7\n"
            + "/TXT/ VAT7 ZA STYCZEN\n"
            + ":71A:SHA\n"; //individual message from messageType
    // end of block 4

    // block 5 -- Thrailer Block
    private String messageAuthenticationCode = "12345678";
    private String checksum = "123456789ABC";

    public String getBasicHeaderBlock() {
        return "Basic Header Block: {" + getApplicationId() + getServiceId() + getSelfBIC() + getSelfLogicalTerminalCode() + getSelfBranchCode() + getSessionNumber() + getSequenceNumber() + "}";
    }

    public String getApplicationHeaderBlock() {
        return "Application Header Block: {" + getMode() + getMessageType() + getOtherBIC() + getOtherLogicalTerminalCode() + getOtherBranchCode() + getMessagePriority() + getDeliveryMonitoring() + getNonDeliveryNotificationPeriod() + "}";
    }

    public String getUserHeaderBlock() {
        return "User Header Block: {" + getBankPriorityCode() + getMessageUserReference() + "}";
    }

    public String getTextBlock() {
        return "Message Content: {" + getMessage() + "_}";
    }
    
    public String getTrailerBlock() {
        return "TrailerBlock: {" + 
                "{MAC:" + getMessageAuthenticationCode() + "}" +
                "{CHK:" + getChecksum() + "}" +
                "}";
    }
    
    @Override
    public String toString() {
        return "SWIFT message:\n"+ getBasicHeaderBlock() + "\n" + getApplicationHeaderBlock() + "\n" + getUserHeaderBlock() + "\n" + getTextBlock() + "\n" + getTrailerBlock();
    }

    // Getter and Setters
    /**
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the selfBIC
     */
    public String getSelfBIC() {
        return selfBIC;
    }

    /**
     * @param selfBIC the selfBIC to set
     */
    public void setSelfBIC(String selfBIC) {
        this.selfBIC = selfBIC;
    }

    /**
     * @return the selfLogicalTerminalCode
     */
    public String getSelfLogicalTerminalCode() {
        return selfLogicalTerminalCode;
    }

    /**
     * @param selfLogicalTerminalCode the selfLogicalTerminalCode to set
     */
    public void setSelfLogicalTerminalCode(String selfLogicalTerminalCode) {
        this.selfLogicalTerminalCode = selfLogicalTerminalCode;
    }

    /**
     * @return the selfBranchCode
     */
    public String getSelfBranchCode() {
        return selfBranchCode;
    }

    /**
     * @param selfBranchCode the selfBranchCode to set
     */
    public void setSelfBranchCode(String selfBranchCode) {
        this.selfBranchCode = selfBranchCode;
    }

    /**
     * @return the sessionNumber
     */
    public String getSessionNumber() {
        return sessionNumber;
    }

    /**
     * @param sessionNumber the sessionNumber to set
     */
    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    /**
     * @return the sequenceNumber
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the otherBIC
     */
    public String getOtherBIC() {
        return otherBIC;
    }

    /**
     * @param otherBIC the otherBIC to set
     */
    public void setOtherBIC(String otherBIC) {
        this.otherBIC = otherBIC;
    }

    /**
     * @return the otherLogicalTerminalCode
     */
    public String getOtherLogicalTerminalCode() {
        return otherLogicalTerminalCode;
    }

    /**
     * @param otherLogicalTerminalCode the otherLogicalTerminalCode to set
     */
    public void setOtherLogicalTerminalCode(String otherLogicalTerminalCode) {
        this.otherLogicalTerminalCode = otherLogicalTerminalCode;
    }

    /**
     * @return the otherBranchCode
     */
    public String getOtherBranchCode() {
        return otherBranchCode;
    }

    /**
     * @param otherBranchCode the otherBranchCode to set
     */
    public void setOtherBranchCode(String otherBranchCode) {
        this.otherBranchCode = otherBranchCode;
    }

    /**
     * @return the messagePriority
     */
    public String getMessagePriority() {
        return messagePriority;
    }

    /**
     * @param messagePriority the messagePriority to set
     */
    public void setMessagePriority(String messagePriority) {
        this.messagePriority = messagePriority;
    }

    /**
     * @return the deliveryMonitoring
     */
    public String getDeliveryMonitoring() {
        return deliveryMonitoring;
    }

    /**
     * @param deliveryMonitoring the deliveryMonitoring to set
     */
    public void setDeliveryMonitoring(String deliveryMonitoring) {
        this.deliveryMonitoring = deliveryMonitoring;
    }

    /**
     * @return the nonDeliveryNotificationPeriod
     */
    public String getNonDeliveryNotificationPeriod() {
        return nonDeliveryNotificationPeriod;
    }

    /**
     * @param nonDeliveryNotificationPeriod the nonDeliveryNotificationPeriod to set
     */
    public void setNonDeliveryNotificationPeriod(String nonDeliveryNotificationPeriod) {
        this.nonDeliveryNotificationPeriod = nonDeliveryNotificationPeriod;
    }

    /**
     * @return the bankPriorityCode
     */
    public String getBankPriorityCode() {
        return bankPriorityCode;
    }

    /**
     * @param bankPriorityCode the bankPriorityCode to set
     */
    public void setBankPriorityCode(String bankPriorityCode) {
        this.bankPriorityCode = bankPriorityCode;
    }

    /**
     * @return the messageUserReference
     */
    public String getMessageUserReference() {
        return messageUserReference;
    }

    /**
     * @param messageUserReference the messageUserReference to set
     */
    public void setMessageUserReference(String messageUserReference) {
        this.messageUserReference = messageUserReference;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the messageAuthenticationCode
     */
    public String getMessageAuthenticationCode() {
        return messageAuthenticationCode;
    }

    /**
     * @param messageAuthenticationCode the messageAuthenticationCode to set
     */
    public void setMessageAuthenticationCode(String messageAuthenticationCode) {
        this.messageAuthenticationCode = messageAuthenticationCode;
    }

    /**
     * @return the checksum
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * @param checksum the checksum to set
     */
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
