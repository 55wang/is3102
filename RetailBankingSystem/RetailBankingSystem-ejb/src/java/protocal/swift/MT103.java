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
public class MT103 {
    
    private String senderReference = "senderReference";
    private String bankOperationCode = "bankOperationCode";
    private String valueCurrencyInterbankSettledAmount = "valueCurrencyInterbankSettledAmount";
    private String orderingCustomer = "orderingCustomer";
    private String beneficiaryCustomer = "beneficiaryCustomer";
    private String detailsOfCharges = "detailsOfCharges";

    @Override
    public String toString() {
        return 
                "\n:20:" + senderReference + 
                "\n:23B:" + bankOperationCode + 
                "\n:32A:" + valueCurrencyInterbankSettledAmount + 
                "\n:50a:" + orderingCustomer + 
                "\n:59a:" + beneficiaryCustomer + 
                "\n:71A:" + detailsOfCharges + "\n";
    }

    
    
    /**
     * @return the senderReference
     */
    public String getSenderReference() {
        return senderReference;
    }

    /**
     * @param senderReference the senderReference to set
     */
    public void setSenderReference(String senderReference) {
        this.senderReference = senderReference;
    }

    /**
     * @return the bankOperationCode
     */
    public String getBankOperationCode() {
        return bankOperationCode;
    }

    /**
     * @param bankOperationCode the bankOperationCode to set
     */
    public void setBankOperationCode(String bankOperationCode) {
        this.bankOperationCode = bankOperationCode;
    }

    /**
     * @return the valueCurrencyInterbankSettledAmount
     */
    public String getValueCurrencyInterbankSettledAmount() {
        return valueCurrencyInterbankSettledAmount;
    }

    /**
     * @param valueCurrencyInterbankSettledAmount the valueCurrencyInterbankSettledAmount to set
     */
    public void setValueCurrencyInterbankSettledAmount(String valueCurrencyInterbankSettledAmount) {
        this.valueCurrencyInterbankSettledAmount = valueCurrencyInterbankSettledAmount;
    }

    /**
     * @return the orderingCustomer
     */
    public String getOrderingCustomer() {
        return orderingCustomer;
    }

    /**
     * @param orderingCustomer the orderingCustomer to set
     */
    public void setOrderingCustomer(String orderingCustomer) {
        this.orderingCustomer = orderingCustomer;
    }

    /**
     * @return the beneficiaryCustomer
     */
    public String getBeneficiaryCustomer() {
        return beneficiaryCustomer;
    }

    /**
     * @param beneficiaryCustomer the beneficiaryCustomer to set
     */
    public void setBeneficiaryCustomer(String beneficiaryCustomer) {
        this.beneficiaryCustomer = beneficiaryCustomer;
    }

    /**
     * @return the detailsOfCharges
     */
    public String getDetailsOfCharges() {
        return detailsOfCharges;
    }

    /**
     * @param detailsOfCharges the detailsOfCharges to set
     */
    public void setDetailsOfCharges(String detailsOfCharges) {
        this.detailsOfCharges = detailsOfCharges;
    }
}
