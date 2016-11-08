/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author qiuxiaqing
 */
public class TransactionSummaryDTO {

    private List<TransactionDTO> transactionSummary = new ArrayList<>();
    private String citiToBankCode;
    private String citiToBankName;
    private String citiFromBankCode;
    private String citiFromBankName;
    private String citiSettlementAmount;
    private String ocbcToBankCode;
    private String ocbcToBankName;
    private String ocbcFromBankCode;
    private String ocbcFromBankName;
    private String ocbcSettlementAmount;
    private String date;

    /**
     * @return the transactionSummary
     */
    public List<TransactionDTO> getTransactionSummary() {
        return transactionSummary;
    }

    /**
     * @param transactionSummary the transactionSummary to set
     */
    public void setTransactionSummary(List<TransactionDTO> transactionSummary) {
        this.transactionSummary = transactionSummary;
    }

    /**
     * @return the citiToBankCode
     */
    public String getCitiToBankCode() {
        return citiToBankCode;
    }

    /**
     * @param citiToBankCode the citiToBankCode to set
     */
    public void setCitiToBankCode(String citiToBankCode) {
        this.citiToBankCode = citiToBankCode;
    }

    /**
     * @return the citiToBankName
     */
    public String getCitiToBankName() {
        return citiToBankName;
    }

    /**
     * @param citiToBankName the citiToBankName to set
     */
    public void setCitiToBankName(String citiToBankName) {
        this.citiToBankName = citiToBankName;
    }

    /**
     * @return the citiSettlementAmount
     */
    public String getCitiSettlementAmount() {
        return citiSettlementAmount;
    }

    /**
     * @param citiSettlementAmount the citiSettlementAmount to set
     */
    public void setCitiSettlementAmount(String citiSettlementAmount) {
        this.citiSettlementAmount = citiSettlementAmount;
    }

    /**
     * @return the ocbcToBankCode
     */
    public String getOcbcToBankCode() {
        return ocbcToBankCode;
    }

    /**
     * @param ocbcToBankCode the ocbcToBankCode to set
     */
    public void setOcbcToBankCode(String ocbcToBankCode) {
        this.ocbcToBankCode = ocbcToBankCode;
    }

    /**
     * @return the ocbcToBankName
     */
    public String getOcbcToBankName() {
        return ocbcToBankName;
    }

    /**
     * @param ocbcToBankName the ocbcToBankName to set
     */
    public void setOcbcToBankName(String ocbcToBankName) {
        this.ocbcToBankName = ocbcToBankName;
    }

    /**
     * @return the ocbcSettlementAmount
     */
    public String getOcbcSettlementAmount() {
        return ocbcSettlementAmount;
    }

    /**
     * @param ocbcSettlementAmount the ocbcSettlementAmount to set
     */
    public void setOcbcSettlementAmount(String ocbcSettlementAmount) {
        this.ocbcSettlementAmount = ocbcSettlementAmount;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the citiFromBankCode
     */
    public String getCitiFromBankCode() {
        return citiFromBankCode;
    }

    /**
     * @param citiFromBankCode the citiFromBankCode to set
     */
    public void setCitiFromBankCode(String citiFromBankCode) {
        this.citiFromBankCode = citiFromBankCode;
    }

    /**
     * @return the citiFromBankName
     */
    public String getCitiFromBankName() {
        return citiFromBankName;
    }

    /**
     * @param citiFromBankName the citiFromBankName to set
     */
    public void setCitiFromBankName(String citiFromBankName) {
        this.citiFromBankName = citiFromBankName;
    }

    /**
     * @return the ocbcFromBankCode
     */
    public String getOcbcFromBankCode() {
        return ocbcFromBankCode;
    }

    /**
     * @param ocbcFromBankCode the ocbcFromBankCode to set
     */
    public void setOcbcFromBankCode(String ocbcFromBankCode) {
        this.ocbcFromBankCode = ocbcFromBankCode;
    }

    /**
     * @return the ocbcFromBankName
     */
    public String getOcbcFromBankName() {
        return ocbcFromBankName;
    }

    /**
     * @param ocbcFromBankName the ocbcFromBankName to set
     */
    public void setOcbcFromBankName(String ocbcFromBankName) {
        this.ocbcFromBankName = ocbcFromBankName;
    }
}
